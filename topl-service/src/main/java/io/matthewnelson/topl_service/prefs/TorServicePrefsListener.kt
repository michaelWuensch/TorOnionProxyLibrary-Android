/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
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
        onionProxyManager.getBroadcastLogger(TorServicePrefsListener::class.java)


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