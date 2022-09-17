package co.ke.mshirika.mshirika_app.data_layer.datasource.models.request


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity("new_loan_table")
@TypeConverters(NewLoan.NewLoanConverter::class)
data class NewLoan(
    @PrimaryKey @Expose(serialize = false) val id: Int,
    val productId: Int,
    val loanOfficerId: Int,
    val loanPurposeId: Int,
    val fundId: String = "",
    val submittedOnDate: String,
    val expectedDisbursementDate: String,
    val externalId: String,
    val linkAccountId: Int,
    val createStandingInstructionAtDisbursement: String,
    val principal: Int,
    val loanTermFrequency: Int,
    val loanTermFrequencyType: Int,
    val numberOfRepayments: Int,
    val repaymentEvery: Int,
    val repaymentFrequencyType: Int,
    val repaymentFrequencyNthDayType: String = "",
    val repaymentFrequencyDayOfWeekType: String = "",
    val repaymentsStartingFromDate: String? = null,
    val interestChargedFromDate: String? = null,
    val interestRatePerPeriod: Int,
    val interestType: Int,
    val isEqualAmortization: Boolean = false,
    val amortizationType: Int,
    val interestCalculationPeriodType: Int,
    val allowPartialPeriodInterestCalcualtion: Boolean,
    val transactionProcessingStrategyId: Int,
    val graceOnArrearsAgeing: Int,
    val loanIdToClose: String = "",
    val isTopup: String = "",
    val recalculationCompoundingFrequencyDate: String = "",
    val charges: List<Charge>,
    val collateral: List<String> = emptyList(),
    val clientId: Int,
    val dateFormat: String = DateUtil.DATE_FORMAT,
    val locale: String = "en",
    val loanType: String = "individual"
) : Parcelable {
    @Keep
    @Parcelize
    data class Charge(
        val chargeId: Int,
        val amount: Int
    ) : Parcelable

    object NewLoanConverter {
        @TypeConverter
        @JvmStatic
        fun toCharge(string: String): List<Charge> {
            val gson = Gson()
            return gson.fromJson(string, object : TypeToken<List<Charge>>() {}.type)
        }

        @TypeConverter
        @JvmStatic
        fun fromCharge(obj: List<Charge>): String {
            return Gson().toJson(obj)
        }

        @TypeConverter
        @JvmStatic
        fun toCollaterals(string: String): List<String> {
            val type = object : TypeToken<List<String>>() {}.type
            return Gson().fromJson(string, type)
        }

        @TypeConverter
        @JvmStatic
        fun fromCollaterals(obj: List<String>): String {
            return Gson().toJson(obj)
        }

    }
}