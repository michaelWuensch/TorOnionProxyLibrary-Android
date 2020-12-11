package io.matthewnelson.topl_service_base

import android.content.Context

/**
 * Set Hooks to be executed from TorService.
 * */
abstract class ServiceExecutionHooks {

    /**
     * Is executed from TorService.onCreate on Dispatchers.Default launched from
     * it's own coroutine.
     * */
    open suspend fun executeOnCreateTorService(context: Context) {}

    /**
     * Is executed from TorService.startTor on Dispatchers.IO
     *
     * **WARNING**: Indefinitely suspending the coroutine (ex. collecting
     * Flow or something) will inhibit the reset of the `startTor` method from
     * executing; perform those tasks from [executeOnCreateTorService].
     *
     * This is meant to provide synchronous execution prior to the
     * start of Tor.
     *
     * **NOTE**: Exceptions thrown from your implementation will inhibit starting
     * Tor and will be broadcast via the EventBroadcaster.
     * */
    open suspend fun executeBeforeStartTor(context: Context) {}

    /**
     * Is executed from TorService.stopTor on Dispatchers.IO
     *
     * **WARNING**: Indefinitely suspending the coroutine (ex. collecting
     * Flow or something) will inhibit the reset of the `stopTor` method from
     * executing; perform those tasks from [executeOnCreateTorService].
     *
     * This is meant to provide synchronous execution of code after Tor has been
     * stopped, prior to TorService being stopped.
     * */
    open suspend fun executeAfterStopTor(context: Context) {}
}