package com.orbital2019.plannerplusplus.view.ui.displaytasks

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.constants.TASK_ITEMMODEL
import com.orbital2019.plannerplusplus.view.rendereradapter.CompositeViewRenderer
import com.orbital2019.plannerplusplus.view.rendereradapter.RendererRecyclerViewAdapter


class TaskViewRenderer(
    context: Context,
    private val itemClickListener: OnItemClickListener,
    private val checkBoxListener: CheckBoxListener,
    private val childAdapterBinder: ChildAdapterBinder
) :
    CompositeViewRenderer<TaskUiModel, TaskViewHolder>(context) {

    override val type: Int
        get() = TASK_ITEMMODEL

    override fun bindView(model: TaskUiModel, holder: TaskViewHolder) {

        val adapter = holder.mRecyclerView?.adapter as RendererRecyclerViewAdapter

        // Call interface which binds childAdapter to LiveData
        childAdapterBinder.bindChildAdapter(model.id!!, adapter)
        model.items = adapter.mItems

        // When card is clicked, pass the taskEntity in its position in the adapter to the listener
        holder.itemView.setOnClickListener {
            // lambda expression called to override onClick method
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(model)
            }
        }

        // when checkbox is clicked, call the listener on the task in that position
        holder.checkBox.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                checkBoxListener.onItemClick(model, holder.checkBox.isChecked)
            }
        }

        // if number of Subtasks are to be tracked, use a LiveData<Int>, due to nature of Async updates
        holder.textViewTitle.text = model.title
        holder.textViewDescription.text = model.details
        holder.checkBox.isChecked = model.isComplete

    }

    override fun createCompositeViewHolder(parent: ViewGroup): TaskViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return TaskViewHolder(
            inflater.inflate(R.layout.listitem_task, parent, false)
        )
    }

    /**
     * OnItemClickListener provides a interface that represents a listener
     * The listener has a single method which represents a RecyclerView item being clicked
     */
    interface OnItemClickListener {
        fun onItemClick(model: TaskUiModel)
    }

    interface CheckBoxListener {
        fun onItemClick(model: TaskUiModel, isChecked: Boolean)
    }

    interface ChildAdapterBinder {
        // link LiveData to mItems in adapter
        fun bindChildAdapter(id: Long, adapter: RendererRecyclerViewAdapter)
    }
}