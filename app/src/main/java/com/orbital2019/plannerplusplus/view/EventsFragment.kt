package com.orbital2019.plannerplusplus.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orbital2019.plannerplusplus.R

class EventsFragment : androidx.fragment.app.Fragment() {

    // todo: set up recyclerview

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }
}