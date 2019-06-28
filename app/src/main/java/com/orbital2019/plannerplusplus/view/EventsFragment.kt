package com.orbital2019.plannerplusplus.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orbital2019.plannerplusplus.Event
import com.orbital2019.plannerplusplus.EventViewModel
import com.orbital2019.plannerplusplus.R

class EventsFragment : Fragment() {

    private val eventsViewModel: EventViewModel by lazy {
        ViewModelProviders.of(this).get(EventViewModel::class.java)
    }

    // todo: set up RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.d("FUN_CALL", "onCreateView called for class EventsFragment")

        // links this viewModel to this fragment, which means:
        //  this ViewModel will only update when this Fragment is in the foreground, and
        //  when this Fragment is closed, so will the ViewModel.
        eventsViewModel.getAllEvents().observe(
            this,
            Observer<List<Event>> {
                Toast.makeText(activity, "onChanged", Toast.LENGTH_SHORT).show()
            })


        return inflater.inflate(R.layout.fragment_events, container, false)
    }
}