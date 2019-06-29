package com.orbital2019.plannerplusplus.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.model.EventEntity
import com.orbital2019.plannerplusplus.model.PlannerEvent
import com.orbital2019.plannerplusplus.viewmodel.EventAdapter
import com.orbital2019.plannerplusplus.viewmodel.EventViewModel

// todo: link fab and options menu to fragment?
class EventsFragment : Fragment() {

    private val eventsViewModel: EventViewModel by lazy {
        ViewModelProviders.of(this).get(EventViewModel::class.java)
    }
    private lateinit var recyclerView: RecyclerView

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all -> {
                eventsViewModel.deleteAllEvents()
                Toast.makeText(activity, "All Notes Deleted", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val layout: View = inflater.inflate(R.layout.fragment_events, container, false)
        recyclerView = layout.findViewById(R.id.events_recycler_view)
        // LinearLayoutManager ensures that items are displayed linearly
        recyclerView.layoutManager = LinearLayoutManager(activity)
        // increases efficiency as our recyclerView will never change in size
        recyclerView.setHasFixedSize(true)

        val adapter = EventAdapter(recyclerView)
        recyclerView.adapter = adapter

        Log.d("FUN_CALL", "onCreateView called for class EventsFragment")

        // links this viewModel to this fragment, which means:
        //  this ViewModel will only update when this Fragment is in the foreground, and
        //  when this Fragment is closed, so will the ViewModel.
        eventsViewModel.getAllEvents().observe(
            this,
            Observer<List<EventEntity>> {
                Log.d("ONCHANGED", "EventViewModel onChanged")
                adapter.events = it as ArrayList<EventEntity>
            })

        // ItemTouchHelper makes RecyclerView swipe-able
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // When item is swiped, delete item from list.
            // todo: add features such as different directions, SnackBar to undo
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // position in adapter where the item is swiped
                eventsViewModel.deleteEvent(adapter.eventAt(viewHolder.adapterPosition))
                // call activity to get the activity attribute
                Toast.makeText(activity, "Event Deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerView)

        adapter.listener = object : EventAdapter.OnItemClickListener {
            override fun onItemClick(event: PlannerEvent) {
                // AddEditEventActivity::class.java is not used, but it is passed back when ActivityForResult terminates
                val intent = Intent(activity, AddEditEventActivity::class.java)
                intent.putExtra(EXTRA_PARCEL_PLANNEREVENT, event)
                startActivityForResult(intent, EDIT_EVENT_REQUEST)
            }
        }

        return layout
    }

}