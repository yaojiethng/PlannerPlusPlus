package com.orbital2019.plannerplusplus.view.ui.displaytasks

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.view.rendereradapter.CompositeViewHolder


class TaskViewHolder(view: View) : CompositeViewHolder(view) {
    init {
        // binds child recyclerView
        mRecyclerView = view.findViewById(R.id.subtasks_recycler_view)
    }

    val textViewTitle: TextView = view.findViewById(R.id.checked_view_task_title)
    val textViewDescription: TextView = view.findViewById(R.id.text_view_task_details)
    val checkBox: CheckBox = view.findViewById(R.id.checkbox_task)
    lateinit var subtaskResultListener: TaskViewRenderer.SubtaskListener
}