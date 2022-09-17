package co.ke.mshirika.mshirika_app.ui_layer.ui.auth

import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.request.Login
import co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.Staff
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.repositories.AuthRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.plainText
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.resourceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepo
) : MshirikaViewModel() {

    fun login(username: String, password: String) {
        viewModelScope.launch {
            when (val result = repo.login(Login(username, password))) {
                is Outcome.Error -> errorChannel.send(plainText(result.msg))
                is Outcome.Success -> success(result.data)
                else -> {}
            }
        }
    }

    private suspend fun success(staff: Staff?) {
        if (staff == null) {
            errorChannel.send(resourceText(R.string.error_null_staff_data))
            return
        }

        val saved = save(staff)
        if (!saved) {
            errorChannel.send(resourceText(R.string.error_saving_key))
            return
        }

        successChannel.send(resourceText(R.string.login_successful))
    }

    private suspend fun save(staff: Staff) = repo.save(staff)

    companion object {
        private const val TAG = "LoginViewModel"
    }
}