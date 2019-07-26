package com.orbital2019.plannerplusplus.view.ui.displaytasks

import android.view.LayoutInflater
import android.view.ViewGroup
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.constants.SUBTASK_ITEMMODEL
import com.orbital2019.plannerplusplus.view.rendereradapter.ViewRenderer

/**
 * Generates ViewHolder corresponding to the cell type. Generics are used in order to avoid type casts.
 * @param M the type of the cell rendered
 * @param VH the type of the ViewHolder
 */
class SubtaskViewRenderer(private val checkBoxListener: CheckBoxListener) :
    ViewRenderer<SubtaskUiModel, SubtaskViewHolder>() {

    override val type: Int
        get() {
            return SUBTASK_ITEMMODEL
        }

    override fun bindView(model: SubtaskUiModel, holder: SubtaskViewHolder) {
        val view = holder.checkedTextView
        view.text = model.title
        view.isChecked = model.isComplete

        view.setOnClickListener {
            // lambda expression called to override onClick method
            checkBoxListener.onItemClick(model, view.isChecked)
        }
    }

    override fun createViewHolder(parent: ViewGroup): SubtaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SubtaskViewHolder(
            inflater.inflate(R.layout.subitem_task, parent, false)
        )
    }

    interface CheckBoxListener {
        fun onItemClick(model: SubtaskUiModel, isChecked: Boolean)
    }
}
