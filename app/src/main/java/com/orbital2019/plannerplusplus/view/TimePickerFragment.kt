package com.orbital2019.plannerplusplus.view

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter


class TimePickerFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var timeNow = LocalTime.now()
        return TimePickerDialog(
            activity,
            activity as TimePickerDialog.OnTimeSetListener?,
            timeNow.hour,
            timeNow.minute,
            DateFormat.is24HourFormat(activity)
        )
    }

    fun getButtonText(hourOfDay: Int, minute: Int): String {
        return LocalTime.of(hourOfDay, minute).format(DateTimeFormatter.ISO_LOCAL_TIME)
    }
}