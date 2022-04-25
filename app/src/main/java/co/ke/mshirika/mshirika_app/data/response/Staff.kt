package co.ke.mshirika.mshirika_app.data.response


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data.response.Staff.Role.Companion.role
import co.ke.mshirika.mshirika_app.data.response.Staff.Role.Companion.string
import co.ke.mshirika.mshirika_app.remote.utils.Respondent
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
    ) : Parcelable {
        companion object {

            fun Role.string(): String {
                val v = listOf(
                    description,
                    disabled.toString(),
                    id.toString(),
                    name
                )
                return v.joinToString(COMPONENT_DELIMITER)
            }

            fun String.role(): Role {
                val s = split(COMPONENT_DELIMITER)
                return Role(
                    description = s.first(),
                    disabled = s[1].toBoolean(),
                    id = s[2].toInt(),
                    name = s.last()
                )
            }
        }
    }

    companion object {
        private const val DELIMITER = "¬"
        private const val COMPONENT_DELIMITER = "¦"

        fun Staff.string(): String {
            val s = listOf(
                authenticated.toString(),
                base64EncodedAuthenticationKey,
                isTwoFactorAuthenticationRequired.toString(),
                officeId.toString(),
                officeName,
                permissions.joinToString(COMPONENT_DELIMITER),
                roles.joinToString(COMPONENT_DELIMITER) { it.string() },
                shouldRenewPassword.toString(),
                staffDisplayName,
                staffId.toString(),
                userId.toString(),
                username,
            )
            return s.joinToString(DELIMITER)
        }

        fun String.staff(): Staff {
            val staff = split(DELIMITER)
            return Staff(
                staff[0].toBoolean(),
                staff[1],
                staff[2].toBoolean(),
                staff[3].toInt(),
                staff[4],
                staff[5].split(COMPONENT_DELIMITER),
                staff[6].split(COMPONENT_DELIMITER).map { it.role() },
                staff[7].toBoolean(),
                staff[8],
                staff[9].toInt(),
                staff[10].toInt(),
                staff.last(),
            )
        }
    }
}