[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](index.md) / [setEventBroadcaster](./set-event-broadcaster.md)

# setEventBroadcaster

`fun setEventBroadcaster(eventBroadcaster: `[`TorServiceEventBroadcaster`](../../../io.matthewnelson.topl_service.service.components.onionproxy/-tor-service-event-broadcaster/index.md)`): Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L243)

Get broadcasts piped to your Application to do with them what you desire. What
you send this will live at [Companion.appEventBroadcaster](../app-event-broadcaster.md) for the remainder of
your application's lifecycle to refer to elsewhere in your App.

NOTE: You will, ofc, have to cast [Companion.appEventBroadcaster](../app-event-broadcaster.md) as whatever your
class actually is.

