package co.ke.mshirika.mshirika_app.remote.response.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class Feedback<T : Parcelable>(
    open val pageItems: List<T>,
    open val totalFilteredRecords: Int
) : Parcelable,Respondent
