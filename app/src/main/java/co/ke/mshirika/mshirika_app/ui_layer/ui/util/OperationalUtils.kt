package co.ke.mshirika.mshirika_app.ui_layer.ui.util

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.shortDate
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.time.*
import java.util.*

object OperationalUtils {
    private const val TAG = "OperationalUtils"

    context (Fragment)
    fun TextInputLayout.openDatePicker(@StringRes titleRes: Int, editText: TextInputEditText) {
        setEndIconOnClickListener {
            openDatePicker(titleRes) {
                editText.setText(it.shortDate)
            }
        }
    }

    fun Fragment.openDatePicker(@StringRes idRes: Int, action: (Long) -> Unit) =
        openDatePicker(getString(idRes), action)

    fun Fragment.openDatePicker(title: String, action: (Long) -> Unit) {

        val datePicker = datePicker().run {
            setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            val month = MonthDay.now().month
            val dayOfMonth = Calendar.getInstance()[Calendar.DAY_OF_MONTH]
            val year = Year.now().value
            val local: LocalDateTime = LocalDateTime.of(year, month, dayOfMonth, 0, 0)
            val end = local
                .atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                .toInstant()
                .toEpochMilli()
            val constraints = CalendarConstraints.Builder()
                .setEnd(end)
                .build()
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
        datePicker.show(childFragmentManager, "{$title}_date_picker")

    }
}