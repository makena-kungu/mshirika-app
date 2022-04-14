package co.ke.mshirika.mshirika_app.utility

object Util {
    fun headers(authKey: String) = mapOf(
        "Fineract-Platform-TenantId" to "default",
        "Authorization" to authKey
    )

    val String.headers: Map<String, String>
        get() = headers(this)
}