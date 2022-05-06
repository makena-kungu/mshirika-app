package co.ke.mshirika.mshirika_app.ui_layer.ui.util

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import java.util.*

object OperationalUtils {
    private const val TAG = "OperationalUtils"
    fun Fragment.openDatePicker(@StringRes idRes: Int, action: (Long) -> Unit) =
        openDatePicker(getString(idRes), action)

    fun Fragment.openDatePicker(title: String, action: (Long) -> Unit) {
        val constraints = CalendarConstraints.Builder()
            .setEnd(Calendar.getInstance().timeInMillis)
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

        }
        datePicker.show(parentFragmentManager, "{$title}_date_picker")

    }
}