package com.orbital2019.plannerplusplus.view.ui.selecttime

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.model.entity.TaskEntity

class LinkTaskAdapter(context: Context) :
    ArrayAdapter<TaskEntity>(context, R.layout.listitem_link_task) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            convertView ?: LayoutInflater.from(parent.context).inflate(R.layout.listitem_link_task, parent, false)
        val holder = LinkTaskHolder(view)
        holder.bind(getItem(position)!!)
        return view
    }

    inner class LinkTaskHolder(itemView: View) {
        var textViewTitle: TextView = itemView.findViewById(R.id.text_view_task_title)
        var deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        fun bind(task: TaskEntity) {
            textViewTitle.text = task.title
            deleteButton.setOnClickListener {
                remove(task)
            }
        }
    }
}