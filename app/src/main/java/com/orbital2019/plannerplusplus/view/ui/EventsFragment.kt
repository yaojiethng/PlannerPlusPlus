package com.orbital2019.plannerplusplus.view.ui

import android.app.AlertDialog
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
import com.orbital2019.plannerplusplus.constants.EDIT_EVENT_REQUEST
import com.orbital2019.plannerplusplus.model.entity.EventEntity
import com.orbital2019.plannerplusplus.view.EventAdapter
import com.orbital2019.plannerplusplus.viewmodel.EventViewModel


// todo: link fab and options menu to fragment?
// Your fragments can contribute menu items to the activity's Options Menu (and, consequently, the app bar) by
// implementing onCreateOptionsMenu().
// In order for this method to receive calls, however, you must call setHasOptionsMenu() during onCreate()
class EventsFragment : Fragment() {

    private val eventsViewModel: EventViewModel by lazy {
        ViewModelProviders.of(this).get(EventViewModel::class.java)
    }
    private lateinit var recyclerView: RecyclerView

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            com.orbital2019.plannerplusplus.R.id.delete_all -> {
                eventsViewModel.deleteAllEvents()
                Toast.makeText(activity, "All Notes Deleted", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val layout: View = inflater.inflate(com.orbital2019.plannerplusplus.R.layout.fragment_events, container, false)
        recyclerView = layout.findViewById(com.orbital2019.plannerplusplus.R.id.events_recycler_view)
        // LinearLayoutManager ensures that items are displayed linearly
        recyclerView.layoutManager = LinearLayoutManager(activity)
        // increases efficiency as our recyclerView will never change in size
        recyclerView.setHasFixedSize(true)

        val adapter = EventAdapter(recyclerView)
        recyclerView.adapter = adapter

        // links this viewModel to this fragment, which means:
        //  this ViewModel will only updateEvent when this Fragment is in the foreground, and
        //  when this Fragment is closed, so will the ViewModel.
        eventsViewModel.getAllEvents().observe(
            viewLifecycleOwner,
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

            // When item is swiped, deleteEvent item from list.
            // todo: add features such as different directions, SnackBar to undo
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertDialog.Builder(context)
                    .setTitle("Delete Event")
                    .setMessage("Do you really want delete this task?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(
                        android.R.string.yes
                    ) { _, _ ->
                        // position in adapter where the item is swiped
                        eventsViewModel.deleteEvent(adapter.eventAt(viewHolder.adapterPosition))
                        // call activity to get the activity attribute
                        Toast.makeText(activity, "Event Deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(android.R.string.no) { dialog, _ ->
                        adapter.notifyItemChanged(viewHolder.adapterPosition)
                        dialog.cancel()
                    }.show()
            }
        }).attachToRecyclerView(recyclerView)

        adapter.listener = object : EventAdapter.OnItemClickListener {
            override fun onItemClick(event: EventEntity) {
                Log.d("EVENTSFRAGMENT", "item clicked.")
                // AddEditEventActivity::class.java is not used, but it is passed back when ActivityForResult terminates
                val intent = Intent(activity, AddEditEventActivity::class.java)
                intent.putExtra(EXTRA_PARCEL_PLANNEREVENT, event)
                startActivityForResult(intent, EDIT_EVENT_REQUEST)
            }
        }

        return layout
    }

}