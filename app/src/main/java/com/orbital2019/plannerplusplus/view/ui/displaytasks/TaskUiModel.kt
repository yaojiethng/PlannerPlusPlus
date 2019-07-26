package com.orbital2019.plannerplusplus.view.ui.displaytasks

import com.orbital2019.plannerplusplus.constants.TASK_ITEMMODEL
import com.orbital2019.plannerplusplus.model.entity.TaskEntity
import com.orbital2019.plannerplusplus.view.rendereradapter.CompositeItemModel
import com.orbital2019.plannerplusplus.view.rendereradapter.ItemModel

/**
 * ItemModel which represents the view data retrieved from a single TaskEntity.
 * This model may or may not have subtasks. If it has subtasks, they will be displayed.
 */
class TaskUiModel(
    val id: Long?,
    val title: String?,
    val details: String?,
    val isComplete: Boolean,
    val task: TaskEntity?
) : CompositeItemModel {

    constructor(task: TaskEntity) : this(
        task.id,
        task.title,
        task.details,
        task.isComplete,
        task
    )

    /**
     * Data retrieved also includes a list of Subtasks
     * As items is passed by reference to the adapter, make sure that it is a val.
     */
    override var items = ArrayList<ItemModel>()

    override fun getType(): Int {
        return TASK_ITEMMODEL
    }

}