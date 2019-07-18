/**
 * Adapters provide a binding from an app-specific data set to views that are displayed within a RecyclerView.
 * TaskAdapter, specifically, binds data from TaskEntities (passed by TaskViewModel) to the RecyclerView containing
 * the list of Tasks in TasksFragment
 */
package com.orbital2019.plannerplusplus.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.model.TaskEntity

class TaskAdapter(var recycler: RecyclerView) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // to prevent any null checks, init the list first
    internal var tasks = ArrayList<TaskEntity>()
        internal set(tasks) {
            field = tasks
            notifyDataSetChanged()
        }
    internal lateinit var itemClickListener: OnItemClickListener
    internal lateinit var checkBoxListener: CheckBoxListener

    fun taskAt(position: Int): TaskEntity {
        return tasks[position]
    }

    /** @param parent: the ViewGroup that is passed, which is the RecyclerView
     * @return TaskViewHolder: decides the layout for the different items in the RecyclerView
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView)
    }

    /**
     * @ return: the number of items that will be displayed in the RecyclerView
     */
    override fun getItemCount(): Int {
        return tasks.size
    }

    /** inserts data from Task objects into the view of the TaskViewHolder
     * @Param holder: the holder object containing the different component views of the item
     * @Param position: the index of the current item being bound
     **/
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        val currentTaskEntity: TaskEntity = tasks[position]
        holder.bind(currentTaskEntity)
    }

    /**
     * TaskViewHolder is an extension of ViewHolder, which holds and recycles the element items in RecyclerView.
     * TaskViewHolder manages and assigns individual view components contained in task_item.xml
     * Click logic is handled when instantiating the ViewHolder which allows for more explicit control
     * (Click listeners are explicitly set up in the init phase)
     * @param itemView: element item of TaskViewHolder (in this case task_item)
     */
    // todo: updateTask with new variables once critical parameters are decided
    inner class TaskViewHolder(itemView: View) : BaseViewHolder<TaskEntity>(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.text_view_task_title)
        var textViewDescription: TextView = itemView.findViewById(R.id.text_view_task_details)
        var checkBox: CheckBox = itemView.findViewById(R.id.checkbox_task)

        init {
            // When card is clicked, pass the taskEntity in its position in the adapter to the listener
            itemView.setOnClickListener {
                // lambda expression called to override onClick method
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(tasks[position])
                }
            }

            // when checkbox is clicked, call the listener on the task in that position
            checkBox.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    checkBoxListener.onItemClick(tasks[position], checkBox.isChecked)
                }
            }
        }

        override fun bind(item: TaskEntity) {
            textViewTitle.text = item.title
            textViewDescription.text = item.details
            checkBox.isChecked = item.complete
        }
    }

    /**
     * OnItemClickListener provides a interface that represents a listener
     * The listener has a single method which represents a RecyclerView item being clicked
     */
    interface OnItemClickListener {
        fun onItemClick(task: TaskEntity)
    }

    interface CheckBoxListener {
        fun onItemClick(task: TaskEntity, isChecked: Boolean)
    }

}
