package com.orbital2019.plannerplusplus.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.orbital2019.plannerplusplus.model.EventEntity
import org.threeten.bp.LocalDateTime

// TODO decide on essential parameters and mark as nonNull in constructor

class PlannerEvent(
    var id: Int?,
    var title: String,
    var dateTimeRaw: LocalDateTime,
    var details: String?,
    var repeated: Boolean,
    var followUp: Boolean,
    private var tags: List<String> = mutableListOf()
) : Parcelable, Taggable {

    constructor(parcel: Parcel) : this(
        parcel.readSerializable() as Int,
        parcel.readString()!!,
        LocalDateTime.parse(parcel.readString()),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        Taggable.splitTag(parcel.readString())
    )

    constructor(entity: EventEntity) : this(
        entity.id!!,
        entity.title,
        LocalDateTime.parse(entity.dateTime),
        entity.details,
        entity.repeated,
        entity.followUp,
        Taggable.splitTag(entity.tags)
    )

    companion object CREATOR : Parcelable.Creator<PlannerEvent> {
        override fun createFromParcel(parcel: Parcel): PlannerEvent {
            return PlannerEvent(parcel)
        }

        override fun newArray(size: Int): Array<PlannerEvent?> {
            return arrayOfNulls(size)
        }

        fun createFromEntity(entity: EventEntity): PlannerEvent {
            return PlannerEvent(entity)
        }
    }

    fun generateEntity(): EventEntity {
        return EventEntity(
            title = title,
            dateTime = dateTimeRaw.toString(),
            details = if (details != null) details else "",
            repeated = repeated,
            followUp = followUp,
            tags = Taggable.concatTag(tags)
        )
    }

    fun updateEntity(): EventEntity {
        return EventEntity(
            id = id!!,
            title = title,
            dateTime = dateTimeRaw.toString(),
            details = if (details != null) details else "",
            repeated = repeated,
            followUp = followUp,
            tags = Taggable.concatTag(tags)
        )
    }

    // PARCELABLE INTERFACE METHODS

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(id)
        parcel.writeString(title)
        parcel.writeString(dateTimeRaw.toString())
        parcel.writeString(details)
        parcel.writeByte(if (repeated) 1 else 0)
        parcel.writeByte(if (followUp) 1 else 0)
        parcel.writeString(Taggable.concatTag(tags))
    }

    override fun describeContents(): Int {
        return 0
    }


}