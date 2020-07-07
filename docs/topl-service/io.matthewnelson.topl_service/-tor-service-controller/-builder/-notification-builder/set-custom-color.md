[topl-service](../../../../index.md) / [io.matthewnelson.topl_service](../../../index.md) / [TorServiceController](../../index.md) / [Builder](../index.md) / [NotificationBuilder](index.md) / [setCustomColor](./set-custom-color.md)

# setCustomColor

`fun setCustomColor(@ColorRes colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, colorizeBackground: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): NotificationBuilder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L298)

Defaults to [R.color.tor_service_white](#)

The color you wish to display when Tor's network state is
[io.matthewnelson.topl_core_base.BaseConsts.TorNetworkState.ENABLED](../../../topl-core-base/io.matthewnelson.topl_core_base/-base-consts/-tor-network-state/-companion/-e-n-a-b-l-e-d.md). Note that
if [colorizeBackground](set-custom-color.md#io.matthewnelson.topl_service.TorServiceController.Builder.NotificationBuilder$setCustomColor(kotlin.Int, kotlin.Boolean)/colorizeBackground) is being passed a value of `true`, the notification will
always be that color where as if it is passed `false`, the icon &amp; action button
colors will change with Tor's network state.

See [Builder](../index.md) for code samples.

### Parameters

`colorRes` - Color resource id.

`colorizeBackground` - true = background is colorized, false = icon is colorized

**Return**
[NotificationBuilder](index.md)

