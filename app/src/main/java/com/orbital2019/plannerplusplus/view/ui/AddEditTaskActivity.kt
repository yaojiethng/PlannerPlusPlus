package com.orbital2019.plannerplusplus.view.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.model.entity.SubtaskEntity
import com.orbital2019.plannerplusplus.model.entity.TaskAndSubtask
import com.orbital2019.plannerplusplus.model.entity.TaskEntity
import com.orbital2019.plannerplusplus.view.rendereradapter.ItemModel
import com.orbital2019.plannerplusplus.view.rendereradapter.RendererRecyclerViewAdapter
import com.orbital2019.plannerplusplus.view.rendereradapter.ViewRenderer
import com.orbital2019.plannerplusplus.view.ui.displaytasks.SubtaskUiModel
import com.orbital2019.plannerplusplus.view.ui.selecttask.LinkTaskViewRenderer
import com.orbital2019.plannerplusplus.viewmodel.TaskViewModel


const val EXTRA_PARCEL_PLANNERTASK = "com.orbital2019.plannerplusplus.PARCEL_PLANNERTASK"

class AddEditTaskActivity : AppCompatActivity() {


    private val viewModel: TaskViewModel by lazy {
        ViewModelProviders.of(this).get(TaskViewModel::class.java)
    }

    // Id is null by default
    private var taskId: Long? = null
    private val editTextTitle: EditText by lazy {
        findViewById<EditText>(R.id.edit_text_new_task_title)
    }
    private val switchNumberTasks: SwitchCompat by lazy {
        findViewById<SwitchCompat>(R.id.switch_number_tasks)
    }
    // todo: add in tags as a fragment
    private val editTextDetails: EditText by lazy {
        findViewById<EditText>(R.id.edit_text_details)
    }
    private var isComplete: Boolean? = null
    private val addTaskButton: Button by lazy {
        findViewById<Button>(R.id.add_task_button)
    }
    private lateinit var adapter: RendererRecyclerViewAdapter

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AddEditTaskActivity::class.java)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)

        // Initializing recyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.link_task_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RendererRecyclerViewAdapter()
        recyclerView.adapter = adapter
        findViewById<TextView>(R.id.add_linked_task_title).text = "Add Sub-task"

        val subtaskRenderer = LinkTaskViewRenderer(
            this,
            object : LinkTaskViewRenderer.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    adapter.mItems.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
            }
        )
        adapter.registerRenderer(subtaskRenderer as ViewRenderer<ItemModel, RecyclerView.ViewHolder>)

        // Adding onClickListener for addTaskButton
        addTaskButton.setOnClickListener {
            val input = EditText(this)
            AlertDialog.Builder(this)
                .setTitle("Add a Subtask")
                .setView(input)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ ->
                    adapter.mItems.add(SubtaskUiModel(null, input.text.toString(), false, null))
                    Toast.makeText(
                        this,
                        "Create SUBTASK",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton(android.R.string.no) { dialog, _ ->
                    dialog.cancel()
                }.show()
            adapter.notifyDataSetChanged()
        }

        // if intent contains a PlannerTask Parcel, it is an edit task
        if (intent.hasExtra(EXTRA_PARCEL_PLANNERTASK)) {
            title = "Edit Task"
            val task: TaskEntity = intent.getParcelableExtra(EXTRA_PARCEL_PLANNERTASK)!!
            bind(task)

        } else {
            title = "Add new Task"
        }
    }

    private fun bind(task: TaskEntity) {
        viewModel.getSubTasksById(task.id!!, object : TaskViewModel.SubtaskResultListener {
            override fun accept(parentId: Long, result: LiveData<List<SubtaskEntity>>) {
                result.observe(
                    this@AddEditTaskActivity,
                    Observer<List<SubtaskEntity>> { subtasks ->
                        Log.d("SUBTASK_COUNT", "THIS TASK HAS " + subtasks.size + " SUBTASKS")
                        adapter.mItems.clear()
                        adapter.mItems.addAll(subtasks.map {
                            return@map SubtaskUiModel(it)
                        })
                        adapter.notifyDataSetChanged()
                    }
                )
            }
        })
        taskId = task.id
        editTextTitle.setText(task.title)
        editTextDetails.setText(task.details)
        switchNumberTasks.isChecked = task.autoNumber
        isComplete = task.isComplete
    }

    private fun retrieve(): TaskAndSubtask {

        val subtasks: Array<SubtaskUiModel> = adapter.getItems()
            .mapNotNull {
                if (it is SubtaskUiModel) {
                    it
                } else {
                    null
                }
            }.toTypedArray()

        return TaskAndSubtask(
            TaskEntity(
                taskId,
                editTextTitle.text.toString(),
                editTextDetails.text.toString(),
                switchNumberTasks.isChecked,
                tags = "", // todo: set up proper tag interaction
                isComplete = isComplete ?: false
            ),
            *subtasks
        )
    }

    private fun saveTask() {

        // check essential fields filled
        if (editTextTitle.text.isEmpty()) {
            toast("Please insert a title")
            return
        }

        val taskSave = retrieve()

        // if task currently has no Id, it is a new task.
        if (taskId == null) {
            viewModel.insertTaskAndSubtask(taskSave)
            Log.d("Save clicked", "EVENT INSERTED WITH ID $taskId")
            // current implementation uses an intent to pass back the result of saveTask
            setResult(
                Activity.RESULT_OK, Intent().putExtra(EXTRA_SAVE_STATUS, "SUCCESSFULLY SAVED")
            )
            finish()
        } else {
            viewModel.updateTaskandSubtask(taskSave)
            Log.d("Save clicked", "EVENT INSERTED WITH ID $taskId")
            setResult(
                Activity.RESULT_OK, Intent().putExtra(EXTRA_SAVE_STATUS, "SUCCESSFULLY UPDATED")
            )
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.orbital2019.plannerplusplus.R.menu.add_task_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item!!.itemId) {
            com.orbital2019.plannerplusplus.R.id.save_task -> {
                saveTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun toast(string: String) {
        return Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }
}
