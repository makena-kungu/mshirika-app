package co.ke.mshirika.mshirika_app.remote.response.roots

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class Feedback<T : Parcelable>(
    open val pageItems: List<T>,
    open val totalFilteredRecords: Int
) : Parcelable
