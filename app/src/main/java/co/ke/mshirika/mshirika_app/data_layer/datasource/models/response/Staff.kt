package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
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
) : Parcelable, Respondent {

    @Parcelize
    @Keep
    data class Role(
        val description: String,
        val disabled: Boolean,
        val id: Int,
        val name: String
    ) : Parcelable
}