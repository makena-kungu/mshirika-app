package co.ke.mshirika.mshirika_app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data.request.Login
import co.ke.mshirika.mshirika_app.repositories.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepo
) :
    ViewModel() {
    val auth = repo.auth.asSharedFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            repo.login(Login(username, password))
        }
    }
}