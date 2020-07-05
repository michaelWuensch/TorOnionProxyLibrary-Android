package io.matthewnelson.topl_core.broadcaster

import android.util.Log
import io.matthewnelson.topl_core.util.CoreConsts
import io.matthewnelson.topl_core_base.EventBroadcaster
import io.matthewnelson.topl_core_base.TorSettings

/**
 * This class is for standardizing broadcast messages across all classes for this Library.
 * Debugging is important while hacking on TOPL-Android, but those Logcat messages
 * should **never** make it to a release build.
 *
 * To enable Logcat messages, [buildConfigDebug] must be `true` (w/e you have sent
 * [io.matthewnelson.topl_core.OnionProxyManager] upon instantiation), and
 * [TorSettings.hasDebugLogs] must also be `true`. This way if your implementation of the
 * Library is causing problems in your App you need only set [TorSettings.hasDebugLogs] to
 * `true` for a Debug build of your App.
 *
 * See helper method [io.matthewnelson.topl_core.OnionProxyManager.createBroadcastLogger] to
 * instantiate.
 *
 * @param [TAG] Typically, the class name, but able to be set to whatever you wish
 * @param [eventBroadcaster] For broadcasting the info
 * @param [buildConfigDebug] To enable/disable Logcat messages
 * @param [hasDebugLogs] To switch debug logs on/off, as well as Logcat messages on Debug builds.
 * */
class BroadcastLogger internal constructor(
    val TAG: String,
    val eventBroadcaster: EventBroadcaster,
    private val buildConfigDebug: Boolean,
    hasDebugLogs: Boolean
): CoreConsts() {

    @Volatile
    internal var hasDebugLogs: Boolean = hasDebugLogs
        private set

    /**
     * See [io.matthewnelson.topl_core.broadcaster.BroadcastLoggerHelper.refreshBroadcastLoggersHasDebugLogsVar]
     * */
    internal fun updateHasDebugLogs(hasDebugLogs: Boolean) {
        this.hasDebugLogs = hasDebugLogs
    }

    private fun toLogcat(): Boolean =
        hasDebugLogs && buildConfigDebug

    /**
     * Will only broadcast if [hasDebugLogs] is on.
     * */
    fun debug(msg: String) {
        if (!hasDebugLogs) return
        eventBroadcaster.broadcastDebug("${BroadcastType.DEBUG}|$TAG|$msg")
        if (!buildConfigDebug) return
        Log.d(TAG, msg)
    }

    fun exception(e: Exception) {
        eventBroadcaster.broadcastException("${BroadcastType.EXCEPTION}|$TAG|${e.message}", e)
        if (!toLogcat()) return
        Log.e(TAG, e.message, e)

    }

    fun notice(msg: String) {
        eventBroadcaster.broadcastNotice("${BroadcastType.NOTICE}|$TAG|$msg")
        if (!toLogcat()) return
        Log.i(TAG, msg)
    }

    fun warn(msg: String) {
        eventBroadcaster.broadcastNotice("${BroadcastType.WARN}|$TAG|$msg")
        if (!toLogcat()) return
        Log.w(TAG, msg)
    }

    fun error(msg: String) {
        eventBroadcaster.broadcastNotice("${BroadcastType.ERROR}|$TAG|$msg")
        if (!toLogcat()) return
        Log.e(TAG, msg)
    }

    fun torState(@TorState state: String, @TorNetworkState networkState: String) {
        eventBroadcaster.broadcastTorState(state, networkState)
    }

}