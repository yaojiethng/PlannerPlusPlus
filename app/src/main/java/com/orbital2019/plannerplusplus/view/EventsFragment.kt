package com.orbital2019.plannerplusplus.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orbital2019.plannerplusplus.R
import com.orbital2019.plannerplusplus.model.EventEntity
import com.orbital2019.plannerplusplus.viewmodel.EventAdapter
import com.orbital2019.plannerplusplus.viewmodel.EventViewModel

class EventsFragment : Fragment() {

    private val eventsViewModel: EventViewModel by lazy {
        ViewModelProviders.of(this).get(EventViewModel::class.java)
    }
    private lateinit var recyclerView: RecyclerView

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


        return layout
    }
}