[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](index.md) / [dormantClientTimeout](./dormant-client-timeout.md)

# dormantClientTimeout

`abstract val dormantClientTimeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorSettings.kt#L162)

Adds to the torrc file "DormantClientTimeout  minutes"

Minimum value 10. Any value less than or equal to 9 will fall back to using the value of 10
when writing the config to the torrc file. Set `null` to disable

See [DEFAULT__DORMANT_CLIENT_TIMEOUT](-d-e-f-a-u-l-t__-d-o-r-m-a-n-t_-c-l-i-e-n-t_-t-i-m-e-o-u-t.md)

