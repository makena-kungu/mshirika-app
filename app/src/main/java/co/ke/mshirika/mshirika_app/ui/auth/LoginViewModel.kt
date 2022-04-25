package co.ke.mshirika.mshirika_app.ui.auth

import android.util.Log
import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.request.Login
import co.ke.mshirika.mshirika_app.data.response.Staff
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.remote.utils.resource
import co.ke.mshirika.mshirika_app.repositories.AuthRepo
import co.ke.mshirika.mshirika_app.ui.MshirikaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepo
) : MshirikaViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val auth = channelFlow {
        repo.auth.asSharedFlow().collectLatest { outcome ->
            Log.i(TAG, "authentication: $outcome")
            when (outcome) {
                is Outcome.Success -> {
                    outcome.data?.let { data -> send(data) }
                    false
                }
                is Outcome.Error -> {
                    outcome.resource(R.string.error_login)
                    false
                }
                is Outcome.Loading -> true
                else -> false
            }.also {
                loadingChannel.send(it)
            }
        }
    }

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