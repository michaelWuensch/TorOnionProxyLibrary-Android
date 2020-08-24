# Change Log

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