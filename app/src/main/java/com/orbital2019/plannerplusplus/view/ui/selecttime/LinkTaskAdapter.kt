package com.orbital2019.plannerplusplus.view.ui.selecttime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.model.entity.TaskEntity
import com.orbital2019.plannerplusplus.view.recyclerview.BaseViewHolder

class LinkTaskAdapter(var recycler: RecyclerView) :
    RecyclerView.Adapter<LinkTaskAdapter.LinkTaskHolder>() {

    internal var tasks = ArrayList<TaskEntity>()

    fun taskAt(position: Int): TaskEntity {
        return tasks[position]
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun addItem(task: TaskEntity) {
        tasks.add(task)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkTaskHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_link_task, parent, false)
        return LinkTaskHolder(itemView)
    }

    override fun onBindViewHolder(holder: LinkTaskAdapter.LinkTaskHolder, position: Int) {
        val task: TaskEntity = tasks[position]
        holder.bind(task)
    }


    inner class LinkTaskHolder(itemView: View) : BaseViewHolder<TaskEntity>(itemView) {
        private var textViewTitle: TextView = itemView.findViewById(R.id.checked_view_task_title)
        private var deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        init {
            deleteButton.setOnClickListener {
                // lambda expression called to override onClick method
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    tasks.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }

        override fun bind(item: TaskEntity) {
            textViewTitle.text = item.title
        }
    }
}