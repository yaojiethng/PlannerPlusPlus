package com.orbital2019.plannerplusplus

import android.arch.lifecycle.AndroidViewModel
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

class EventsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.
        val viewModel: ViewModel by lazy {
            ViewModelProviders.of(this).get(AppViewModel::class.java)
        }

        return inflater.inflate(R.layout.fragment_events, container, false)


    }
}