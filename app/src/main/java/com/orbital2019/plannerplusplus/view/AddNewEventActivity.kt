package com.orbital2019.plannerplusplus.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.orbital2019.plannerplusplus.model.PlannerEvent
import com.orbital2019.plannerplusplus.viewmodel.EventUpdater
import org.threeten.bp.LocalDateTime

const val EXTRA_SAVE_STATUS = "com.orbital2019.plannerplusplus.SAVE_STATUS"

// todo: de-clutter and modularize some of the methods in AddNewEventActivity, particularly the global constants
class AddNewEventActivity : AppCompatActivity() {


    private val eventUpdater: EventUpdater by lazy {
        ViewModelProviders.of(this).get(EventUpdater::class.java)
    }

    private val editTextTitle: EditText by lazy {
        findViewById<EditText>(R.id.edit_text_new_event_title)
    }
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
        // EventDataState class?

        fun newIntent(context: Context): Intent {
            return Intent(context, AddNewEventActivity::class.java)
        }
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_event)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)
        title = "Add new Note"
    }

    // todo: when go to next event save all entered data, when back put details back
    private fun saveEvent() {

        val title: String = editTextTitle.text.toString()
        val details: String = editTextDetails.text.toString()

        // todo: decide on essential fields
        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title", Toast.LENGTH_SHORT).show()
            return
        }

        val eventSave = PlannerEvent(
            title,
            LocalDateTime.now(),
            details,
            switchRepeat.isChecked,
            switchFollowUp.isChecked,
            mutableListOf()
        )

        eventUpdater.insertEvent(eventSave)
        // current implementation uses an intent to pass back the result of saveEvent
        setResult(
            Activity.RESULT_OK, Intent().putExtra(EXTRA_SAVE_STATUS, "SUCCESSFULLY SAVED")
        )
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.add_event_menu, menu)
        // true shows the menu and false hides it
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item!!.itemId) {
            R.id.save_event -> {
                saveEvent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
