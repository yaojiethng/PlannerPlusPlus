package com.orbital2019.plannerplusplus.view.ui.selecttask

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R

class LinkTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textViewTitle: TextView = itemView.findViewById(R.id.checked_view_task_title)
    var deleteButton: ImageView = itemView.findViewById(R.id.delete_button)
}