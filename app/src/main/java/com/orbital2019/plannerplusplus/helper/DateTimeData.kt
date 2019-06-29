package com.orbital2019.plannerplusplus.helper

import org.threeten.bp.LocalDateTime

data class DateTimeData(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val hour: Int,
    val minute: Int
) {

    constructor(dateTimeRaw: LocalDateTime) : this(
        dateTimeRaw.year, dateTimeRaw.monthValue, dateTimeRaw.dayOfMonth, dateTimeRaw.hour, dateTimeRaw.minute
    )

    companion object {
        fun toArray(dateTime: DateTimeData): IntArray {
            return intArrayOf(dateTime.year, dateTime.month, dateTime.dayOfMonth, dateTime.hour, dateTime.minute)
        }
    }
}

