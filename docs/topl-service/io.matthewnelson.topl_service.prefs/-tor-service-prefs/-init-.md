[topl-service](../../index.md) / [io.matthewnelson.topl_service.prefs](../index.md) / [TorServicePrefs](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`TorServicePrefs(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`)`

This class provides a standardized way for library users to change settings used
by [io.matthewnelson.topl_service.service.TorService](#) such that the values expressed
as default [io.matthewnelson.topl_core_base.TorSettings](http://FIX_DOKKA_LINKS/topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) when initializing things via
the [io.matthewnelson.topl_service.TorServiceController.Builder](../../io.matthewnelson.topl_service/-tor-service-controller/-builder/index.md) can be updated. The
values saved to [TorServicePrefs](index.md) are always preferred over the defaults declared.

See [io.matthewnelson.topl_service.onionproxy.ServiceTorSettings](#)

