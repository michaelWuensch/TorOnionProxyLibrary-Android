[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`TorSettings()`

This class is for defining default values for your torrc file. Extend this class and define
your own settings.

Keep in mind that Orbot and TorBrowser are the 2 most widely used applications
using Tor, and to use settings that won't conflict (those settings are documented
as such, and contain further details).

[TorSettings.Companion](#) contains pretty standard default values which'll get you a Socks5 proxy
running, nothing more.

Would **highly recommend** reading up on what's what in the manual:

* https://2019.www.torproject.org/docs/tor-manual.html.en
