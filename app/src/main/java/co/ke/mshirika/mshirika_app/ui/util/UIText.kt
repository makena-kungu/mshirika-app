package co.ke.mshirika.mshirika_app.ui.util

import android.content.Context

sealed class UIText(open val action: () -> Unit) {

    data class DynamicText(
        val text: String,
        val title: String? = null,
        override val action: () -> Unit
    ) : UIText(action)

    class ResourceText(
        val id: Int,
        vararg val args: Any,
        override val action: () -> Unit
    ) : UIText(action)

    fun text(context: Context): String {
        return when (this) {
            is DynamicText -> text
            is ResourceText -> context.getString(id, *args)
        }
    }
}

fun dynamicText(text: String, title: String? = null, action: () -> Unit): UIText.DynamicText {
    return UIText.DynamicText(text, title, action)
}