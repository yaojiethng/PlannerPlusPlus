package com.orbital2019.plannerplusplus.view.ui.displaytasks

import com.orbital2019.plannerplusplus.constants.TASK_ITEMMODEL
import com.orbital2019.plannerplusplus.model.entity.TaskEntity
import com.orbital2019.plannerplusplus.view.rendereradapter.CompositeItemModel
import com.orbital2019.plannerplusplus.view.rendereradapter.ItemModel

/**
 * ItemModel which represents a single TaskEntity.
 * This model may or may not have subtasks. If it has subtasks, they will be displayed.
 */
class TaskUiModel(
    val id: Long?,
    val title: String?,
    val details: String?,
    val isComplete: Boolean,
    val task: TaskEntity?,
    vararg val subtasks: ItemModel
) : CompositeItemModel {

    constructor(task: TaskEntity) : this(
        task.id,
        task.title,
        task.details,
        task.isComplete,
        task
    )

    override var items = ArrayList<ItemModel>()
        set(value) {
            items.clear()
            items.addAll(value)
            subtaskListener?.updateSubtask(value)
        }
    var subtaskListener: TaskViewRenderer.SubtaskListener? = null

    init {
        items.addAll(subtasks)
    }

    override fun getType(): Int {
        return TASK_ITEMMODEL
    }

}