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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.model.PlannerEvent
import org.threeten.bp.LocalDateTime

class AddNewEventActivity : AppCompatActivity() {

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
    // todo: add in tags
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
    fun saveEvent() {

        val title: String = editTextTitle.text.toString()
        val details: String = editTextDetails.text.toString()

        if (title.trim().isEmpty() || details.trim().isEmpty()) {
            var eventSave = PlannerEvent(
                title,
                LocalDateTime.now(),
                details,
                switchRepeat.isChecked,
                switchFollowUp.isChecked,
                mutableListOf()
            )
            // todo: make a viewmodel for save and one for read using ViewModel.insert
            // current implementation uses an intent to pass back the eventSave
            setResult(Activity.RESULT_OK, Intent().putExtra("status", "SUCCESSFULLY SAVED"))
            finish()
        }

        /* Intent data = new Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)*/

        // setResult(RESULT_OK, data)
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
