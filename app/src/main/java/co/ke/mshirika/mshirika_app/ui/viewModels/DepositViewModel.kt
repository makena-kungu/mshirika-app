package co.ke.mshirika.mshirika_app.ui.viewModels

import co.ke.mshirika.mshirika_app.utility.ld
import co.ke.mshirika.mshirika_app.utility.mld

class DepositViewModel {
    private val _paymentMode = mld<String>()

    private val paymentMode:ld<String> = _paymentMode

    fun paymentMode(selectedMode: String) {
        _paymentMode.postValue(selectedMode)
    }
}