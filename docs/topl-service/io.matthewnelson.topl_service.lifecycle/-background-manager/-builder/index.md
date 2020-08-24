[topl-service](../../../index.md) / [io.matthewnelson.topl_service.lifecycle](../../index.md) / [BackgroundManager](../index.md) / [Builder](./index.md)

# Builder

`class Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/lifecycle/BackgroundManager.kt#L151)

This [BackgroundManager.Builder](./index.md) sets how you want the service to operate while your
app is in the background (the Recent App's tray or lock screen), such that things run
reliably based off of your application's needs.

When your application is brought back into the foreground your [Policy](-policy.md) is canceled
and, if [BaseService.lastAcceptedServiceAction](#) was **not** to Stop the service, a
startup command is issued to bring it back to the started state no matter if it is still
running or not.

``` kotlin
//  private fun generateBackgroundManagerPolicy(): BackgroundManager.Builder.Policy {
        return BackgroundManager.Builder()

            // All available options present. Only 1 is able to be chosen.
            .respectResourcesWhileInBackground(secondsFrom5To45 = 20)
            //.runServiceInForeground(killAppIfTaskIsRemoved = true)
//  }
```

### Types

| Name | Summary |
|---|---|
| [Policy](-policy.md) | Holds the chosen policy to be built in [io.matthewnelson.topl_service.TorServiceController.Builder.build](../../../io.matthewnelson.topl_service/-tor-service-controller/-builder/build.md).`class Policy` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | This [BackgroundManager.Builder](./index.md) sets how you want the service to operate while your app is in the background (the Recent App's tray or lock screen), such that things run reliably based off of your application's needs.`Builder()` |

### Functions

| Name | Summary |
|---|---|
| [respectResourcesWhileInBackground](respect-resources-while-in-background.md) | Stops [TorService](#) after being in the background for the declared [secondsFrom5To45](respect-resources-while-in-background.md#io.matthewnelson.topl_service.lifecycle.BackgroundManager.Builder$respectResourcesWhileInBackground(kotlin.Int)/secondsFrom5To45).`fun respectResourcesWhileInBackground(secondsFrom5To45: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null): Policy` |
| [runServiceInForeground](run-service-in-foreground.md) | Electing this option will, when your application is sent to the background, immediately move [TorService](#) to the Foreground. If the user returns to your application, [TorService](#) will then be backgrounded.`fun runServiceInForeground(killAppIfTaskIsRemoved: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): Policy` |
