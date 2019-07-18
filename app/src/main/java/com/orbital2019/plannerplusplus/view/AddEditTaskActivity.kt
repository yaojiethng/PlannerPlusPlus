package com.orbital2019.plannerplusplus.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProviders
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.model.TaskEntity
import com.orbital2019.plannerplusplus.viewmodel.TaskUpdater

const val EXTRA_PARCEL_PLANNERTASK = "com.orbital2019.plannerplusplus.PARCEL_PLANNERTASK"

// todo: de-clutter and modularize some of the methods in AddEditTaskActivity, particularly the global constants
class AddEditTaskActivity : AppCompatActivity() {


    private val taskUpdater: TaskUpdater by lazy {
        ViewModelProviders.of(this).get(TaskUpdater::class.java)
    }

    // Id is null by default
    private var taskId: Int? = null
    private val editTextTitle: EditText by lazy {
        findViewById<EditText>(R.id.edit_text_new_task_title)
    }
    private val switchNumberTasks: SwitchCompat by lazy {
        findViewById<SwitchCompat>(R.id.switch_number_tasks)
    }
    // todo: add in tags, SubTask as a fragment
    private val editTextDetails: EditText by lazy {
        findViewById<EditText>(R.id.edit_text_details)
    }

    companion object {
        // TaskDataState class?

        fun newIntent(context: Context): Intent {
            return Intent(context, AddEditTaskActivity::class.java)
        }
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)

        // intent has a PlannerTask Parcel, which means it is an edit task
        if (intent.hasExtra(EXTRA_PARCEL_PLANNERTASK)) {
            title = "Edit Task"
            val task: TaskEntity = intent.getParcelableExtra(EXTRA_PARCEL_PLANNERTASK)!!
            taskId = task.id
            editTextTitle.setText(task.title)
            switchNumberTasks.isChecked = task.autoNumber
            // todo: add in support for tags

        } else {
            title = "Add new Task"
        }
    }

    // todo: when go to next task save all entered data, when back put details back
    private fun saveTask() {

        val title: String = editTextTitle.text.toString()
        val details: String = editTextDetails.text.toString()

        // todo: decide on essential fields
        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please insertEvent a title", Toast.LENGTH_SHORT).show()
            return
        }

        val taskSave = TaskEntity(
            title,
            details,
            switchNumberTasks.isChecked,
            tags = "", // todo: set up proper tag interaction
            complete = false
        )

        // if task currently has no Id, it is a new task.
        if (taskId == null) {
            taskUpdater.insertTask(taskSave)
            // current implementation uses an intent to pass back the result of saveTask
            setResult(
                Activity.RESULT_OK, Intent().putExtra(EXTRA_SAVE_STATUS, "SUCCESSFULLY SAVED")
            )
            finish()
        } else {
            taskUpdater.updateTask(taskSave)
            setResult(
                Activity.RESULT_OK, Intent().putExtra(EXTRA_SAVE_STATUS, "SUCCESSFULLY UPDATED")
            )
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.add_task_menu, menu)
        // true shows the menu and false hides it
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item!!.itemId) {
            R.id.save_task -> {
                Log.i("Save clicked", "SAVE CLICKED IN ADD_EDIT_TASK")
                saveTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
