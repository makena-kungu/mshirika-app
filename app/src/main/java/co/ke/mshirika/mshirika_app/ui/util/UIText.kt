package co.ke.mshirika.mshirika_app.ui.util

import android.content.Context

sealed class UIText() {

    data class PlainText(
        val s: String
    ) : UIText()

    data class DynamicText(
        val text: String,
        val title: String? = null,
        val action: () -> Unit
    ) : UIText()

    class ResourceText(
        val id: Int,
        vararg val args: Any,
        val action: () -> Unit
    ) : UIText()

    fun text(context: Context): String {
        return when (this) {
            is DynamicText -> text
            is ResourceText -> context.getString(id, *args)
            is PlainText -> s
        }
    }
}

fun dynamicText(text: String, title: String? = null, action: () -> Unit): UIText.DynamicText {
    return UIText.DynamicText(text, title, action)
}