[topl-service](../../../index.md) / [io.matthewnelson.topl_service.notification](../../index.md) / [ServiceNotification](../index.md) / [Builder](index.md) / [setContentIntentData](./set-content-intent-data.md)

# setContentIntentData

`fun setContentIntentData(bundle: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`?, requestCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?): Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/notification/ServiceNotification.kt#L211)

Default notification behaviour is to use the launch intent for your application
from Package Manager when a user taps the notification. Electing this method allows
for adding a request code and bundle to the PendingIntent.

**NOTE:** electing [setActivityToBeOpenedOnTap](set-activity-to-be-opened-on-tap.md) method behaviour takes precedent until
it is removed in a future release.

**NOTE:** If you do not elect this method or [setActivityToBeOpenedOnTap](set-activity-to-be-opened-on-tap.md) in your
[Builder](index.md), the notification's content intent is still set with a default [requestCode](set-content-intent-data.md#io.matthewnelson.topl_service.notification.ServiceNotification.Builder$setContentIntentData(android.os.Bundle, kotlin.Int)/requestCode)
value of 0 and null bundle.

### Parameters

`bundle` - Bundle to be sent to the Launch Activity

`requestCode` - Request Code to be used when launching the Activity