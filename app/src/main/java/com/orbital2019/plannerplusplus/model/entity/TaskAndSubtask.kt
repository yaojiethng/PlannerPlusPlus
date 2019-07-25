package com.orbital2019.plannerplusplus.model.entity

import com.orbital2019.plannerplusplus.view.ui.displaytasks.SubtaskUiModel

class TaskAndSubtask(var task: TaskEntity, private vararg val subtasks: SubtaskUiModel) {
    fun getSubtasks(parentId: Long): Array<SubtaskEntity> {
        return subtasks.map {
            SubtaskEntity(it.id, it.title, it.isComplete, parentId)
        }.toTypedArray()
    }
}