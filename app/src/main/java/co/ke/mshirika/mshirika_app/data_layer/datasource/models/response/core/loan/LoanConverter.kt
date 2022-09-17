package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@TypeConverters
object LoanConverter {
    @JvmStatic
    val gson: Gson
        get() = Gson()

    @TypeConverter
    @JvmStatic
    fun fromAmortizationType(obj: AmortizationType): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toAmortizationType(s: String): AmortizationType {
        val type = object : TypeToken<AmortizationType>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromCurrency(obj: Currency): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toCurrency(s: String): Currency {
        val type = object : TypeToken<Currency>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromDaysInMonthType(obj: DaysInMonthType): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toDaysInMonthType(s: String): DaysInMonthType {
        val type = object : TypeToken<DaysInMonthType>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromDaysInYearType(obj: DaysInYearType): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toDaysInYearType(s: String): DaysInYearType {
        val type = object : TypeToken<DaysInYearType>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromInterestCalculationPeriodType(obj: InterestCalculationPeriodType): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toInterestCalculationPeriodType(s: String): InterestCalculationPeriodType {
        val type =
            object : TypeToken<InterestCalculationPeriodType>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromInterestRateFrequencyType(obj: InterestRateFrequencyType): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toInterestRateFrequencyType(s: String): InterestRateFrequencyType {
        val type = object : TypeToken<InterestRateFrequencyType>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromInterestRecalculationData(obj: InterestRecalculationData): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toInterestRecalculationData(s: String): InterestRecalculationData {
        val type = object : TypeToken<InterestRecalculationData>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromInterestType(obj: InterestType): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toInterestType(s: String): InterestType {
        val type = object : TypeToken<InterestType>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromLoanType(obj: LoanType): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toLoanType(s: String): LoanType {
        val type = object : TypeToken<LoanType>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromPaidInAdvance(obj: PaidInAdvance): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toPaidInAdvance(s: String): PaidInAdvance {
        val type = object : TypeToken<PaidInAdvance>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromRepaymentFrequencyType(obj: RepaymentFrequencyType): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toRepaymentFrequencyType(s: String): RepaymentFrequencyType {
        val type = object : TypeToken<RepaymentFrequencyType>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromStatus(obj: Status): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toStatus(s: String): Status {
        val type = object : TypeToken<Status>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromSummary(obj: Summary): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toSummary(s: String): Summary {
        val type = object : TypeToken<Summary>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromTermPeriodFrequencyType(obj: TermPeriodFrequencyType): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toTermPeriodFrequencyType(s: String): TermPeriodFrequencyType {
        val type = object : TypeToken<TermPeriodFrequencyType>() {}.type
        return gson.fromJson(s, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromTimeline(obj: Timeline): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    @JvmStatic
    fun toTimeline(s: String): Timeline {
        val type = object : TypeToken<Timeline>() {}.type
        return gson.fromJson(s, type)
    }
}