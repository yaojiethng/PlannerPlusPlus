package com.orbital2019.plannerplusplus.view.ui

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
import com.orbital2019.plannerplusplus.constants.EDIT_EVENT_REQUEST
import com.orbital2019.plannerplusplus.model.entity.TaskEntity
import com.orbital2019.plannerplusplus.view.rendereradapter.ItemModel
import com.orbital2019.plannerplusplus.view.rendereradapter.RendererRecyclerViewAdapter
import com.orbital2019.plannerplusplus.view.rendereradapter.ViewRenderer
import com.orbital2019.plannerplusplus.view.ui.displaytasks.TaskUiModel
import com.orbital2019.plannerplusplus.view.ui.displaytasks.TaskViewRenderer
import com.orbital2019.plannerplusplus.viewmodel.TaskViewModel
import java.util.*

/**
 * todo: link fab and options menu to fragment?
 * TasksFragment is the fragment opened in MainActivity, containing one RecyclerView listing all the current created
 * Tasks. Contains a FAB menu, SearchBar, and all onClickListeners
 */
class TasksFragment : Fragment() {

    /**
     * ViewModel interfacing between Model and View for all Task methods
     */
    private val tasksViewModel: TaskViewModel by lazy {
        ViewModelProviders.of(this).get(TaskViewModel::class.java)
    }
    /**
     * Adapter which updates the sole RecyclerView in TasksFragment
     */
    private lateinit var adapter: RendererRecyclerViewAdapter
    /**
     * View object representing the sole RecyclerView
     */
    private lateinit var recyclerView: RecyclerView

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

    /**
     * onCreateView is called when the view for this fragment is created.
     * In this method, we bind the layout to the view of this fragment, and bind the Adapter to its RecyclerView.
     * LiveData gets assigned a observer which updates the adapter when LiveData is changed.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // set layout associated with this class
        val layout: View = inflater.inflate(R.layout.fragment_tasks, container, false)

        // bind RecyclerViews to variables
        recyclerView = layout.findViewById(R.id.tasks_recycler_view)
        // LinearLayoutManager ensures that items are displayed linearly
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // If recyclerView will never change in size, set this to optimize
        recyclerView.setHasFixedSize(true)

        adapter = RendererRecyclerViewAdapter()
        recyclerView.adapter = adapter

        val taskRenderer = TaskViewRenderer(
            activity!!,
            object : TaskViewRenderer.OnItemClickListener {
                override fun onItemClick(model: TaskUiModel) {
                    // AddEditTaskActivity::class.java is not used, but it is passed back when ActivityForResult terminates
                    val intent = Intent(activity, AddEditTaskActivity::class.java)
                    intent.putExtra(EXTRA_PARCEL_PLANNERTASK, model.task!!)
                    startActivityForResult(intent, EDIT_EVENT_REQUEST)
                }
            },
            object : TaskViewRenderer.CheckBoxListener {
                override fun onItemClick(model: TaskUiModel, isChecked: Boolean) {
                    if (isChecked) {
                        tasksViewModel.setTaskComplete(model.task!!)
                    } else {
                        tasksViewModel.setTaskIncomplete(model.task!!)
                    }
                }
            }
        )

        // Register Renderers to Adapter
        adapter.registerRenderer(taskRenderer as ViewRenderer<ItemModel, RecyclerView.ViewHolder>)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Links tasksViewModel to the view of TasksFragment, which means:
        // this ViewModel will only updateTask when this Fragment is in the foreground.
        // viewLifeCycleOwner is the owner, which closes the observer when the view is destroyed.
        tasksViewModel.getAllTasks().observe(
            viewLifecycleOwner,
            Observer<List<TaskEntity>> { task ->
                // overriding onChanged for LiveData<List<TaskEntity>>> Observer
                Log.d(
                    "OBSERVER_comTASKS",
                    "Change on CompleteTasks with " + adapter.mItems.size + "Tasks"
                )
                // changes adapter.tasks and calls set method
                adapter.mItems = task.map {
                    TaskUiModel(it) as ItemModel
                } as ArrayList<ItemModel>
                adapter.notifyDataSetChanged()
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
                tasksViewModel.delete(adapter.mItems[viewHolder.adapterPosition])
                // call activity to get the activity attribute
                Toast.makeText(activity, "Task Deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerView)
    }
}
