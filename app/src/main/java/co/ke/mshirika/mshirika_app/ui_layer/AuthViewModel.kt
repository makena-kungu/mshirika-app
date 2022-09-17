package co.ke.mshirika.mshirika_app.ui_layer

import androidx.lifecycle.ViewModel
import co.ke.mshirika.mshirika_app.data_layer.repositories.PreferencesStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    store: PreferencesStoreRepository
):ViewModel() {

    val isAuthenticated = store.isAuthed
}