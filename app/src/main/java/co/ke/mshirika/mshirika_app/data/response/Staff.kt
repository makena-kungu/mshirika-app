package co.ke.mshirika.mshirika_app.data.response


import android.os.Parcelable
import co.ke.mshirika.mshirika_app.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
data class Staff(
    val authenticated: Boolean,
    val base64EncodedAuthenticationKey: String,
    val isTwoFactorAuthenticationRequired: Boolean,
    val officeId: Int,
    val officeName: String,
    val permissions: List<String>,
    val roles: List<Role>,
    val shouldRenewPassword: Boolean,
    val staffDisplayName: String,
    val staffId: Int,
    val userId: Int,
    val username: String
) : Parcelable,Respondent {

    @Parcelize
    data class Role(
        val description: String,
        val disabled: Boolean,
        val id: Int,
        val name: String
    ) : Parcelable
}