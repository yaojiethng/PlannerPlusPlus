package com.orbital2019.plannerplusplus.view.ui.selecttask

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.constants.SUBTASK_ITEMMODEL
import com.orbital2019.plannerplusplus.view.rendereradapter.ViewRenderer
import com.orbital2019.plannerplusplus.view.ui.displaytasks.SubtaskUiModel

/**
 * Used when linking Subtask in AddEditTaskActivity
 * or when linking Task to Event in EventsFragment
 */
class LinkTaskViewRenderer(
    val context: Context,
    private val listener: OnItemClickListener
) :
    ViewRenderer<SubtaskUiModel, LinkTaskViewHolder>() {
    override val type: Int
        get() = SUBTASK_ITEMMODEL

    override fun bindView(model: SubtaskUiModel, holder: LinkTaskViewHolder) {
        holder.textViewTitle.text = model.title
        holder.deleteButton.setOnClickListener {
            listener.onItemClick(holder.adapterPosition)
        }
    }

    override fun createViewHolder(parent: ViewGroup): LinkTaskViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return LinkTaskViewHolder(
            inflater.inflate(R.layout.listitem_link_task, parent, false)
        )
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}