[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](index.md) / [setServiceExecutionHooks](./set-service-execution-hooks.md)

# setServiceExecutionHooks

`fun setServiceExecutionHooks(executionHooks: `[`ServiceExecutionHooks`](../../../..//topl-service-base/io.matthewnelson.topl_service_base/-service-execution-hooks/index.md)`): Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L260)

Implement and set hooks to be executed in [TorService.onCreate](#), and
[ServiceActionProcessor.processServiceAction](#) prior to starting of Tor, and
post stopping of Tor.

