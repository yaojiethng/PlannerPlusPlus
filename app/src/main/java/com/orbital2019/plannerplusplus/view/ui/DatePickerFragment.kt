package com.orbital2019.plannerplusplus.view.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import org.threeten.bp.LocalDate


class DatePickerFragment(var dateNow: LocalDate?) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (dateNow == null) dateNow = LocalDate.now()
        return DatePickerDialog(
            activity as Context,
            activity as DatePickerDialog.OnDateSetListener,
            dateNow!!.year,
            dateNow!!.monthValue - 1,
            dateNow!!.dayOfMonth
        )

    }
}