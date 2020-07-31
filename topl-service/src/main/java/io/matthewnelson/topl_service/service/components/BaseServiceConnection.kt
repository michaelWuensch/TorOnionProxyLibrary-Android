package io.matthewnelson.topl_service.service.components

import android.content.ServiceConnection

internal abstract class BaseServiceConnection: ServiceConnection {

    @Volatile
    var serviceBinder: TorServiceBinder? = null
        private set

    /**
     * Sets the reference to [TorServiceBinder] to `null` because
     * [onServiceDisconnected] is not always called on disconnect, as the name
     * suggests.
     * */
    fun clearServiceBinderReference() {
        serviceBinder = null
    }

    fun setServiceBinder(torServiceBinder: TorServiceBinder) {
        serviceBinder = torServiceBinder
    }
}