[topl-service](../../../index.md) / [io.matthewnelson.topl_service.lifecycle](../../index.md) / [BackgroundManager](../index.md) / [Builder](index.md) / [respectResourcesWhileInBackground](./respect-resources-while-in-background.md)

# respectResourcesWhileInBackground

`@JvmOverloads fun respectResourcesWhileInBackground(secondsFrom5To45: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null): Policy` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/lifecycle/BackgroundManager.kt#L180)

Stops [TorService](#) after being in the background for the declared [secondsFrom5To45](respect-resources-while-in-background.md#io.matthewnelson.topl_service.lifecycle.BackgroundManager.Builder$respectResourcesWhileInBackground(kotlin.Int)/secondsFrom5To45).

Your application won't go through it's normal `Application.onCreate` process unless
it was killed, but [TorService](#) may have been stopped when the policy gets executed.

Electing this option ensures [TorService](#) gets restarted in a more reliable manner then
returning `Context.START_STICKY` in [TorService.onStartCommand](#). It also allows for
a proper shutdown of Tor prior to the service being stopped instead of it being
killed along with your application (which causes problems sometimes).

If killed by the OS then this gets garbage collected such that in the event
the user brings your application back into the foreground (after it had been killed),
this will be re-instantiated when going through `Application.onCreate` again, and
[TorService](#) started by however you have it implemented.

### Parameters

`secondsFrom5To45` - ? Seconds before the [Policy](-policy.md) is executed after the
Application goes to the background. Sending null will use the default (30s)

**Return**
[BackgroundManager.Builder.Policy](-policy.md) To use when initializing
[io.matthewnelson.topl_service.TorServiceController.Builder](../../../io.matthewnelson.topl_service/-tor-service-controller/-builder/index.md)

