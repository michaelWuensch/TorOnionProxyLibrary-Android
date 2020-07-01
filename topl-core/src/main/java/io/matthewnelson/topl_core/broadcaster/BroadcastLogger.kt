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
 * To enable Logcat messages, the `BuildConfig.DEBUG` must be `true` (w/e you have sent
 * [io.matthewnelson.topl_core.OnionProxyManager] upon instantiation), and
 * [TorSettings.hasDebugLogs] must also be `true`. This way if your implementation of the
 * Library is causing problems in your App you need only set [TorSettings.hasDebugLogs] to
 * `true` for a Debug build of your App.
 *
 * See helper method [io.matthewnelson.topl_core.OnionProxyManager.createBroadcastLogger].
 *
 * @param [eventBroadcaster] To broadcast after Logging (if enabled)
 * @param [buildConfigDebug] Use `BuildConfig.DEBUG`
 * @param [torSettings] For checking if `hasDebugLogs` is true
 * */
class BroadcastLogger(
    clazz: Class<*>,
    private val eventBroadcaster: EventBroadcaster,
    private val buildConfigDebug: Boolean,
    private val torSettings: TorSettings
): CoreConsts() {

    private val TAG = clazz.simpleName
    private fun toLogcat(): Boolean =
        buildConfigDebug && torSettings.hasDebugLogs

    fun debug(msg: String) {
        if (toLogcat())
            Log.d(TAG, msg)
        eventBroadcaster.broadcastDebug("${BroadcastType.DEBUG}|$TAG|$msg")
    }

    fun exception(e: Exception) {
        if (toLogcat())
            Log.e(TAG, e.message, e)
        eventBroadcaster.broadcastException("${BroadcastType.EXCEPTION}|$TAG|${e.message}", e)
    }

    fun notice(msg: String) {
        if (toLogcat())
            Log.i(TAG, msg)
        eventBroadcaster.broadcastNotice("${BroadcastType.NOTICE}|$TAG|$msg")
    }

    fun warn(msg: String) {
        if (toLogcat())
            Log.w(TAG, msg)
        eventBroadcaster.broadcastNotice("${BroadcastType.WARN}|$TAG|$msg")
    }

    fun torState(@TorState state: String, @TorNetworkState networkState: String) {
        if (toLogcat())
            Log.i(TAG, "$state|$networkState")
        eventBroadcaster.broadcastTorState(state, networkState)
    }

}