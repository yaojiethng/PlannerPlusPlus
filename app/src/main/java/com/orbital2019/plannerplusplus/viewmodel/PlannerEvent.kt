package com.orbital2019.plannerplusplus.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.orbital2019.plannerplusplus.helper.DateTimeData
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
) : Parcelable {

    // convert LocalDateTime into a String for easy storage
    private var dateTime: DateTimeData = DateTimeData(dateTimeRaw)

    constructor(parcel: Parcel) : this(
        parcel.readSerializable() as Int,
        parcel.readString()!!,
        LocalDateTime.parse(parcel.readString()),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        splitTag(parcel.readString())
    )

    constructor(entity: EventEntity) : this(
        entity.id!!,
        entity.title,
        LocalDateTime.parse(entity.dateTime),
        entity.details,
        entity.repeated,
        entity.followUp,
        splitTag(entity.tags)
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

        private fun splitTag(string: String?): List<String> {
            return string?.split(", ") ?: listOf()
        }
    }

    fun generateEntity(): EventEntity {
        return EventEntity(
            title = title,
            dateTime = dateTimeRaw.toString(),
            details = if (details != null) details else "",
            repeated = repeated,
            followUp = followUp,
            tags = concatTag()
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
            tags = concatTag()
        )
    }

    private fun concatTag(): String {
        return tags.joinToString { it }
    }

    // PARCELABLE INTERFACE METHODS

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(id)
        parcel.writeString(title)
        parcel.writeString(dateTimeRaw.toString())
        parcel.writeString(details)
        parcel.writeByte(if (repeated) 1 else 0)
        parcel.writeByte(if (followUp) 1 else 0)
        parcel.writeString(concatTag())
    }

    override fun describeContents(): Int {
        return 0
    }


}