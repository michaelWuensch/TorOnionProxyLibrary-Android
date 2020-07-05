package io.matthewnelson.topl_core.broadcaster

import io.matthewnelson.topl_core.OnionProxyContext
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_core.listener.BaseEventListener
import io.matthewnelson.topl_core.util.TorInstaller
import io.matthewnelson.topl_core_base.EventBroadcaster

/**
 * This class is for handling the instantiation of new [BroadcastLogger]s such that debugging
 * can be controlled in a more efficient/effective manner. It also handles initialization of
 * several other classes [BroadcastLogger]'s upon instantiation.
 * */
internal class BroadcastLoggerHelper(
    private val onionProxyManager: OnionProxyManager,
    private val eventBroadcaster: EventBroadcaster,
    private val buildConfigDebug: Boolean
) {

    private val broadcastLoggerList = mutableListOf<BroadcastLogger>()

    init {
        onionProxyManager.onionProxyContext.initBroadcastLogger(
            getBroadcastLogger(OnionProxyContext::class.java)
        )
        onionProxyManager.torInstaller.initBroadcastLogger(
            getBroadcastLogger(TorInstaller::class.java)
        )
        onionProxyManager.eventListener.initBroadcastLogger(
            getBroadcastLogger(BaseEventListener::class.java)
        )
    }

    /**
     * Necessary such that we're not querying `topl-service:TorServicePrefs` every second.
     * This gets called automatically at every [OnionProxyManager.start]. Can be called at
     * any time whether Tor's State is ON or OFF.
     * */
    fun refreshBroadcastLoggersHasDebugLogsVar() {
        if (broadcastLoggerList.size < 1) return
        val hasDebugLogs = onionProxyManager.torSettings.hasDebugLogs
        try {
            broadcastLoggerList.forEach {
                it.updateHasDebugLogs(hasDebugLogs)
            }
        } catch (e: Exception) {}
    }

    /**
     * Helper method for instantiating a [BroadcastLogger] for your class with the values
     * [OnionProxyManager] has been initialized with. If one with the same [BroadcastLogger.TAG]
     * exists in [broadcastLoggerList], that will be returned instead of creating a new one.
     *
     * @param [clazz] Class<*> - For initializing [BroadcastLogger.TAG] with your class' name.
     * */
    fun getBroadcastLogger(clazz: Class<*>): BroadcastLogger =
        getBroadcastLogger(clazz.simpleName)

    /**
     * Helper method for instantiating a [BroadcastLogger] for your class with the values
     * [OnionProxyManager] has been initialized with. If one with the same [BroadcastLogger.TAG]
     * exists in [broadcastLoggerList], that will be returned instead of creating a new one.
     *
     * @param [tagName] String - For initialize [BroadcastLogger.TAG].
     * */
    fun getBroadcastLogger(tagName: String): BroadcastLogger {
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
        if (broadcastLoggerList.size < 1) return null
        return try {
            broadcastLoggerList.forEach {
                if (it.TAG == tagName) {
                    return it
                }
            }
            null
        } catch (e: Exception) {
            null
        }
    }

}