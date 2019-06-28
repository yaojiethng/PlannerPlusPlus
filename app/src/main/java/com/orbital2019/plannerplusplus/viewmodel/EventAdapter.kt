package com.orbital2019.plannerplusplus.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.model.EventEntity

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventHolder>() {

    // todo: make a buffer Event class between EventEntity, which is stored in the database, and Event.

    // to prevent any null checks, init the list first
    internal var events = ArrayList<EventEntity>()
        internal set(events) {
            field = events
            notifyDataSetChanged()
            // todo: change to RecyclerView specific granular methods like notifyItemInserted and notifyItemRemoved
            //  which has animations
        }

    // @param parent: the ViewGroup that is passed, which is the RecyclerView
    // @return EventHolder: decides the layout for the different items in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventHolder(itemView)
    }

    // @return: the number of items that will be displayed in the RecyclerView
    override fun getItemCount(): Int {
        return events.size
    }

    // inserts data from Event objects into the view of the EventHolder
    // @Param holder: the holder object containing the different component views of the item
    // @Param position: the index of the current item being bound
    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        val currentEvent: EventEntity = events[position]
        holder.textViewTitle.text = currentEvent.title
        holder.textViewDescription.text = currentEvent.description
    }

    // this class holds the different views in our single view items
    inner class EventHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var textViewTitle: TextView = itemView.findViewById(R.id.text_view_event_title)
        internal var textViewDescription: TextView = itemView.findViewById(R.id.text_view_event_description)

    }

}
