package io.matthewnelson.topl_service_base

import android.content.Context

/**
 * Set Hooks to be executed from TorService.
 *
 * @sample [io.matthewnelson.sampleapp.topl_android.MyServiceExecutionHooks]
 * */
abstract class ServiceExecutionHooks {

    /**
     * Is executed from TorService.onCreate on Dispatchers.Default launched from
     * it's own coroutine.
     *
     * **NOTE**: Exceptions thrown from your implementation will be broadcast via
     * the EventBroadcaster on Dispatchers.Main.
     *
     * @sample [io.matthewnelson.sampleapp.topl_android.MyServiceExecutionHooks.executeOnCreateTorService]
     * */
    open suspend fun executeOnCreateTorService(context: Context) {}

    /**
     * Is executed from TorService.startTor on Dispatchers.IO
     *
     * This is meant to provide synchronous code execution prior to the
     * start of Tor, and will be executed every single time Tor is started (even on restarts).
     *
     * **WARNING**: Indefinitely suspending the coroutine (ex. collecting
     * Flow or something) will also indefinitely suspend the ServiceActionProcessor
     * which will inhibit processing of any other commands; perform those tasks
     * from [executeOnCreateTorService].
     *
     * **NOTE**: Exceptions thrown from your implementation will inhibit starting
     * Tor and will be broadcast via the EventBroadcaster on Dispatchers.Main.
     *
     * @sample [io.matthewnelson.sampleapp.topl_android.MyServiceExecutionHooks.executeBeforeStartTor]
     * */
    open suspend fun executeBeforeStartTor(context: Context) {}

    /**
     * Is executed from TorService.stopTor on Dispatchers.IO
     *
     * This is meant to provide synchronous code execution after Tor has been
     * stopped, and will be executed every single time Tor is stopped (even on restarts).
     *
     * **WARNING**: Indefinitely suspending the coroutine (ex. collecting
     * Flow or something) will also indefinitely suspend the ServiceActionProcessor
     * which will inhibit processing of any other commands; perform those tasks
     * from [executeOnCreateTorService].
     *
     * **NOTE**: Exceptions thrown from your implementation will be broadcast via
     * the EventBroadcaster on Dispatchers.Main.
     *
     * @sample [io.matthewnelson.sampleapp.topl_android.MyServiceExecutionHooks.executeAfterStopTor]
     * */
    open suspend fun executeAfterStopTor(context: Context) {}

    /**
     * Is executed from TorService.stopService on Dispatchers.IO
     *
     * This is meant to provide synchronous code execution prior to calling
     * stopSelf() on the service. When this is called for execution,
     * [executeOnStartCommandBeforeStartTor], will be allowed to execute again
     * if TorServiceController.startTor() is called prior to this method's completion.
     *
     * **WARNING**: Indefinitely suspending the coroutine (ex. collecting
     * Flow or something) will also indefinitely suspend the ServiceActionProcessor
     * which will inhibit processing of any other commands; perform those tasks
     * from [executeOnCreateTorService].
     *
     * **NOTE**: Exceptions thrown from your implementation will be broadcast via
     * the EventBroadcaster on Dispatchers.Main.
     *
     * @sample [io.matthewnelson.sampleapp.topl_android.MyServiceExecutionHooks.executeBeforeStoppingService]
     * */
    open suspend fun executeBeforeStoppingService(context: Context) {}

    /**
     * Is executed from TorService.onStartCommand on Dispatchers.Default. This works in
     * conjunction with [executeBeforeStoppingService] such that if
     * [executeBeforeStoppingService] is active, [executeOnStartCommandBeforeStartTor] will
     * suspend until that has been completed, then execute, then start Tor.
     *
     * This is meant to provide quasi one-time synchronous code execution at first start of
     * TorService, prior to starting Tor.
     *
     * Example usage:
     *
     *   If you want to query a DB for V3 Client Authorization Keys stored in
     *   an encrypted fashion, write them to disk prior to first start of Tor, then in
     *   [executeBeforeStoppingService] delete the private keys.
     *
     *   If the user (or some other process) executes TorServiceController.startTor()
     *   while [executeBeforeStoppingService] is active, TorService.stopSelf() be prevented
     *   from being called, execution of this method will suspend until
     *   [executeBeforeStoppingService] completes, then execute, then Tor will be started again.
     *
     * **WARNING**: Indefinitely suspending the coroutine (ex. collecting
     * Flow or something) will also indefinitely suspend the ServiceActionProcessor
     * which will inhibit processing of any other commands; perform those tasks
     * from [executeOnCreateTorService].
     *
     * **NOTE**: Exceptions thrown from your implementation will be broadcast via
     * the EventBroadcaster on Dispatchers.Main.
     *
     * @sample [io.matthewnelson.sampleapp.topl_android.MyServiceExecutionHooks.executeOnStartCommandBeforeStartTor]
     * */
    open suspend fun executeOnStartCommandBeforeStartTor(context: Context) {}
}