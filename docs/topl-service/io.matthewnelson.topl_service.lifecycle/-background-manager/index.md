[topl-service](../../index.md) / [io.matthewnelson.topl_service.lifecycle](../index.md) / [BackgroundManager](./index.md)

# BackgroundManager

`class BackgroundManager : `[`ServiceConsts`](../../io.matthewnelson.topl_service.util/-service-consts/index.md)`, LifecycleObserver` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/lifecycle/BackgroundManager.kt#L125)

When your application is sent to the background (the Recent App's tray or lock screen), the
chosen [BackgroundManager.Builder.Policy](-builder/-policy/index.md) will be executed after the number of seconds you've
declared.

If brought back into the foreground by the user:

* **Before Policy execution**: Execution is canceled. If [BaseService.lastAcceptedServiceAction](#)
was **not** [ServiceConsts.ServiceActionName.STOP](#), a startService call is made to ensure it's
started.

* **After Policy execution**: If [BaseService.lastAcceptedServiceAction](#)
was **not** [ServiceConsts.ServiceActionName.STOP](#), a startService call is made to ensure it's
started.

* See [BaseService.updateLastAcceptedServiceAction](#) and [TorService.onTaskRemoved](#) for
more information.

While your application is in the foreground the only way to stop the service is by
calling [io.matthewnelson.topl_service.TorServiceController.stopTor](../../io.matthewnelson.topl_service/-tor-service-controller/stop-tor.md), or via the
[io.matthewnelson.topl_service.notification.ServiceNotification](../../io.matthewnelson.topl_service.notification/-service-notification/index.md) Action (if enabled);
The OS will not kill a service started using `Context.startService` &amp;
`Context.bindService` (how [TorService](#) is started) while in the foreground.

When the user sends your application to the Recent App's tray though, to recoup resources
the OS will kill your app after being idle for a period of time (random AF... typically
0.75m to 1.25m). This is not an issue if the user removes the task before the OS
kills the app, as Tor will be able to shutdown properly and the service will stop.

This is where Services get sketchy (especially when trying to implement an always
running service for networking), and is the purpose of the [BackgroundManager](./index.md) class.

This class starts your chosen [BackgroundManager.Builder.Policy](-builder/-policy/index.md) as soon as your
application is sent to the background, waits for the time you declared, and then executes.

See the [BackgroundManager.Builder](-builder/index.md) for more detail.

### Parameters

`policy` - The chosen [ServiceConsts.BackgroundPolicy](#) to be executed.

`executionDelay` - Length of time before the policy gets executed *after* the application
is sent to the background.

`serviceClass` - The Service class being managed

`bindServiceFlag` - The flag to be used when binding to the service

**See Also**

[io.matthewnelson.topl_service.service.components.binding.BaseServiceBinder.executeBackgroundPolicyJob](#)

[io.matthewnelson.topl_service.service.components.binding.BaseServiceBinder.cancelExecuteBackgroundPolicyJob](#)

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | This [BackgroundManager.Builder](-builder/index.md) sets how you want the service to operate while your app is in the background (the Recent App's tray or lock screen), such that things run reliably based off of your application's needs.`class Builder` |
