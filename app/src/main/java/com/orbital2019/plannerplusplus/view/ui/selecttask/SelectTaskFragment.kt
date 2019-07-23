package com.orbital2019.plannerplusplus.view.ui.selecttask

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.model.entity.TaskEntity
import com.orbital2019.plannerplusplus.view.SelTaskAdapter
import com.orbital2019.plannerplusplus.viewmodel.TaskViewModel


class SelectTaskFragment : DialogFragment() {

    companion object {
        fun newInstance() = SelectTaskFragment()
    }

    private lateinit var viewModel: TaskViewModel
    private lateinit var selectTasksRecycler: RecyclerView

    // Container Activity must implement this interface
    var taskSelectedListener: TaskSelectedListener? = null

    // todo read up on this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, theme)
    }

    /**
     * Ensure that the host activity implements TaskSelectedListener interface,
     * instantiate an instance of OnArticleSelectedListener by casting the Activity that is passed into onAttach().
     * If the activity hasn't implemented the interface, then the fragment throws a ClassCastException.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        taskSelectedListener = context as? TaskSelectedListener
        if (taskSelectedListener == null) {
            throw ClassCastException("$context must implement TaskSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Toast.makeText(context, "Select Task Fragment Created", Toast.LENGTH_SHORT).show()
        // set layout associated with this class
        val layout: View =
            inflater.inflate(com.orbital2019.plannerplusplus.R.layout.select_task_fragment, container, false)

        // bind RecyclerViews to variables
        selectTasksRecycler = layout.findViewById(com.orbital2019.plannerplusplus.R.id.select_task_recycler_view)
        // LinearLayoutManager ensures that items are displayed linearly
        selectTasksRecycler.layoutManager = LinearLayoutManager(activity)

        // If tasksRecyclerView will never change in size, set this to optimize
        selectTasksRecycler.setHasFixedSize(true)

        return layout
    }

    /**
     * onActivityCreated is called after both the activity and the view for this fragment is initialized.
     * In this method, we:
     *  1. Get the list of tasks from the viewModel
     *  2. Assign the list of tasks to the Adapter via a LiveData observer, and
     *  3. Bind the Adapter to its RecyclerView.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(TaskViewModel::class.java)
        val selTasksAdapter = SelTaskAdapter(selectTasksRecycler)
        selectTasksRecycler.adapter = selTasksAdapter

        // LiveData can only be unwrapped by an observer
        viewModel.getAllTasks().observe(
            viewLifecycleOwner,
            Observer<List<TaskEntity>> {
                // overriding onChanged for LiveData<List<TaskEntity>>> Observer
                selTasksAdapter.tasks = it as ArrayList<TaskEntity>
            })


        // Adapters' listeners are instantiated here:
        selTasksAdapter.itemClickListener = object : SelTaskAdapter.OnItemClickListener {
            /**
             * When a task is clicked, it should select the task and pass it to the corresponding activity
             * (CopyExistingActivity or todo("LinkTaskToActivity") )
             */
            override fun onItemClick(task: TaskEntity) {
                // calls corresponding method on Activity that passes the corresponding task item
                taskSelectedListener!!.onTaskSelected(task)
                dismiss()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    /**
     * Defining a callback interface inside SelectTaskFragment.
     * Any activity creating a SelectTaskFragment has to implement this interface.
     */
    interface TaskSelectedListener {

        /**
         * When onTaskSelected is called, close the calling fragment and return the correct Fragment
         */
        fun onTaskSelected(task: TaskEntity)

        fun getSelectedTask(): TaskEntity
    }

}
