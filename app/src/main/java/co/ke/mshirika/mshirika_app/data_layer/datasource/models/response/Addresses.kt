package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response


import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

class Addresses : ArrayList<Addresses.Address>(){
    @Keep
    @Parcelize
    data class Address(
        val fieldConfigurationId: Int,
        val entity: String,
        val subentity: String,
        val field: String,
        val isEnabled: Boolean,
        val isMandatory: Boolean,
        val validationRegex: String
    ) : Parcelable
}