# Change Log

## Version 1.0.0-beta01 (2020-08-24)
 - Bug Fix: remove use of `=` operator from kotlin methods where `java.void` is expected as a return value.
 - Adds JvmOverloads/JvmStatic to methods/variables for making use of Library for Java users more convenient.
 - Bug Fix: implements try/catch block on `BaseService.startService` method if application calls it while in the background.
 - Adds new option for `BackgroundManager.Builder` to run the `Service` in the Foreground when the application is sent 
 to the background.
 - Exposes `ServiceTorSettings` class for easier construction of a settings screen by Library users.
     - Adds methods to `ServiceTorSettings` for quickly saving user settings to `TorServicePrefs`.
     - Adds a helper method in `TorServiceController.Companion` for quickly generating the class.
 - Adds option within `TorSettings` for setting `IsolationFlags` specific to the protocol being opened on a port.
 - Updates `TorServiceEventBroadcaster`'s broadcasting of ports to now broadcast a single class containing 
 all port information instead of individual methods for each port type.
 - Adds a delay to modifying of Tor config `DisableNetwork` on device connectivity loss such that
 intermittent disconnections are smoothed out w/o the network call failing
     - Also helps with inhibiting ports from changing if utilizing "auto".
 - Updates Dependency versions [88469d7](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/commit/88469d7028020097624cc7cad9b1a616088d07e8)
 - Bug Fix: Dokka Docs generated with failed links caused by not accounting for directory depth of the file.

## Version 1.0.0-alpha02 (2020-08-03)
 - API breaking changes were made to `TorServiceController.Builder`. See the following to update:
     - [TorServiceController.Builder](./topl-service/io.matthewnelson.topl_service/-tor-service-controller/-builder/index.md)
     - [TorServiceEventBroadcaster](./topl-service/io.matthewnelson.topl_service.service.components.onionproxy.model/-tor-service-event-broadcaster/index.md)
 - Added ability to hide the notification
     - NOTE: Will be shown when user swipes the application out of recent's tray (required).
 - Service now starts via `Context.startService` + `Context.bindService` instead of starting
 in the foreground.
 - Added a queue for processing of commands, ie. `ServiceActions`, for improved response time to 
 API interactions.
 - Added management of the service for when the application is sent to the background.
 - Added APIs for retrieving the `TorConfigFiles` and `TorSettings` used by `topl-service`
 - Various bug fixes and code clean up

## Version 1.0.0-alpha01 (2020-07-18)
 - Initial Release