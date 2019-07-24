package com.orbital2019.plannerplusplus.view.ui.selecttask

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
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.model.entity.TaskEntity
import com.orbital2019.plannerplusplus.view.SelTaskAdapter
import com.orbital2019.plannerplusplus.viewmodel.TaskViewModel

/**
 * SelectTaskFragment represents the creation of a dialog pop-up whenever a Task needs to be selected.
 * @param listener the implementation of the TaskSelectedListener interface which runs onTaskSelected whenever a Task is selected
 */
class SelectTaskFragment(var listener: TaskSelectedListener) : DialogFragment() {

    companion object {
        fun newInstance(listener: TaskSelectedListener) = SelectTaskFragment(listener)
    }

    private lateinit var viewModel: TaskViewModel
    private lateinit var selectTasksRecycler: RecyclerView

    // Container Activity must implement this interface
    // var listener: TaskSelectedListener? = null

    // todo https://guides.codepath.com/android/using-dialogfragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, theme)
        // todo set toolBar with filter https://stackoverflow.com/questions/11425020/actionbar-in-a-dialogfragment/38917527#38917527
    }

    /**
     * Ensure that the host activity implements TaskSelectedListener interface,
     * instantiate an instance of OnArticleSelectedListener by casting the Activity that is passed into onAttach().
     * If the activity hasn't implemented the interface, then the fragment throws a ClassCastException.
     */
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        listener = taskSelectedListener as? TaskSelectedListener
//        if (listener == null) {
//            throw ClassCastException("$context must implement TaskSelectedListener")
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Toast.makeText(context, "Select Task Fragment Created", Toast.LENGTH_SHORT).show()
        // set layout associated with this class
        val layout: View =
            inflater.inflate(R.layout.fragment_select_task, container, false)

        // bind RecyclerViews to variables
        selectTasksRecycler = layout.findViewById(R.id.select_task_recycler_view)
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

        viewModel.getAllTasks().observe(
            viewLifecycleOwner,
            Observer<List<TaskEntity>> {
                // overriding onChanged for LiveData<List<TaskEntity>>> Observer
                selTasksAdapter.tasks = it as ArrayList<TaskEntity>
            })


        // Adapters' listeners are instantiated here:
        selTasksAdapter.itemClickListener = object : SelTaskAdapter.OnItemClickListener {
            /**
             * When a task is clicked, calls corresponding method from TaskSelectedListener interface
             * and passes the selected task item
             */
            override fun onItemClick(task: TaskEntity) {
                //
                listener.onTaskSelected(task)
                // dismisses the fragment after task is selected
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
     * Any activity creating a SelectTaskFragment has to provide this listener.
     */
    interface TaskSelectedListener {

        /**
         * When onTaskSelected is called, close the calling fragment and return the correct Fragment
         */
        fun onTaskSelected(task: TaskEntity)

    }

}
