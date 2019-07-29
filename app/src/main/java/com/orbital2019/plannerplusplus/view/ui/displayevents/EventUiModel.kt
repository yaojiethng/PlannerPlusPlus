package com.orbital2019.plannerplusplus.view.ui.displayevents

import androidx.lifecycle.MutableLiveData
import com.orbital2019.plannerplusplus.constants.EVENT_ITEMMODEL
import com.orbital2019.plannerplusplus.model.entity.EventEntity
import com.orbital2019.plannerplusplus.view.rendereradapter.CompositeItemModel
import com.orbital2019.plannerplusplus.view.rendereradapter.ItemModel
import org.threeten.bp.OffsetDateTime

/**
 * ItemModel which represents the view data retrieved from a single TaskEntity.
 * This model may or may not have subtasks. If it has subtasks, they will be displayed.
 */
class EventUiModel(
    val id: Long?,
    val title: String?,
    val details: String?,
    val startDateTime: OffsetDateTime,
    val endDateTime: OffsetDateTime,
    val event: EventEntity?
) : CompositeItemModel {

    constructor(event: EventEntity) : this(
        event.id,
        event.title,
        event.details,
        event.eventStartTime,
        event.eventEndTime!!,
        event
    )

    /**
     * Data retrieved also includes a list of Subtasks
     * As items is passed by reference to the adapter, make sure that it is a val.
     */
    override var items = ArrayList<ItemModel>()
    var tasks: MutableLiveData<List<ItemModel>> = MutableLiveData()

    override fun getType(): Int {
        return EVENT_ITEMMODEL
    }
}