package com.orbital2019.plannerplusplus.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.model.entity.EventEntity
import org.threeten.bp.format.DateTimeFormatter

class EventAdapter(var recycler: RecyclerView) : RecyclerView.Adapter<EventAdapter.EventHolder>() {

    // to prevent any null checks, init the list first
    internal var events = ArrayList<EventEntity>()
        internal set(events) {
            field = events
            notifyDataSetChanged()
            // todo: change to RecyclerView specific granular methods like notifyItemInserted and notifyItemRemoved which has animations
        }
    internal lateinit var listener: OnItemClickListener

    fun eventAt(position: Int): EventEntity {
        return events[position]
    }

    // @param parent: the ViewGroup that is passed, which is the RecyclerView
    // @return EventHolder: decides the layout for the different items in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_item, parent, false)
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
        val currentEventEntity: EventEntity = events[position]
        holder.textViewTitle.text = currentEventEntity.title
        holder.textViewDescription.text = currentEventEntity.details
        holder.textViewDateTime.text = currentEventEntity.eventStartTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    }

    // this class holds the different views in our single view items
    // todo: updateEvent with new variables once critical parameters are decided
    inner class EventHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.text_view_event_title)
        var textViewDescription: TextView = itemView.findViewById(R.id.text_view_event_details)
        var textViewDateTime: TextView = itemView.findViewById(R.id.text_view_event_datetime)

        init {
            // When card is called, pass the eventEntity in its position in the adapter to the listener
            itemView.setOnClickListener {
                // lambda expression called to override onClick method
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(events[position])
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(event: EventEntity)
    }
}
