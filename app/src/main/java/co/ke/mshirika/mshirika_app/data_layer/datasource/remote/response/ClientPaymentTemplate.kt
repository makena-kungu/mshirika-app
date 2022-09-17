package co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response


import android.os.Parcelable
import androidx.annotation.Keep
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Respondent
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ClientPaymentTemplate(
    val clientData: ClientData,
    val loanAccounts: List<LoanAccount>,
    val savingsAccounts: List<SavingsAccount>,
    val paymentTypes: List<PaymentType>
) : Respondent {
    @Keep
    @Parcelize
    data class ClientData(
        val id: Int,
        val accountNo: String,
        val externalId: String,
        val status: Status,
        val subStatus: SubStatus,
        val active: Boolean,
        val activationDate: List<Int>,
        val firstname: String,
        val middlename: String,
        val lastname: String,
        val displayName: String,
        val mobileNo: String,
        val dateOfBirth: List<Int>,
        val gender: Gender,
        val clientType: ClientType,
        val clientClassification: ClientClassification,
        val isStaff: Boolean,
        val officeId: Int,
        val officeName: String,
        val staffId: Int,
        val staffName: String,
        val timeline: Timeline,
        val savingsProductId: Int,
        val savingsProductName: String,
        val savingsAccountId: Int,
        val legalForm: LegalForm,
        val nationalId: String,
        //val groups: List<Any>,
        val clientNonPersonDetails: ClientNonPersonDetails
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Status(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class SubStatus(
            val id: Int,
            val name: String,
            val description: String,
            val active: Boolean,
            val mandatory: Boolean
        ) : Parcelable

        @Keep
        @Parcelize
        data class Gender(
            val id: Int,
            val name: String,
            val active: Boolean,
            val mandatory: Boolean
        ) : Parcelable

        @Keep
        @Parcelize
        data class ClientType(
            val id: Int,
            val name: String,
            val active: Boolean,
            val mandatory: Boolean
        ) : Parcelable

        @Keep
        @Parcelize
        data class ClientClassification(
            val id: Int,
            val name: String,
            val active: Boolean,
            val mandatory: Boolean
        ) : Parcelable

        @Keep
        @Parcelize
        data class Timeline(
            val submittedOnDate: List<Int>,
            val submittedByUsername: String,
            val submittedByFirstname: String,
            val submittedByLastname: String,
            val activatedOnDate: List<Int>,
            val activatedByUsername: String,
            val activatedByFirstname: String,
            val activatedByLastname: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class LegalForm(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class ClientNonPersonDetails(
            val constitution: Constitution,
            val mainBusinessLine: MainBusinessLine
        ) : Parcelable {
            @Keep
            @Parcelize
            data class Constitution(
                val active: Boolean,
                val mandatory: Boolean
            ) : Parcelable

            @Keep
            @Parcelize
            data class MainBusinessLine(
                val active: Boolean,
                val mandatory: Boolean
            ) : Parcelable
        }
    }

    @Keep
    @Parcelize
    data class LoanAccount(
        val id: Int,
        val accountNo: String,
        val productId: Int,
        val productName: String,
        val shortProductName: String,
        val status: Status,
        val loanType: LoanType,
        val loanCycle: Int,
        val timeline: Timeline,
        val inArrears: Boolean,
        val originalLoan: Double,
        val loanBalance: Double,
        val amountPaid: Double
    ) : Parcelable {
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
        @Parcelize
        data class LoanType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class Timeline(
            val submittedOnDate: List<Int>,
            val submittedByUsername: String,
            val submittedByFirstname: String,
            val submittedByLastname: String,
            val approvedOnDate: List<Int>,
            val approvedByUsername: String,
            val approvedByFirstname: String,
            val approvedByLastname: String,
            val expectedDisbursementDate: List<Int>,
            val actualDisbursementDate: List<Int>,
            val disbursedByUsername: String,
            val disbursedByFirstname: String,
            val disbursedByLastname: String,
            val expectedMaturityDate: List<Int>
        ) : Parcelable
    }

    @Keep
    @Parcelize
    data class SavingsAccount(
        val id: Int,
        val accountNo: String,
        val productId: Int,
        val productName: String,
        val shortProductName: String,
        val status: Status,
        val currency: Currency,
        val accountBalance: Double,
        val accountType: AccountType,
        val timeline: Timeline,
        val depositType: DepositType,
        //val savingsCharges: List<Any>
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Status(
            val id: Int,
            val code: String,
            val value: String,
            val submittedAndPendingApproval: Boolean,
            val approved: Boolean,
            val rejected: Boolean,
            val withdrawnByApplicant: Boolean,
            val active: Boolean,
            val closed: Boolean,
            val prematureClosed: Boolean,
            val transferInProgress: Boolean,
            val transferOnHold: Boolean,
            val matured: Boolean
        ) : Parcelable

        @Keep
        @Parcelize
        data class Currency(
            val code: String,
            val name: String,
            val decimalPlaces: Int,
            val inMultiplesOf: Int,
            val displaySymbol: String,
            val nameCode: String,
            val displayLabel: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class AccountType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable

        @Keep
        @Parcelize
        data class Timeline(
            val submittedOnDate: List<Int>,
            val submittedByUsername: String,
            val submittedByFirstname: String,
            val submittedByLastname: String,
            val approvedOnDate: List<Int>,
            val approvedByUsername: String,
            val approvedByFirstname: String,
            val approvedByLastname: String,
            val activatedOnDate: List<Int>
        ) : Parcelable

        @Keep
        @Parcelize
        data class DepositType(
            val id: Int,
            val code: String,
            val value: String
        ) : Parcelable
    }

    @Keep
    @Parcelize
    data class PaymentType(
        val id: Int,
        val name: String,
        val description: String,
        val isCashPayment: Boolean,
        val position: Int
    ) : Parcelable
}