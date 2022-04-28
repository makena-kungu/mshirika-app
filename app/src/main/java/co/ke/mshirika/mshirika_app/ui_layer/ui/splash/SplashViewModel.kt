package co.ke.mshirika.mshirika_app.ui_layer.ui.splash

import androidx.lifecycle.ViewModel
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    pref: PreferencesStoreRepository
) : ViewModel() {
    val auth = pref.authKey
}