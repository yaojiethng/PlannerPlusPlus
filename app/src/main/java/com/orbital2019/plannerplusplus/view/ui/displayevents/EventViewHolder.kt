package com.orbital2019.plannerplusplus.view.ui.displayevents

import android.view.View
import android.widget.TextView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.view.rendereradapter.CompositeViewHolder

class EventViewHolder(itemView: View) : CompositeViewHolder(itemView) {
    var textViewTitle: TextView = itemView.findViewById(R.id.text_view_event_title)
    var textViewDescription: TextView = itemView.findViewById(R.id.text_view_event_details)
    var textViewDateTime: TextView = itemView.findViewById(R.id.text_view_event_datetime)

    init {
        // binds child recyclerView
        mRecyclerView = itemView.findViewById(R.id.subtasks_recycler_view)
    }
}
