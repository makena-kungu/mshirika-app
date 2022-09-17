package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.core.loans

import androidx.room.TypeConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.ConservativeLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.DetailedLoanAccount
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.loan.LoanRepaymentSchedule
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.loan.NewLoanTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.loan.NewLoanTemplate2
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.loan.NewLoanTemplate3
import com.google.gson.Gson

object LoansConverter {
    @TypeConverter
    fun fromConservativeLoanAccount(obj: ConservativeLoanAccount): String {
        val gson = Gson()
        return gson.toJson(obj)
    }

    @TypeConverter
    fun toConservativeLoanAccount(str: String): ConservativeLoanAccount {
        val gson = Gson()
        return gson.fromJson(str, ConservativeLoanAccount::class.java)
    }

    @TypeConverter
    fun fromDetailedLoanAccount(obj: DetailedLoanAccount): String {
        val gson = Gson()
        return gson.toJson(obj)
    }

    @TypeConverter
    fun toDetailedLoanAccount(str: String): DetailedLoanAccount {
        val gson = Gson()
        return gson.fromJson(str, DetailedLoanAccount::class.java)
    }

    @TypeConverter
    fun fromSchedule(obj: LoanRepaymentSchedule): String {
        val gson = Gson()
        return gson.toJson(obj)
    }

    @TypeConverter
    fun toSchedule(str: String): LoanRepaymentSchedule {
        val gson = Gson()
        return gson.fromJson(str, LoanRepaymentSchedule::class.java)
    }

    @TypeConverter
    fun fromTemplate(obj: NewLoanTemplate): String {
        val gson = Gson()
        return gson.toJson(obj)
    }

    @TypeConverter
    fun toTemplate(str: String): NewLoanTemplate {
        val gson = Gson()
        return gson.fromJson(str, NewLoanTemplate::class.java)
    }

    @TypeConverter
    fun fromNewLoanTemplate2(obj: NewLoanTemplate2): String {
        val gson = Gson()
        return gson.toJson(obj)
    }

    @TypeConverter
    fun toNewLoanTemplate2(str: String): NewLoanTemplate2 {
        val gson = Gson()
        return gson.fromJson(str, NewLoanTemplate2::class.java)
    }

    @TypeConverter
    fun fromNewLoanTemplate3(obj: NewLoanTemplate3): String {
        val gson = Gson()
        return gson.toJson(obj)
    }

    @TypeConverter
    fun toNewLoanTemplate3(str: String): NewLoanTemplate3 {
        val gson = Gson()
        return gson.fromJson(str, NewLoanTemplate3::class.java)
    }
}