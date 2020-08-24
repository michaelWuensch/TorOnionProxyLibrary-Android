[topl-service](../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy.model](./index.md)

## Package io.matthewnelson.topl_service.service.components.onionproxy.model

### Types

| Name | Summary |
|---|---|
| [TorPortInfo](-tor-port-info/index.md) | Contains information regarding what ports Tor is operating on.`class TorPortInfo` |
| [TorServiceEventBroadcaster](-tor-service-event-broadcaster/index.md) | Adds broadcasting methods to the [EventBroadcaster](../..//topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) to update you with information about what addresses Tor is operating on. Very helpful when choosing "auto" in your [io.matthewnelson.topl_core_base.TorSettings](../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) to easily identify what addresses to use for making network calls, as well as being notified when Tor is ready to be used.`abstract class TorServiceEventBroadcaster : `[`EventBroadcaster`](../..//topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) |
