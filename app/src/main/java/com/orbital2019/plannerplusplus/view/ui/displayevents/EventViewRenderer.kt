package com.orbital2019.plannerplusplus.view.ui.displayevents

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.constants.EVENT_ITEMMODEL
import com.orbital2019.plannerplusplus.view.rendereradapter.CompositeViewRenderer
import com.orbital2019.plannerplusplus.view.rendereradapter.ItemModel
import com.orbital2019.plannerplusplus.view.rendereradapter.RendererRecyclerViewAdapter
import org.threeten.bp.OffsetDateTime


class EventViewRenderer(
    context: Context,
    private val itemClickListener: OnItemClickListener
) :
    CompositeViewRenderer<EventUiModel, EventViewHolder>(context) {

    override val type: Int
        get() = EVENT_ITEMMODEL

    override fun bindView(model: EventUiModel, holder: EventViewHolder) {

        // When card is clicked, pass the taskEntity in its position in the adapter to the listener
        holder.itemView.setOnClickListener {
            // lambda expression called to override onClick method
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(model)
            }
        }

        // if number of Subtasks are to be tracked, use a LiveData<Int>, due to nature of Async updates
        val timeNow = OffsetDateTime.now()
        val startTime = model.startDateTime
        val endTime = model.endDateTime
        holder.textViewStatus.text = when {
            startTime.isBefore(timeNow) -> "[UPCOMING] "
            endTime.isBefore(timeNow) -> "[ONGOING] "
            else -> "[COMPLETED] "
        }
        holder.textViewTitle.text = model.title
        holder.textViewDescription.text = model.details
        holder.textViewDateTime.text = String.format(
            "%02d:%02d@%d %s\n to\n %02d:%02d@%d %s",
            startTime.hour, startTime.minute,
            startTime.dayOfMonth, startTime.month.toString(),
            endTime.hour, endTime.minute,
            endTime.dayOfMonth, endTime.month.toString()
        )

        model.tasks.observe(
            context as LifecycleOwner,
            Observer<List<ItemModel>> {
                val adapter = holder.mRecyclerView?.adapter as RendererRecyclerViewAdapter
                adapter.mItems.clear()
                adapter.mItems.addAll(it)
                adapter.notifyDataSetChanged()
            }
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

    interface UpdatedSubtaskListener {
        fun onSubtaskUpdated(model: EventUiModel, subtasks: ArrayList<ItemModel>)
    }
}