package co.ke.mshirika.mshirika_app.ui.util

import android.content.Context
import android.util.Patterns
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.shortDate
import co.ke.mshirika.mshirika_app.ui.util.OperationalUtils.openDatePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object EditableUtils {


    fun TextInputEditText.text(): String =
        text.toString().trim()

    fun MaterialAutoCompleteTextView.text():String =
        text.toString().trim()

    val EditText.s: String
        get() = text.trim().toString()

    val EditText.canBeEmptyText: String?
        get() {
            val t = text.toString().trim()
            if (t.isEmpty()) return null
            return t
        }

    fun EditText.nonEmptyText(field: String, context: Context, action: (Boolean) -> Unit): String? {
        val t = text.toString().trim()

        val notEmpty = t.isNotEmpty()
        action(notEmpty)// if it's not empty you can proceed
        if (notEmpty)
            return t

        if (this is TextInputEditText) error =
            context.getString(co.ke.mshirika.mshirika_app.R.string.field_cannot_be_empty, field)
        return null
    }

    /**
     * The views pushed as parameters will be equiped with a text watcher that ensures that they're
     * not empty before enabling the button
     */
    fun MaterialButton.attachNonVoidFields(vararg inputFields: EditText) {
        isEnabled = inputFields.map { it.text.isNotEmpty() }.reduce { acc, b -> acc && b }

        inputFields.forEach { editText ->
            editText.addTextChangedListener { editable ->
                editable?.let {
                    isEnabled =
                        inputFields.map { it.text.isNotEmpty() }.reduce { acc, b -> acc && b }
                }
            }
        }
    }

    fun Fragment.viewsOpeningTheDatePicker(vararg views: Triple<String, EditText, TextInputLayout>) {
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

    infix fun <A, B, C> A.andd(pair: Pair<B, C>): Triple<A, B, C> =
        Triple(this, pair.first, pair.second)
}