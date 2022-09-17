package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.more

import androidx.lifecycle.viewModelScope
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.data_layer.datasource.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.data_layer.repositories.SmsRepo
import co.ke.mshirika.mshirika_app.ui_layer.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.plainText
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.resourceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmsViewModel @Inject constructor(
    private val repo: SmsRepo
) : MshirikaViewModel() {

    fun sendSms(clientId: Int, msg: String) {
        viewModelScope.launch(IO) {
            when (val result = repo.sendSms(clientId, msg)) {
                is Outcome.Error -> {
                    errorChannel.send(plainText(result.msg))
                }
                else -> {
                    successChannel.send(resourceText(R.string.message_sent))
                }
            }
        }
    }
}