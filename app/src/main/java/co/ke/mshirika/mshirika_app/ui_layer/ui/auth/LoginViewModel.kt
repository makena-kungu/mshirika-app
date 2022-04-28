package co.ke.mshirika.mshirika_app.ui_layer.ui.auth

import android.util.Log
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.data_layer.remote.models.request.Login
import co.ke.mshirika.mshirika_app.data_layer.remote.models.response.Staff
import co.ke.mshirika.mshirika_app.data_layer.repositories.AuthRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepo
) : MshirikaViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val auth = channelFlow {
        repo.auth.collectLatest { outcome ->
            Log.i(TAG, "authentication: $outcome")
            outcome.stateHandler {
                trySend(it)
            }
        }
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    val loggedInState = repo.loggedInState
    val authKey = repo.authKey

    fun login(username: String, password: String) {
        viewModelScope.launch {
            repo.login(Login(username, password))
        }
    }

    fun cancel() {
        repo.cancel()
    }

    fun save(key: String) {
        viewModelScope.launch(IO) {
            //todo check if we'll need for the key to be saved before proceeding
            repo.save(key)
        }
    }

    fun save(staff: Staff) {
        viewModelScope.launch(IO) {
            repo.save(staff)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}