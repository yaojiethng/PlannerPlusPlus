package com.orbital2019.plannerplusplus.view.ui.selecttime

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment
import org.threeten.bp.LocalTime


class TimePickerFragment(var timeNow: LocalTime?) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (timeNow == null) timeNow = LocalTime.now()
        return TimePickerDialog(
            activity,
            activity as TimePickerDialog.OnTimeSetListener?,
            timeNow!!.hour,
            timeNow!!.minute,
            DateFormat.is24HourFormat(activity)
        )
    }
}