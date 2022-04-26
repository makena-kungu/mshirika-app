package co.ke.mshirika.mshirika_app.data_layer.remote.models.response.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    val code: String,
    val decimalPlaces: Int,
    val displayLabel: String,
    val displaySymbol: String,
    val inMultiplesOf: Int,
    val name: String,
    val nameCode: String
) : Parcelable