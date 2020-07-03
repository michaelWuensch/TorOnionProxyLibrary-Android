package io.matthewnelson.topl_service.prefs

import android.content.SharedPreferences
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_core.broadcaster.BroadcastLogger
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyBoolean

/**
 * Listens to [TorServicePrefs] for changes such that while Tor is running, it can
 * query [onionProxyManager] to have it updated immediately (if the setting doesn't
 * require a restart).
 *
 * @param [torService] To instantiate [TorServicePrefs]
 * @param [onionProxyManager]
 * */
internal class TorServicePrefsListener(
    private val torService: TorService,
    private val onionProxyManager: OnionProxyManager
): SharedPreferences.OnSharedPreferenceChangeListener {

    private val torServicePrefs = TorServicePrefs(torService)
    private val broadcastLogger: BroadcastLogger =
        onionProxyManager.createBroadcastLogger(TorServicePrefsListener::class.java)


    // Register itself immediately upon instantiation.
    init {
        torServicePrefs.registerListener(this)
    }

    /**
     * Called from [io.matthewnelson.topl_service.service.TorService.onDestroy].
     * */
    fun unregister() {
        torServicePrefs.unregisterListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (!key.isNullOrEmpty()) {
            broadcastLogger.debug("$key was modified")
            when (key) {
                PrefKeyBoolean.HAS_DEBUG_LOGS -> {
                    // TODO: Think about doing something with local prefs such that it
                    //  turns Debugging off at every application start automatically?
                    //  .
                    //  Especially necessary if I switch Tor's debug output location from
                    //  SystemOut to log to a file (more secure).
                    //  .
                    //  Will need to create another class available to Library user
                    //  strictly for Tor logs if logging to a file, such that they can
                    //  easily query, read, and load them to views.
                    //  Maybe a `TorDebugLogHelper` class?
                    //  .
                    //  Will need some way of automatically clearing old log files, too.
                    if (!onionProxyManager.torStateMachine.isOff)
                        onionProxyManager.refreshBroadcastLoggersHasDebugLogsVar()
                }
            }
        }
    }

}