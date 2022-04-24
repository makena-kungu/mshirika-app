package co.ke.mshirika.mshirika_app.ui.auth

import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data.request.Login
import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.remote.utils.resource
import co.ke.mshirika.mshirika_app.repositories.AuthRepo
import co.ke.mshirika.mshirika_app.ui.MshirikaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepo
) : MshirikaViewModel() {
    val auth = flow {
        repo.auth.asSharedFlow().collectLatest { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    outcome.data?.let { data -> emit(data) }
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
}