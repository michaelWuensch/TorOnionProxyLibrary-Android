[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](index.md) / [hasCookieAuthentication](./has-cookie-authentication.md)

# hasCookieAuthentication

`abstract val hasCookieAuthentication: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorSettings.kt#L256)

**Highly** recommended to be set to `true` for securing the ControlPort

Adds to the torrc file:

"CookieAuthentication 1"
"CookieAuthFile &lt;[TorConfigFiles.cookieAuthFile](../-tor-config-files/cookie-auth-file.md) path&gt;

**Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#CookieAuthentication
**Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#CookieAuthFile

See [DEFAULT__HAS_COOKIE_AUTHENTICATION](-d-e-f-a-u-l-t__-h-a-s_-c-o-o-k-i-e_-a-u-t-h-e-n-t-i-c-a-t-i-o-n.md)

