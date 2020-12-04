package io.matthewnelson.topl_service_base

import android.content.Context

/**
 * Set Hooks to be executed from TorService.
 * */
abstract class ServiceExecutionHooks {

    /**
     * Is executed from TorService.onCreate on Dispatchers.Main
     * */
    abstract suspend fun executeOnCreateTorService(context: Context)

    /**
     * Is executed from ServiceActionProcessor.processQueue on Dispatchers.Main
     * */
    abstract suspend fun executeBeforeStartTor(context: Context)

    /**
     * Is executed from ServiceActionProcessor.processQueue on Dispatchers.Main
     * */
    abstract suspend fun executeAfterStopTor(context: Context)
}