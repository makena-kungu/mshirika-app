package co.ke.mshirika.mshirika_app.utility.connectivity

import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException

object Connection {

    fun hasConnection(): Boolean =
        try {
            val socket = Socket()
            //Pinging the google servers to test the internet
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            false
        }
}