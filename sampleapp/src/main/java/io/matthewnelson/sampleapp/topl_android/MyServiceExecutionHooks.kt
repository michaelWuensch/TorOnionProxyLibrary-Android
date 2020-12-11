package io.matthewnelson.sampleapp.topl_android

import android.content.Context
import io.matthewnelson.topl_service_base.ServiceExecutionHooks

class MyServiceExecutionHooks: ServiceExecutionHooks() {

    override suspend fun executeOnCreateTorService(context: Context) {}

    override suspend fun executeBeforeStartTor(context: Context) {}

    override suspend fun executeAfterStopTor(context: Context) {}
}