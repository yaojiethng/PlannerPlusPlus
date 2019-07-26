package com.orbital2019.plannerplusplus.view.ui.displaytasks

import android.view.View
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R


class SubtaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val checkedTextView: CheckedTextView = view.findViewById(R.id.checked_view_task_title)

}