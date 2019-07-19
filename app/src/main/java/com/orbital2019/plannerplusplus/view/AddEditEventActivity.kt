package com.orbital2019.plannerplusplus.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProviders
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.helper.DateTimeData
import com.orbital2019.plannerplusplus.model.entity.EventEntity
import com.orbital2019.plannerplusplus.viewmodel.EventUpdater
import org.threeten.bp.OffsetDateTime

const val EXTRA_SAVE_STATUS = "com.orbital2019.plannerplusplus.SAVE_STATUS"
const val EXTRA_PARCEL_PLANNEREVENT = "com.orbital2019.plannerplusplus.PARCEL_PLANNEREVENT"

// todo: de-clutter and modularize some of the methods in AddEditEventActivity, particularly the global constants (write bind and retreive methods)
class AddEditEventActivity : AppCompatActivity() {


    private val eventUpdater: EventUpdater by lazy {
        ViewModelProviders.of(this).get(EventUpdater::class.java)
    }

    // Id is null by default
    private var eventId: Int? = null
    private val editTextTitle: EditText by lazy {
        findViewById<EditText>(R.id.edit_text_new_event_title)
    }
    private val editDate: DatePicker by lazy {
        findViewById<DatePicker>(R.id.date_picker_new_event)
    }
    // todo: editDate and editTime as Dialogs
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
        // EventDataState class?

        fun newIntent(context: Context): Intent {
            return Intent(context, AddEditEventActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_event)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)

        // intent has a PlannerEvent Parcel, which means it is an edit event
        if (intent.hasExtra(EXTRA_PARCEL_PLANNEREVENT)) {
            title = "Edit Note"
            val event: EventEntity = intent.getParcelableExtra(EXTRA_PARCEL_PLANNEREVENT)!!
            eventId = event.id
            editTextTitle.setText(event.title)
            val dateTime = DateTimeData(event.eventStartTime!!.toLocalDateTime())
            editDate.updateDate(dateTime.dayOfMonth, dateTime.month, dateTime.year)
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

        } else {
            title = "Add new Note"
        }
    }

    // todo: when go to next event save all entered data, when back put details back
    private fun saveEvent() {

        val title: String = editTextTitle.text.toString()
        val details: String = editTextDetails.text.toString()

        // todo: decide on essential fields
        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please insertEvent a title", Toast.LENGTH_SHORT).show()
            return
        }

        val eventSave = EventEntity(
            title,
            // todo setup this.
//            OffsetDateTime.of(
//                editDate.year,
//                editDate.month,
//                editDate.dayOfMonth,
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) editTime.hour else editTime.currentHour,
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) editTime.minute else editTime.currentMinute
//            ),
            eventStartTime = OffsetDateTime.now(),
            eventDuration = OffsetDateTime.now(),
            details = details,
            repeated = switchRepeat.isChecked,
            followUp = switchFollowUp.isChecked,
            tags = "" //todo: set up proper tag implementation
        )

        // if event currently has no Id, it is a new event.
        if (eventId == null) {
            Log.d("Save clicked", "SAVE_I")
            eventUpdater.insertEvent(eventSave)
            // current implementation uses an intent to pass back the result of saveEvent
            setResult(
                Activity.RESULT_OK, Intent().putExtra(EXTRA_SAVE_STATUS, "SUCCESSFULLY SAVED")
            )
            finish()
        } else {
            eventUpdater.updateEvent(eventSave)
            Log.d("Save clicked", "SAVE_D")
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
