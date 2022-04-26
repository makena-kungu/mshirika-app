package co.ke.mshirika.mshirika_app.data_layer.remote.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class Feedback<T : Parcelable>(
    open val pageItems: List<T>,
    open val totalFilteredRecords: Int
) : Respondent
