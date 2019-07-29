package com.orbital2019.plannerplusplus.view.ui.displayevents

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.constants.EVENT_ITEMMODEL
import com.orbital2019.plannerplusplus.view.rendereradapter.CompositeViewRenderer
import com.orbital2019.plannerplusplus.view.rendereradapter.RendererRecyclerViewAdapter
import org.threeten.bp.format.DateTimeFormatter


class EventViewRenderer(
    context: Context,
    private val itemClickListener: OnItemClickListener,
    private val childAdapterBinder: ChildAdapterBinder
) :
    CompositeViewRenderer<EventUiModel, EventViewHolder>(context) {

    override val type: Int
        get() = EVENT_ITEMMODEL

    override fun bindView(model: EventUiModel, holder: EventViewHolder) {

        val adapter = holder.mRecyclerView?.adapter as RendererRecyclerViewAdapter

        // Call interface which binds childAdapter to LiveData
        childAdapterBinder.bindChildAdapter(model.id!!, adapter)
        model.items = adapter.mItems

        // When card is clicked, pass the taskEntity in its position in the adapter to the listener
        holder.itemView.setOnClickListener {
            // lambda expression called to override onClick method
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(model)
            }
        }

        // if number of Subtasks are to be tracked, use a LiveData<Int>, due to nature of Async updates
        holder.textViewTitle.text = model.title
        holder.textViewDescription.text = model.details
        holder.textViewDateTime.text = String.format(
            "%s, %s",
            model.dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE),
            model.dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
        )

    }

    override fun createCompositeViewHolder(parent: ViewGroup): EventViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return EventViewHolder(
            inflater.inflate(R.layout.listitem_event, parent, false)
        )
    }

    /**
     * OnItemClickListener provides a interface that represents a listener
     * The listener has a single method which represents a RecyclerView item being clicked
     */
    interface OnItemClickListener {
        fun onItemClick(model: EventUiModel)
    }

    interface ChildAdapterBinder {
        // link LiveData to mItems in adapter
        fun bindChildAdapter(id: Long, adapter: RendererRecyclerViewAdapter)
    }
}