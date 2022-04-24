package co.ke.mshirika.mshirika_app.ui.util

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import co.ke.mshirika.mshirika_app.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator

class LoadingDialog(context: Context, cancel: () -> Unit) {
    private val builder = MaterialAlertDialogBuilder(context).apply {
        val root = LinearLayout(context)
        val padding = context.resources.getDimensionPixelSize(R.dimen.padding_dialog)
        val params = LinearLayout.LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT
        )
        root.layoutParams = params
        root.setPadding(padding)
        root.gravity = Gravity.CENTER

        val progressIndicator = CircularProgressIndicator(context).apply {
            isIndeterminate = true
        }
        root.addView(progressIndicator)

        setOnCancelListener {
            val dismiss = MaterialAlertDialogBuilder(context).apply {
                setMessage(R.string.cancel_upload_prompt)
                setPositiveButton(R.string.okay) { dialog, _ ->
                    cancel()
                    it.dismiss()
                    dialog.cancel()
                }
                setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
            }
            dismiss.show()
        }
    }

    private var dialog:AlertDialog? = null

    fun show() {
        dialog = builder.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

}