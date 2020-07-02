package io.matthewnelson.topl_core.broadcaster

import io.matthewnelson.topl_core.OnionProxyContext
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_core.listener.BaseEventListener
import io.matthewnelson.topl_core.util.TorInstaller
import io.matthewnelson.topl_core_base.EventBroadcaster

/**
 * Just a helper class for broadcasting/logging.
 *
 * */
internal class BroadcastLoggerHelper(
    private val onionProxyManager: OnionProxyManager,
    private val eventBroadcaster: EventBroadcaster,
    private val buildConfigDebug: Boolean
) {

    private val broadcastLoggerList = mutableListOf<BroadcastLogger>()

    init {
        onionProxyManager.onionProxyContext.initBroadcastLogger(
            createBroadcastLogger(OnionProxyContext::class.java)
        )
        onionProxyManager.torInstaller.initBroadcastLogger(
            createBroadcastLogger(TorInstaller::class.java)
        )
        onionProxyManager.eventListener.initBroadcastLogger(
            createBroadcastLogger(BaseEventListener::class.java)
        )
    }

    /**
     * Necessary such that we're not querying `topl-service:TorServicePrefs` every second.
     * This gets called automatically at every [OnionProxyManager.start]. Can be called at
     * any time whether Tor's State is ON or OFF.
     * */
    fun refreshBroadcastLoggersHasDebugLogsVar() {
        val hasDebugLogs = onionProxyManager.torSettings.hasDebugLogs
        try {
            if (broadcastLoggerList.size > 0) {
                broadcastLoggerList.forEach {
                    it.updateHasDebugLogs(hasDebugLogs)
                }
            }
        } catch (e: Exception) {}
    }

    /**
     * Helper method for instantiating a [BroadcastLogger] for your class with the values
     * [OnionProxyManager] has been initialized with.
     *
     * @param [clazz] Class<*> - For initializing [BroadcastLogger.TAG] with your class' name.
     * */
    fun createBroadcastLogger(clazz: Class<*>): BroadcastLogger =
        createBroadcastLogger(clazz.simpleName)

    /**
     * Helper method for instantiating a [BroadcastLogger] for your class with the values
     * [OnionProxyManager] has been initialized with.
     *
     * @param [tagName] String - For initialize [BroadcastLogger.TAG].
     * */
    fun createBroadcastLogger(tagName: String): BroadcastLogger {
        var bl: BroadcastLogger? = checkIfBroadcastLoggerExists(tagName)
        if (bl == null) {
            bl = BroadcastLogger(
                tagName,
                eventBroadcaster,
                buildConfigDebug,
                onionProxyManager.torSettings.hasDebugLogs
            )
            broadcastLoggerList.add(bl)
        }
        return bl
    }

    private fun checkIfBroadcastLoggerExists(tagName: String): BroadcastLogger? {
        return try {
            if (broadcastLoggerList.size > 0) {
                broadcastLoggerList.forEach {
                    if (it.TAG == tagName) {
                        return it
                    }
                }
            }
            null
        } catch (e: Exception) {
            null
        }
    }

}