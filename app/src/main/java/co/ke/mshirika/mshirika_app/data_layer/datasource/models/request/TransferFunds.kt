package co.ke.mshirika.mshirika_app.data_layer.datasource.models.request

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil
import com.google.gson.annotations.Expose

@Keep
@Entity(tableName = "transfers_funds_creation_table")
data class TransferFunds constructor(
    @PrimaryKey(autoGenerate = true) @Expose(serialize = false)
    val id: Int,
    val toOfficeId: Int,
    val toClientId: Int,
    val toAccountType: Int,
    val toAccountId: Int,
    val transferAmount: String,
    val transferDate: String,
    val transferDescription: String,
    val dateFormat: String = DateUtil.DATE_FORMAT,
    val locale: String = "en",
    val fromAccountId: String,
    val fromAccountType: String,
    val fromClientId: Int,
    val fromOfficeId: Int
) {
    constructor(
        toOfficeId: Int,
        toClientId: Int,
        toAccountType: Int,
        toAccountId: Int,
        transferAmount: String,
        transferDate: String,
        transferDescription: String,
        dateFormat: String = DateUtil.DATE_FORMAT,
        locale: String = "en",
        fromAccountId: String,
        fromAccountType: String,
        fromClientId: Int,
        fromOfficeId: Int
    ) : this(
        0,
        toOfficeId,
        toClientId,
        toAccountType,
        toAccountId,
        transferAmount,
        transferDate,
        transferDescription,
        dateFormat,
        locale,
        fromAccountId,
        fromAccountType,
        fromClientId,
        fromOfficeId
    )
}