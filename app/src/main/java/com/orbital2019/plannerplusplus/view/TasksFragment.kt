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
import com.orbital2019.plannerplusplus.viewmodel.PlannerTask
import com.orbital2019.plannerplusplus.viewmodel.TaskViewModel


// todo: link fab and options menu to fragment?
class TasksFragment : Fragment() {

    private val tasksViewModel: TaskViewModel by lazy {
        ViewModelProviders.of(this).get(TaskViewModel::class.java)
    }
    private lateinit var incompleteTasksRecyclerView: RecyclerView
    private lateinit var completedTasksRecyclerView: RecyclerView

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
        incompleteTasksRecyclerView = layout.findViewById(R.id.incomplete_tasks_recycler_view)
        completedTasksRecyclerView = layout.findViewById(R.id.completed_tasks_recycler_view)

        // LinearLayoutManager ensures that items are displayed linearly
        incompleteTasksRecyclerView.layoutManager = LinearLayoutManager(activity)
        completedTasksRecyclerView.layoutManager = LinearLayoutManager(activity)

        // If incompleteTasksRecyclerView will never change in size, set this to optimize
        // incompleteTasksRecyclerView.setHasFixedSize(true)

        val incompleteTasksAdapter = TaskAdapter(incompleteTasksRecyclerView)
        incompleteTasksRecyclerView.adapter = incompleteTasksAdapter

        val completedTasksAdapter = TaskAdapter(completedTasksRecyclerView)
        completedTasksRecyclerView.adapter = completedTasksAdapter

        // links this viewModel to this fragment, which means:
        //  this ViewModel will only updateTask when this Fragment is in the foreground, and
        //  when this Fragment is closed, so will the ViewModel.
        tasksViewModel.getIncompleteTasks().observe(
            this,
            Observer<List<TaskEntity>> {
                Log.d("OBSERVER_incomTASKS", "Change on IncompleteTasks")
                incompleteTasksAdapter.tasks = it as ArrayList<TaskEntity>
            })
        tasksViewModel.getCompletedTasks().observe(
            this,
            Observer<List<TaskEntity>> {
                Log.d("OBSERVER_comTASKS", "TaskViewModel onChanged")
                completedTasksAdapter.tasks = it as ArrayList<TaskEntity>
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
            // todo check if this needs to be changed for other adapter
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // position in adapter where the item is swiped
                tasksViewModel.deleteTask(incompleteTasksAdapter.taskAt(viewHolder.adapterPosition))
                // call activity to get the activity attribute
                Toast.makeText(activity, "Task Deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(incompleteTasksRecyclerView)

        // Adapters' listeners are instantiated here:
        incompleteTasksAdapter.itemClickListener = object : TaskAdapter.OnItemClickListener {
            override fun onItemClick(task: TaskEntity) {
                // AddEditTaskActivity::class.java is not used, but it is passed back when ActivityForResult terminates
                val intent = Intent(activity, AddEditTaskActivity::class.java)
                intent.putExtra(EXTRA_PARCEL_PLANNERTASK, PlannerTask.createFromEntity(task))
                startActivityForResult(intent, EDIT_EVENT_REQUEST)
            }
        }
        incompleteTasksAdapter.checkBoxListener = object : TaskAdapter.CheckBoxListener {
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
