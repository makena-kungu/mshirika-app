package co.ke.mshirika.mshirika_app.utility.connectivity

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkMonitor @Inject constructor(
    @ApplicationContext context: Context
) : LiveData<Boolean>() {
    //implement using the flow api
    private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkCallback by lazy {
        Callback()
    }
    private val validNetworks = mutableSetOf<Network>()
    var isOnline: Boolean
        get() {
            return value ?: false
        }
        set(value) {
            postValue(value)
        }

    override fun onActive() {
        super.onActive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            cm.registerDefaultNetworkCallback(networkCallback)
        else {
            NetworkRequest.Builder()
                .addCapability(NET_CAPABILITY_INTERNET)
                .build()
                .also {
                    cm.registerNetworkCallback(it, networkCallback)
                }
        }
    }

    override fun onInactive() {
        super.onInactive()
        cm.unregisterNetworkCallback(networkCallback)
    }

    private fun rerunValidityCheck() {
        isOnline = validNetworks.size > 0
    }

    inner class Callback : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            cm.getNetworkCapabilities(network)?.apply {
                if (!hasCapability(NET_CAPABILITY_INTERNET)) return

                CoroutineScope(IO + Job()).launch {
                    if (!Connection.hasConnection())
                        return@launch

                    withContext(Main) {
                        validNetworks += network
                        rerunValidityCheck()
                    }
                }
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            validNetworks -= network
            rerunValidityCheck()
        }
    }
}