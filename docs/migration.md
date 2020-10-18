# Migration

## Migrating from 1.0.0 to 2.0.0
 - Your Application's `TorSettings` class needs to now extend
 [ApplicationDefaultTorSettings](./topl-service-base/io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md)
 - Remove and re-import class paths, as all non-Builder classes were moved to the
 `topl-service-base` module