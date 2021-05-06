# Change Log

## Version 2.1.1 (2021-05-06)
 - Bump dependencies

## Version 2.1.0 (2021-01-30)
 - New Features Added:
     - `topl-service:TorService` Lifecycle Events are now broadcast to `TorServiceEventBroadcaster`
     - `topl-service-base:ServiceExecutionHooks` have been implemented such that library users can
     leverage suspension functions at key points of `TorService`'s operation, and allow for synchronous
     code execution.
     - Moved `topl-service:ServiceActionName` String definitions to `topl-service-base` to expose them
     such that library users can utilize them in their implementation of `TorServiceEventBroadcaster`
 - Bug Fixes:
     - Kotlin internal visibility modifier is public from Java code.
     See Issue <a href="https://github.com/05nelsonm/TorOnionProxyLibrary-Android/issues/100" target="_blank">100</a>
     - Duplicate v3 Client Authentication files were inhibiting Tor from starting properly.
     See Issue <a href="https://github.com/05nelsonm/TorOnionProxyLibrary-Android/issues/105" target="_blank">105</a>
     - Tor notice messages were modified between 0.4.4.0 and 0.4.5.4-rc, which caused
     `topl-service:ServiceEventBroadcaster`'s notice message filter to miss broadcasting of ports.
     See Issue <a href="https://github.com/05nelsonm/TorOnionProxyLibrary-Android/issues/110" target="_blank">110</a>
 - Dependencies were bumped to latest stable releases.

## Version 2.0.2 (2020-10-19)
 - Bug Fix: Notifications not showing on API 25 and below [12abbf7](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/commit/12abbf7faec781cb2c9b0a9babf3fe0e3d6cb352)
 - Deprecates `ServiceNotification.Builder.setContentIntentData` in favor of
 `ServiceNotification.Builder.setContentIntent` via Library implementer providing the needed PendingIntent
 - Removes Notification Action icons

## Version 2.0.1 (2020-10-18)
 - Deprecates the `ServiceNotification.Builder.setActivityToBeOpenedOnTap` method in favor of using
 the new `ServiceNotification.Builder.setContentIntentData` to mitigate multiple activities from
 being opened.

## Version 2.0.0 (2020-10-18)
 - Adds support for Version 3 Hidden Service Client Authentication
     - The `V3ClientAuthManager` class can be obtained from `TorServiceController.getV3ClientAuthManager`
     after Builder initialization, which facilitates easily adding private keys to Tor's `ClientAuthDir`.
 - Adds better support for multi-module projects by moving `topl-service`'s public
 classes/abstractions to a separate module, `topl-service-base`.
 - TorService now broadcasts as a `NOTICE` via the EventBroadcasters when onTaskRemoved occurred,
 instead of being broadcast as `DEBUG` (which required TorSettings.hasDebugLogs to be set to true).
     - Can now listen for it in the implementing application's `TorServiceEventBroadcaster`.
 - See [Migrations](./migration.md) for details on how to migrate from `1.0.0` to `2.0.0`

## Version 1.0.0-beta02 (2020-10-08)
 - Bug Fix: Service re-binding when application sent to background inhibiting call to stopSelf in 
 some instances [a544c73](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/pull/85/commits/a544c73a7c28211c75063df6af30001f2ec1c071)

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
     - [TorServiceEventBroadcaster](./topl-service-base/io.matthewnelson.topl_service_base/-tor-service-event-broadcaster/index.md)
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