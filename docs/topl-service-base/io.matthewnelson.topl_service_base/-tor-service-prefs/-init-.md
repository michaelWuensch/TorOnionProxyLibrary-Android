[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [TorServicePrefs](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`TorServicePrefs(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`)`

This class provides a standardized way for library users to change settings used
by the `topl-service` module such that the values expressed as default
[io.matthewnelson.topl_service_base.ApplicationDefaultTorSettings](../-application-default-tor-settings/index.md) when initializing things
can be modified by the implementing application.

The values saved to [TorServicePrefs](index.md) are always preferred over the defaults declared
when initializing the `topl-service` module.

Restarting Tor is currently required for the new settings to take effect.

