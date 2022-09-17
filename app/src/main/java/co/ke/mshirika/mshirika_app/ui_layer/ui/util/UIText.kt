package co.ke.mshirika.mshirika_app.ui_layer.ui.util

import android.content.Context

sealed interface UIText {

    data class PlainText(
        val s: String
    ) : UIText

    data class DynamicText(
        val text: String,
        val title: String? = null,
        val action: () -> Unit
    ) : UIText

    class ResourceText(
        val id: Int,
        vararg val args: Any
    ) : UIText

    class NoArgsResourceText(
        val id: Int
    ) : UIText

    fun text(context: Context): String {
        return when (this) {
            is DynamicText -> text
            is ResourceText -> context.getString(id, *args)
            is PlainText -> s
            is NoArgsResourceText -> context.getString(id)
        }
    }
}

fun plainText(text: String) = UIText.PlainText(text)

fun dynamicText(text: String, title: String? = null, action: () -> Unit): UIText.DynamicText {
    return UIText.DynamicText(text, title, action)
}

fun resourceText(resId: Int, vararg args: Any) = UIText.ResourceText(resId, args)

fun resourceText(resId: Int) = UIText.NoArgsResourceText(resId)