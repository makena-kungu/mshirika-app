package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class LoanWithGuarantors(
    val id: Int,
    val accountNo: String,
    val externalId: String,
    val status: Status,
    val clientId: Int,
    val clientAccountNo: String,
    val clientName: String,
    val clientOfficeId: Int,
    val loanProductId: Int,
    val loanProductName: String,
    val guarantors: List<Guarantor>
) : Respondent {

    @Keep
    @Parcelize
    data class Status(
        val id: Int,
        val code: String,
        val value: String,
        val pendingApproval: Boolean,
        val waitingForDisbursal: Boolean,
        val active: Boolean,
        val closedObligationsMet: Boolean,
        val closedWrittenOff: Boolean,
        val closedRescheduled: Boolean,
        val closed: Boolean,
        val overpaid: Boolean
    ) : Parcelable

    @Keep
    data class Currency(
        val code: String,
        val name: String,
        val decimalPlaces: Int,
        val inMultiplesOf: Int,
        val displaySymbol: String,
        val nameCode: String,
        val displayLabel: String
    )

    @Keep
    data class Timeline(
        val submittedOnDate: List<Int>,
        val submittedByUsername: String,
        val submittedByFirstname: String,
        val submittedByLastname: String,
        val expectedDisbursementDate: List<Int>,
        val expectedMaturityDate: List<Int>
    )

    @Keep
    @Parcelize
    data class Guarantor(
        val id: Int,
        val loanId: Int,
        val guarantorType: GuarantorType,
        val firstname: String,
        val lastname: String,
        val entityId: Int,
        val externalId: String,
        val officeName: String,
        val joinedDate: List<Int>,
        val guarantorFundingDetails: List<GuarantorFundingDetail>,
        val status: Boolean
    ) : Parcelable {
        val name: String get() = "$firstname $lastname"

        @Keep
        @Parcelize
        data class GuarantorType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable {
            fun type(): String {
                return value.lowercase().replaceFirstChar { it.uppercaseChar() }
            }
        }

        @Keep
        @Parcelize
        data class GuarantorFundingDetail(
            val id: Int,
            val status: Status,
            val savingsAccount: SavingsAccount,
            val amount: Double,
            val amountReleased: Int,
            val amountRemaining: Double,
            val amountTransfered: Int
        ) : Parcelable {
            @Keep
            @Parcelize
            data class Status(
                val id: Int,
                val code: String,
                val value: String
            ) : Parcelable {
                fun status(): String {
                    return value.lowercase().replaceFirstChar { it.uppercaseChar() }
                }
            }

            @Keep
            @Parcelize
            data class SavingsAccount(
                val id: Int,
                val accountNo: String
            ) : Parcelable
        }

        companion object : DiffUtil.ItemCallback<Guarantor>() {
            override fun areItemsTheSame(oldItem: Guarantor, newItem: Guarantor): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Guarantor, newItem: Guarantor): Boolean {
                return oldItem == newItem
            }
        }
    }
}