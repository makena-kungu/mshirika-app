package co.ke.mshirika.mshirika_app.data.response


import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

class Address : ArrayList<Address.AddressItem>(){
    @Keep
    @Parcelize
    data class AddressItem(
        val fieldConfigurationId: Int,
        val entity: String,
        val subentity: String,
        val field: String,
        val isEnabled: Boolean,
        val isMandatory: Boolean,
        val validationRegex: String
    ) : Parcelable
}