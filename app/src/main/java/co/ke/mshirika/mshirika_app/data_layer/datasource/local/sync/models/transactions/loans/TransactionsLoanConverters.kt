package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.loans

import androidx.room.TypeConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.RepaymentResponse
import com.google.gson.Gson

object TransactionsLoanConverters {
    @TypeConverter
    fun toRepaymentTypes(string: String): RepaymentResponse {
        return Gson().fromJson(string, RepaymentResponse::class.java)
    }

    @TypeConverter
    fun fromRepaymentTypes(obj: RepaymentResponse): String {
        return Gson().toJson(obj)
    }
}