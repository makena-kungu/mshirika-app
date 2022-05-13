package co.ke.mshirika.mshirika_app.ui_layer.ui.util

import android.text.Editable
import android.util.Patterns
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.shortDate
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.OperationalUtils.openDatePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object EditableUtils {


    fun TextInputEditText.text(): String =
        text.toString().trim()

    fun MaterialAutoCompleteTextView.text(): String =
        text.toString().trim()

    fun Editable.text() = toString().trim()

    val EditText.s: String
        get() = text.toString().trim()

    /**
     * The views pushed as parameters will be equipped with a text watcher that ensures that they're
     * not empty before enabling the button
     */
    fun MaterialButton.attachNonVoidFields(vararg inputFields: EditText) {
        isEnabled = inputFields.status

        inputFields.forEach { editText ->
            editText.doAfterTextChanged { editable ->
                editable?.let {
                    isEnabled = inputFields.status
                }
                //check if it doesn't contain an error
                isEnabled = inputFields.all { it.error.isEmpty() }
            }
        }
    }

    val Array<out EditText>.status get() = all { it.text.isNotEmpty() }

    fun Fragment.viewsOpeningTheDatePicker(vararg views: Triple<Int, EditText, TextInputLayout>) {
        views.forEach { (tag, editText, inputLayout) ->
            inputLayout.setEndIconOnClickListener {
                openDatePicker(tag) {
                    editText.setText(it.shortDate)
                }
            }
        }
    }

    fun TextInputEditText.emailAddressValidator() {
        val regex = Patterns.EMAIL_ADDRESS.pattern().toRegex()
        addTextChangedListener { editable ->
            editable?.let {
                regex.matches(it)
            }?.also {
                if (!it) error = context.getString(R.string.email_pattern_error)
            }
        }
    }

    fun clear(vararg editTexts: EditText) {
        editTexts.forEach {
            it.clear()
        }
    }

    fun EditText.clear() {
        text.clear()
        // TODO: if the above does not work, use setText("")
    }

    infix fun <A, B, C> A.andd(pair: Pair<B, C>): Triple<A, B, C> =
        Triple(this, pair.first, pair.second)
}