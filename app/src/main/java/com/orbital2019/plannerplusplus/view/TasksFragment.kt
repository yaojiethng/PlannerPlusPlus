package com.orbital2019.plannerplusplus.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.model.TaskEntity
import com.orbital2019.plannerplusplus.viewmodel.TaskViewModel


// todo: link fab and options menu to fragment?
class TasksFragment : Fragment() {

    private val tasksViewModel: TaskViewModel by lazy {
        ViewModelProviders.of(this).get(TaskViewModel::class.java)
    }
    private lateinit var tasksRecyclerView: RecyclerView

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all -> {
                tasksViewModel.deleteAllTasks()
                Toast.makeText(activity, "All Notes Deleted", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // set layout associated with this class
        val layout: View = inflater.inflate(R.layout.fragment_tasks, container, false)

        // bind RecyclerViews to variables
        tasksRecyclerView = layout.findViewById(R.id.tasks_recycler_view)
        // LinearLayoutManager ensures that items are displayed linearly
        tasksRecyclerView.layoutManager = LinearLayoutManager(activity)

        // If tasksRecyclerView will never change in size, set this to optimize
        tasksRecyclerView.setHasFixedSize(true)

        val tasksAdapter = TaskAdapter(tasksRecyclerView)
        tasksRecyclerView.adapter = tasksAdapter

        // links this viewModel to this fragment, which means:
        //  this ViewModel will only updateTask when this Fragment is in the foreground, and
        //  when this Fragment is closed, so will the ViewModel.
        tasksViewModel.getAllTasks().observe(
            this,
            Observer<List<TaskEntity>> {
                // overriding onChanged for LiveData<List<TaskEntity>>> Observer
                Log.d(
                    "OBSERVER_comTASKS",
                    "Change on CompleteTasks with " + tasksAdapter.tasks.size + "Tasks"
                )
                // changes tasksAdapter.tasks and calls set method
                tasksAdapter.tasks = it as ArrayList<TaskEntity>
            })

        // ItemTouchHelper makes RecyclerView swipe-able
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // When item is swiped, deleteTask item from list.
            // todo: add features such as different directions, SnackBar to undo
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // position in adapter where the item is swiped
                tasksViewModel.deleteTask(tasksAdapter.taskAt(viewHolder.adapterPosition))
                // call activity to get the activity attribute
                Toast.makeText(activity, "Task Deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(tasksRecyclerView)

        // Adapters' listeners are instantiated here:
        tasksAdapter.itemClickListener = object : TaskAdapter.OnItemClickListener {
            override fun onItemClick(task: TaskEntity) {
                // AddEditTaskActivity::class.java is not used, but it is passed back when ActivityForResult terminates
                val intent = Intent(activity, AddEditTaskActivity::class.java)
                intent.putExtra(EXTRA_PARCEL_PLANNERTASK, task)
                startActivityForResult(intent, EDIT_EVENT_REQUEST)
            }
        }
        tasksAdapter.checkBoxListener = object : TaskAdapter.CheckBoxListener {
            override fun onItemClick(task: TaskEntity, isChecked: Boolean) {
                if (isChecked) {
                    tasksViewModel.setTaskComplete(task)
                } else {
                    tasksViewModel.setTaskIncomplete(task)
                }
            }
        }

        return layout
    }
}
