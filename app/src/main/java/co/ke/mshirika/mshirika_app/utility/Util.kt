package co.ke.mshirika.mshirika_app.utility

import co.ke.mshirika.mshirika_app.remote.utils.Outcome
import co.ke.mshirika.mshirika_app.remote.utils.Outcome.*
import co.ke.mshirika.mshirika_app.ui.util.MshirikaViewModel
import co.ke.mshirika.mshirika_app.ui.util.dynamicText

object Util {
    fun headers(authKey: String) = mapOf(
        "Fineract-Platform-TenantId" to "default",
        "Authorization" to authKey
    )

    suspend inline fun <T, VM:MshirikaViewModel> Outcome<T>.stateHandler(
        viewModel: VM,
        success: Success<T>.()->Unit
    ) {
        stateHandler(viewModel, success) {

        }
    }

    suspend inline fun <T, VM : MshirikaViewModel> Outcome<T>.stateHandler(
        viewModel: VM,
        success: Success<T>.() -> Unit,
        noinline action: (String) -> Unit = {}
    ) {
        when (this) {
            is Empty -> false
            is Loading -> true
            is Error -> {
                viewModel.errorChannel.send(dynamicText(msg, action))
                false
            }
            is Success -> {
                success()
                false
            }
        }.also {
            viewModel.loadingChannel.send(it)
        }
    }
}