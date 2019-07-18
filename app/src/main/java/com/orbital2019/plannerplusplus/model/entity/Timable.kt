package com.orbital2019.plannerplusplus.model.entity

import androidx.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

interface Timable {
    // Type Converters from OffsetDateTime to String
    object TiviTypeConverters {
        private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        @TypeConverter
        @JvmStatic
        fun toOffsetDateTime(value: String?): OffsetDateTime? {
            return value?.let {
                return formatter.parse(value, OffsetDateTime::from)
            }
        }

        @TypeConverter
        @JvmStatic
        fun fromOffsetDateTime(date: OffsetDateTime?): String? {
            return date?.format(formatter)
        }
    }
}