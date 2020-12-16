package io.matthewnelson.topl_service_base

import android.content.Context

/**
 * Set Hooks to be executed from TorService.
 * */
abstract class ServiceExecutionHooks {

    /**
     * Is executed from TorService.onCreate on Dispatchers.Default launched from
     * it's own coroutine.
     *
     * **NOTE**: Exceptions thrown from your implementation will be broadcast via
     * the EventBroadcaster on Dispatchers.Main.
     * */
    open suspend fun executeOnCreateTorService(context: Context) {}

    /**
     * Is executed from TorService.startTor on Dispatchers.IO
     *
     * **WARNING**: Indefinitely suspending the coroutine (ex. collecting
     * Flow or something) will inhibit the rest of the `startTor` method from
     * executing; perform those tasks from [executeOnCreateTorService].
     *
     * This is meant to provide synchronous code execution prior to the
     * start of Tor, and will be executed every single time Tor is started (even on restarts).
     *
     * **NOTE**: Exceptions thrown from your implementation will inhibit starting
     * Tor and will be broadcast via the EventBroadcaster on Dispatchers.Main.
     * */
    open suspend fun executeBeforeStartTor(context: Context) {}

    /**
     * Is executed from TorService.stopTor on Dispatchers.IO
     *
     * **WARNING**: Indefinitely suspending the coroutine (ex. collecting
     * Flow or something) will inhibit the rest of the `stopTor` method from
     * executing; perform those tasks from [executeOnCreateTorService].
     *
     * This is meant to provide synchronous code execution after Tor has been
     * stopped, and will be executed every single time Tor is stopped (even on restarts).
     *
     * **NOTE**: Exceptions thrown from your implementation will be broadcast via
     * the EventBroadcaster on Dispatchers.Main.
     * */
    open suspend fun executeAfterStopTor(context: Context) {}

    /**
     * Is executed from TorService.stopService on Dispatchers.IO
     *
     * **WARNING**: Indefinitely suspending the coroutine (ex. collecting
     * Flow or something) will inhibit the rest of the `stopService` method from
     * executing.
     *
     * This is meant to provide synchronous code execution prior to calling
     * stopSelf() on the service.
     *
     * **NOTE**: Exceptions thrown from your implementation will be broadcast via
     * the EventBroadcaster on Dispatchers.Main.
     * */
    open suspend fun executeBeforeStoppingService(context: Context) {}
}