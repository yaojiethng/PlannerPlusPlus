package com.orbital2019.plannerplusplus.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.orbital2019.plannerplusplus.model.TaskEntity
import com.orbital2019.plannerplusplus.viewmodel.Taggable.Companion.concatTag
import com.orbital2019.plannerplusplus.viewmodel.Taggable.Companion.splitTag

// TODO decide on essential parameters and mark as nonNull in constructor

class PlannerTask (
    var id: Int?,
    var title: String,
    var details: String?,
    var autoNumber: Boolean,
    private var tags: List<String> = mutableListOf()
) : Parcelable, Taggable {

    constructor(parcel: Parcel) : this(
        parcel.readSerializable() as Int,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        splitTag(parcel.readString())
    )

    constructor(entity: TaskEntity) : this(
        entity.id!!,
        entity.title,
        entity.details,
        entity.autoNumber,
        splitTag(entity.tags)
    )

    companion object CREATOR : Parcelable.Creator<PlannerTask> {
        override fun createFromParcel(parcel: Parcel): PlannerTask {
            return PlannerTask(parcel)
        }

        override fun newArray(size: Int): Array<PlannerTask?> {
            return arrayOfNulls(size)
        }

        fun createFromEntity(entity: TaskEntity): PlannerTask {
            return PlannerTask(entity)
        }
    }

    fun generateEntity(): TaskEntity {
        return TaskEntity(
            title = title,
            details = if (details != null) details else "",
            autoNumber = autoNumber,
            tags = concatTag(tags)
        )
    }

    fun updateEntity(): TaskEntity {
        return TaskEntity(
            id = id!!,
            title = title,
            details = if (details != null) details else "",
            autoNumber = autoNumber,
            tags = concatTag(tags)
        )
    }

    // PARCELABLE INTERFACE METHODS

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(id)
        parcel.writeString(title)
        parcel.writeString(details)
        parcel.writeByte(if (autoNumber) 1 else 0)
        parcel.writeString(concatTag(tags))
    }

    override fun describeContents(): Int {
        return 0
    }


}