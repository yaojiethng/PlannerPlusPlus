package com.orbital2019.plannerplusplus

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class PlannerPlusPlus : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}