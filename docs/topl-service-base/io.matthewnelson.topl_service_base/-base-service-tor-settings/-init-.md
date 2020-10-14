[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`BaseServiceTorSettings(servicePrefs: `[`TorServicePrefs`](../-tor-service-prefs/index.md)`, defaultTorSettings: `[`ApplicationDefaultTorSettings`](../-application-default-tor-settings/index.md)`)`

This class enables the querying of [TorServicePrefs](../-tor-service-prefs/index.md) to obtain values potentially set by
the User such that they are *preferred* over static/default values you may have set in
your [ApplicationDefaultTorSettings](../-application-default-tor-settings/index.md).

It enables the updating of settings in a standardized manner so library users can
simply instantiate [TorServicePrefs](../-tor-service-prefs/index.md), modify settings, and then call `restartTor` from the
`topl-service::TorServiceController` to have them applied to the Tor Process. It also makes
designing of a settings screen much easier for your application.

### Parameters

`servicePrefs` - [TorServicePrefs](../-tor-service-prefs/index.md) to query/save values to shared preferences

`defaultTorSettings` - Default values to fall back on if nothing is returned from
[TorServicePrefs](../-tor-service-prefs/index.md)