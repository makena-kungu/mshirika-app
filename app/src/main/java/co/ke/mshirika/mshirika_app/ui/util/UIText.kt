package co.ke.mshirika.mshirika_app.ui.util

import android.content.Context

sealed class UIText(open val action: (String) -> Unit) {

    data class DynamicText(val text: String, override val action: (String) -> Unit) : UIText(action)
    class ResourceText(
        val id: Int,
        vararg val args: Any,
        override val action: (String) -> Unit
    ) : UIText(action)

    fun text(context: Context): String {
        return when (this) {
            is DynamicText -> text
            is ResourceText -> context.getString(id, *args)
        }
    }
}

fun dynamicText(text: String, action: (String) -> String): UIText.DynamicText {
    return UIText.DynamicText(text) {
        //the string returned by the
        action(it)
    }
}