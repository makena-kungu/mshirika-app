package co.ke.mshirika.mshirika_app.ui.util

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker

object Utils {
    fun openDatePicker(title: String, fragmentMan: FragmentManager, action: (Long) -> Unit) {
        val constraints = CalendarConstraints.Builder()
            .setEnd(System.currentTimeMillis())
            .build()

        val datePicker = datePicker().run {
            setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            setCalendarConstraints(constraints)
            setTitleText(title)
            build()
        }
        datePicker.run {
            addOnNegativeButtonClickListener {
                dismiss()
            }
            addOnPositiveButtonClickListener {
                action(it)
                dismiss()
            }

            show(fragmentMan, "{$title}_date_picker")
        }

    }
}