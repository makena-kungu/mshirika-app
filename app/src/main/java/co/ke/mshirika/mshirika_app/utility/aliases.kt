package co.ke.mshirika.mshirika_app.utility

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call

typealias mld<T> = MutableLiveData<T>
typealias ld<T> = LiveData<T>
typealias cMld<T> = mld<Call<T>>
typealias cld<T> = ld<Call<T>>
typealias lMld<T> = MutableLiveData<List<T>>
typealias lLd<T> = MutableLiveData<List<T>>