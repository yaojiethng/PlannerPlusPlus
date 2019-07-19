package com.orbital2019.plannerplusplus.view

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.model.entity.EventEntity
import com.orbital2019.plannerplusplus.viewmodel.EventUpdater
import kotlinx.android.synthetic.main.activity_add_edit_event.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE
import org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_TIME

const val EXTRA_SAVE_STATUS = "com.orbital2019.plannerplusplus.SAVE_STATUS"
const val EXTRA_PARCEL_PLANNEREVENT = "com.orbital2019.plannerplusplus.PARCEL_PLANNEREVENT"

// todo: de-clutter and modularize some of the methods in AddEditEventActivity, particularly the global constants (write bind and retreive methods)
class AddEditEventActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {


    private val eventUpdater: EventUpdater by lazy {
        ViewModelProviders.of(this).get(EventUpdater::class.java)
    }

    // Id is null by default
    private var eventId: Long? = null
    private val editTextTitle: EditText by lazy {
        findViewById<EditText>(R.id.edit_text_new_event_title)
    }

    private var date: LocalDate? = null
    private val editDateButton: Button by lazy {
        findViewById<Button>(R.id.date_dialog_button)
    }

    private var time: LocalTime? = null
    private val editTimeButton: Button by lazy {
        findViewById<Button>(R.id.time_dialog_button)
    }

    // todo: editDate and editTime as Dialogs
    private val editDate: DatePicker by lazy {
        findViewById<DatePicker>(R.id.date_picker_new_event)
    }
    private val editTime: TimePicker by lazy {
        findViewById<TimePicker>(R.id.time_picker_new_event)
    }

    private val switchRepeat: SwitchCompat by lazy {
        findViewById<SwitchCompat>(R.id.switch_repeat_new_event)
    }
    private val switchFollowUp: SwitchCompat by lazy {
        findViewById<SwitchCompat>(R.id.switch_followup_new_event)
    }
    // todo: add in tags as a fragment
    private val editTextDetails: EditText by lazy {
        findViewById<EditText>(R.id.edit_text_details)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AddEditEventActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_event)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)

        // bind Buttons to DialogFragments
        time_dialog_button.setOnClickListener {
            var timePicker: DialogFragment = TimePickerFragment()
            timePicker.show(supportFragmentManager, "Time_Picker")
        }

        // intent has a PlannerEvent Parcel, which means it is an edit event
        if (intent.hasExtra(EXTRA_PARCEL_PLANNEREVENT)) {
            title = "Edit Note"
            val event: EventEntity = intent.getParcelableExtra(EXTRA_PARCEL_PLANNEREVENT)!!
            bind(event)
        } else {
            title = "Add new Note"
            editTimeButton.text = LocalTime.now().format(ISO_LOCAL_TIME)
            editDateButton.text = LocalDate.now().format(ISO_LOCAL_DATE)
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        time = LocalTime.of(hourOfDay, minute)
        editTimeButton.text = time!!.format(ISO_LOCAL_TIME)
    }

    private fun bind(event: EventEntity) {
        val dateTime = event.eventStartTime

        eventId = event.id
        editTextTitle.setText(event.title)

        time = LocalTime.of(dateTime.hour, dateTime.minute)
        time_dialog_button.text = time!!.format(ISO_LOCAL_TIME)

        date = LocalDate.of(dateTime.year, dateTime.month, dateTime.dayOfMonth)
        date_dialog_button.text = date!!.format(ISO_LOCAL_DATE)

        editDate.updateDate(dateTime.year, dateTime.monthValue - 1, dateTime.dayOfMonth)
        // API support for deprecated methods in TimePicker
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            editTime.hour = dateTime.hour
            editTime.minute = dateTime.minute
        } else {
            @Suppress("DEPRECATION")
            editTime.currentHour = dateTime.hour
            @Suppress("DEPRECATION")
            editTime.currentMinute = dateTime.minute
        }

        switchRepeat.isChecked = event.repeated
        switchFollowUp.isChecked = event.followUp
        // todo: add in support for tags
        editTextDetails.setText(event.details)
    }

    private fun retrieve(): EventEntity {
        val time = OffsetDateTime.of(
            editDate.year,
            editDate.month + 1,
            editDate.dayOfMonth,
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) editTime.hour else editTime.currentHour,
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) editTime.minute else editTime.currentMinute,
            0,
            0,
            OffsetDateTime.now().offset
        )
        return EventEntity(
            id = eventId,
            title = editTextTitle.text.toString(),
            eventStartTime = time,
            eventDuration = time,
            details = editTextDetails.text.toString(),
            repeated = switchRepeat.isChecked,
            followUp = switchFollowUp.isChecked,
            tags = "" //todo: set up proper tag implementation
        )
    }

    // todo: when go to next event save all entered data, when back put details back
    private fun saveEvent() {

        val title: String = editTextTitle.text.toString()

        // todo: decide on essential fields
        if (title.trim().isEmpty()) {
            Toast.makeText(this, "NO TITLE!", Toast.LENGTH_SHORT).show()
            return
        }

        val eventSave = retrieve()

        // if event currently has no Id, it is a new event.
        if (eventId == null) {
            Log.d("Save clicked", "EVENT INSERTED WITH ID $eventId")
            eventUpdater.insertEvent(eventSave)
            // current implementation uses an intent to pass back the result of saveEvent
            setResult(
                Activity.RESULT_OK, Intent().putExtra(EXTRA_SAVE_STATUS, "SUCCESSFULLY SAVED")
            )
            finish()
        } else {
            eventUpdater.updateEvent(eventSave)
            Log.d("Save clicked", "EVENT UPDATED WITH ID $eventId")
            setResult(
                Activity.RESULT_OK, Intent().putExtra(EXTRA_SAVE_STATUS, "SUCCESSFULLY UPDATED")
            )
            finish()
        }
    }

    /**
     * @return true if the menu should be expanded and false otherwise
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.add_event_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item!!.itemId) {
            R.id.save_event -> {
                Log.i("Save clicked", "SAVE CLICKED IN ADD_EDIT_EVENT")
                saveEvent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
