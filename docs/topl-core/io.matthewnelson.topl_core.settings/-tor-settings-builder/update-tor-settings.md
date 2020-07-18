[topl-core](../../index.md) / [io.matthewnelson.topl_core.settings](../index.md) / [TorSettingsBuilder](index.md) / [updateTorSettings](./update-tor-settings.md)

# updateTorSettings

`fun updateTorSettings(): `[`TorSettingsBuilder`](index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/settings/TorSettingsBuilder.kt#L141)

Updates the buffer for all methods annotated with [SettingsConfig](#). You still need
to call [finishAndReturnString](finish-and-return-string.md) and then write the returned String to your
[io.matthewnelson.topl_core_base.TorConfigFiles.torrcFile](http://FIX_DOKKA_LINKS/topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/torrc-file.md).

Alternatively, call [finishAndWriteToTorrcFile](finish-and-write-to-torrc-file.md), it's up to you.

### Exceptions

`SecurityException` - If denied access to the class

`IllegalAccessException` - see [java.lang.reflect.Method.invoke](https://docs.oracle.com/javase/6/docs/api/java/lang/reflect/Method.html#invoke(java.lang.Object, java.lang.Object...))

`IllegalArgumentException` - see [java.lang.reflect.Method.invoke](https://docs.oracle.com/javase/6/docs/api/java/lang/reflect/Method.html#invoke(java.lang.Object, java.lang.Object...))

`InvocationTargetException` - see [java.lang.reflect.Method.invoke](https://docs.oracle.com/javase/6/docs/api/java/lang/reflect/Method.html#invoke(java.lang.Object, java.lang.Object...))

`NullPointerException` - see [java.lang.reflect.Method.invoke](https://docs.oracle.com/javase/6/docs/api/java/lang/reflect/Method.html#invoke(java.lang.Object, java.lang.Object...))

`ExceptionInInitializerError` -

see [java.lang.reflect.Method.invoke](https://docs.oracle.com/javase/6/docs/api/java/lang/reflect/Method.html#invoke(java.lang.Object, java.lang.Object...))



TODO: Replace reflection.......... gross.

