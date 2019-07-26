package com.orbital2019.plannerplusplus.view.ui.displaytasks

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.constants.TASK_ITEMMODEL
import com.orbital2019.plannerplusplus.view.rendereradapter.CompositeViewRenderer
import com.orbital2019.plannerplusplus.view.rendereradapter.ItemModel
import com.orbital2019.plannerplusplus.view.rendereradapter.RendererRecyclerViewAdapter


class TaskViewRenderer(
    context: Context,
    private val itemClickListener: OnItemClickListener,
    private val checkBoxListener: CheckBoxListener
) :
    CompositeViewRenderer<TaskUiModel, TaskViewHolder>(context) {

    override val type: Int
        get() = TASK_ITEMMODEL

    override fun bindView(model: TaskUiModel, holder: TaskViewHolder) {
        holder.textViewTitle.text = String.format(model.title + "[%d]", model.subtasks.size)
        holder.textViewDescription.text = model.details
        holder.checkBox.isChecked = model.isComplete

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

        model.subtaskListener = object : SubtaskListener {
            override fun updateSubtask(models: ArrayList<ItemModel>) {
                val adapter = holder.mRecyclerView?.adapter as RendererRecyclerViewAdapter
                adapter.mItems.clear()
                adapter.mItems.addAll(models)
                adapter.notifyDataSetChanged()

            }
        }

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

    interface SubtaskListener {
        fun updateSubtask(models: ArrayList<ItemModel>)
    }
}