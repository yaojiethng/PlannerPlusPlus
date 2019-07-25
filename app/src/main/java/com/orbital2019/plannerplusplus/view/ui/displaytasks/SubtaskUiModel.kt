package com.orbital2019.plannerplusplus.view.ui.displaytasks

import com.orbital2019.plannerplusplus.constants.SUBTASK_ITEMMODEL
import com.orbital2019.plannerplusplus.model.entity.SubtaskEntity
import com.orbital2019.plannerplusplus.view.rendereradapter.ItemModel


class SubtaskUiModel(
    val id: Long?,
    val title: String?,
    val isComplete: Boolean,
    val subtask: SubtaskEntity?
) : ItemModel {

    constructor(subtask: SubtaskEntity) : this(
        subtask.id,
        subtask.title,
        subtask.isComplete,
        subtask
    )

    override fun getType(): Int {
        return SUBTASK_ITEMMODEL
    }
}