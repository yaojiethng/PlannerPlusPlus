package com.orbital2019.plannerplusplus.viewmodel

import com.orbital2019.plannerplusplus.model.entity.TaskEntity

interface TaskSelectedListener {

    /**
     * When onTaskSelected is called, close the calling fragment and return the correct Fragment
     */
    fun onTaskSelected(task: TaskEntity)

    fun getSelectedTask(): TaskEntity
}