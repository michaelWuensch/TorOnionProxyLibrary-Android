[topl-service](../../../index.md) / [io.matthewnelson.topl_service.lifecycle](../../index.md) / [BackgroundManager](../index.md) / [Builder](index.md) / [runServiceInForeground](./run-service-in-foreground.md)

# runServiceInForeground

`@JvmOverloads fun runServiceInForeground(killAppIfTaskIsRemoved: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): Policy` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/lifecycle/BackgroundManager.kt#L219)

Electing this option will, when your application is sent to the background, immediately
move [TorService](#) to the Foreground. If the user returns to your application,
[TorService](#) will then be backgrounded.

Some things to note about your application's behaviour with this option:

* If the user sends your app to the recent App's tray and then swipes it away,
[TorService.onTaskRemoved](#) will stop Tor, and then [TorService](#).
* Because of how shitty the Service APIs are, your application will *not* be
killed like one would expect, thus not going through `Application.onCreate` if the
user re-launches your application.
* In the event of being re-launched in the aforementioned state,
[applicationMovedToForeground](#) is called and Tor will be started again to match the
Service's State for which it was left, prior to "terminating" your application.
* Even while the Service has been properly stopped and everything cleaned up, your
application will continue running and not be killed (Again, Service APIs...).
* If [TorService](#) is stopped, and *then* your application is cleared from the
recent apps tray, your application will be killed.

### Parameters

`killAppIfTaskIsRemoved` - If set to `true`, your Application's Process will be
killed in [TorService.onDestroy](#) if the user removed the task from the recent app's
tray and has not returned to the application before [killAppProcess](#) is called.