package co.ke.mshirika.mshirika_app.utility

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow

typealias mld<T> = MutableLiveData<T>
typealias ld<T> = LiveData<T>

typealias msf<T> = MutableStateFlow<T>
typealias mSharedF<T> = MutableStateFlow<T>
