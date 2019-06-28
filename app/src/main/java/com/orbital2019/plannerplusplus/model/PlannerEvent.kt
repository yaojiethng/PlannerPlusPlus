package com.orbital2019.plannerplusplus.model

import android.os.Parcel
import android.os.Parcelable
import org.threeten.bp.LocalDateTime

// TODO decide on essential parameters and mark as nonNull in constructor

class PlannerEvent(
    var title: String,
    dateTimeRaw: LocalDateTime,
    var details: String?,
    var repeated: Boolean,
    var followUp: Boolean,
    var tags: List<String> = mutableListOf()
) : Parcelable {

    // convert LocalDateTime into a String for easy storage
    private var dateTime: String = dateTimeRaw.toString()

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        LocalDateTime.parse(parcel.readString()),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        splitTag(parcel.readString())
    )

    companion object CREATOR : Parcelable.Creator<PlannerEvent> {
        override fun createFromParcel(parcel: Parcel): PlannerEvent {
            return PlannerEvent(parcel)
        }

        override fun newArray(size: Int): Array<PlannerEvent?> {
            return arrayOfNulls(size)
        }

        private fun splitTag(string: String?): List<String> {
            return string?.split(", ") ?: listOf()
        }
    }

    fun generateEntity(): EventEntity {
        return EventEntity(
            title = title,
            dateTime = dateTime,
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
        parcel.writeString(title)
        parcel.writeString(dateTime)
        parcel.writeString(details)
        parcel.writeByte(if (repeated) 1 else 0)
        parcel.writeByte(if (followUp) 1 else 0)
        parcel.writeString(concatTag())
    }

    override fun describeContents(): Int {
        return 0
    }


}