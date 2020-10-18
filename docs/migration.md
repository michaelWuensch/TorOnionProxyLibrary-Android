# Migration

## Migrating from 1.0.0 to 2.0.0
 - Your Application's `TorSettings` class needs to now extend
 [ApplicationDefaultTorSettings](./topl-service-base/io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md)
 - Remove and re-import class paths, as all non-Builder classes were moved to the
 `topl-service-base` module
 - The `topl-core-base` dependency is no longer required as it is automatically provided with
 `topl-service`. If you have a `Tor` module, other module's depending on it need only
 import the `topl-service-base` module now as all visible classes/abstractions have been moved there.
 - The `TorServiceController.getServiceTorSettings` no longer requires context