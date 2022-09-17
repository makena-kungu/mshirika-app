package co.ke.mshirika.mshirika_app.data_layer.datasource.local.sync.models.transactions.clients_savings

import androidx.room.TypeConverter
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.transfer_funds.TransferFundsTemplate
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.transfer_funds.TransferFundsTemplateWithToClients
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.templates.transfer_funds.TransferFundsTemplateWithToClientsAccounts
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.response.ClientPaymentTemplate
import com.google.gson.Gson

object SavingsConverter {
    @TypeConverter
    fun toTemplate(str: String): TransferFundsTemplate {
        return Gson().fromJson(str, TransferFundsTemplate::class.java)
    }

    @TypeConverter
    fun fromTemplate(obj: TransferFundsTemplate): String {
        return Gson().toJson(obj)
    }

    @TypeConverter
    fun toPaymentTemplate(str: String): ClientPaymentTemplate {
        return Gson().fromJson(str, ClientPaymentTemplate::class.java)
    }

    @TypeConverter
    fun fromPaymentTemplate(obj: ClientPaymentTemplate): String {
        return Gson().toJson(obj)
    }

    @TypeConverter
    fun toTemplateClients(str: String): TransferFundsTemplateWithToClients {
        return Gson().fromJson(str, TransferFundsTemplateWithToClients::class.java)
    }

    @TypeConverter
    fun fromTemplateClients(obj: TransferFundsTemplateWithToClients): String {
        return Gson().toJson(obj)
    }

    @TypeConverter
    fun toTemplateClientsAccount(str: String): TransferFundsTemplateWithToClientsAccounts {
        return Gson().fromJson(str, TransferFundsTemplateWithToClientsAccounts::class.java)
    }

    @TypeConverter
    fun fromTemplateClientsAccount(obj: TransferFundsTemplateWithToClientsAccounts): String {
        return Gson().toJson(obj)
    }
}