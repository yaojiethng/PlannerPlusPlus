package com.orbital2019.plannerplusplus.view.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
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
import com.orbital2019.plannerplusplus.model.entity.TaskEntity
import com.orbital2019.plannerplusplus.view.ui.selectdate.DatePickerFragment
import com.orbital2019.plannerplusplus.view.ui.selecttask.SelectTaskFragment
import com.orbital2019.plannerplusplus.view.ui.selecttime.LinkTaskAdapter
import com.orbital2019.plannerplusplus.view.ui.selecttime.TimePickerFragment
import com.orbital2019.plannerplusplus.viewmodel.EventUpdater
import kotlinx.android.synthetic.main.activity_add_edit_event.*
import kotlinx.android.synthetic.main.widget_add_linked_task.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE
import org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_TIME

const val EXTRA_SAVE_STATUS = "com.orbital2019.plannerplusplus.SAVE_STATUS"
const val EXTRA_PARCEL_PLANNEREVENT = "com.orbital2019.plannerplusplus.PARCEL_PLANNEREVENT"

class AddEditEventActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {


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
    private val addTaskButton: Button by lazy {
        findViewById<Button>(R.id.add_task_button)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AddEditEventActivity::class.java)
        }
    }

    /**
     * Called when the activity is first created.
     * This is where you should do all of your normal static set up: create views, bind data to lists, etc.
     * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
     */
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_event)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)

        // bind Buttons to OnClickListeners (which open DialogFragments)
        date_dialog_button.setOnClickListener {
            val datePicker: DialogFragment = DatePickerFragment(date)
            datePicker.show(supportFragmentManager, "Date_Picker")
        }
        time_dialog_button.setOnClickListener {
            val timePicker: DialogFragment = TimePickerFragment(time)
            timePicker.show(supportFragmentManager, "Time_Picker")
        }

        // setting up widget
        val listAdapter = LinkTaskAdapter(this)
        list.adapter = listAdapter

        addTaskButton.setOnClickListener {
            // when addTaskButton is clicked, open Select Task dialog
            SelectTaskFragment(object : SelectTaskFragment.TaskSelectedListener {
                override fun onTaskSelected(task: TaskEntity) {
                    listAdapter.add(task)
                }
            }).show(supportFragmentManager, "select_task")
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

        findViewById<TextView>(R.id.add_linked_task_title).text = "Add Required Task"
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        time = LocalTime.of(hourOfDay, minute)
        editTimeButton.text = time!!.format(ISO_LOCAL_TIME)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        date = LocalDate.of(year, month + 1, dayOfMonth)
        editDateButton.text = date!!.format(ISO_LOCAL_DATE)
    }

    private fun bind(event: EventEntity) {
        val dateTime = event.eventStartTime

        eventId = event.id
        editTextTitle.setText(event.title)

        time = LocalTime.of(dateTime.hour, dateTime.minute)
        time_dialog_button.text = time!!.format(ISO_LOCAL_TIME)

        date = LocalDate.of(dateTime.year, dateTime.month, dateTime.dayOfMonth)
        date_dialog_button.text = date!!.format(ISO_LOCAL_DATE)

        switchRepeat.isChecked = event.repeated
        switchFollowUp.isChecked = event.followUp
        // todo: add in support for tags
        editTextDetails.setText(event.details)
    }

    private fun retrieve(): EventEntity {
        val startDateTime = OffsetDateTime.of(
            date ?: LocalDate.now(),
            time ?: LocalTime.now(),
            OffsetDateTime.now().offset
        )
        return EventEntity(
            id = eventId,
            title = editTextTitle.text.toString(),
            eventStartTime = startDateTime,
            eventDuration = startDateTime,
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
