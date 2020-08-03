[topl-core](../../index.md) / [io.matthewnelson.topl_core](../index.md) / [OnionProxyManager](index.md) / [signalNewNym](./signal-new-nym.md)

# signalNewNym

`@Synchronized suspend fun signalNewNym(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/OnionProxyManager.kt#L892)

Will signal for a NewNym, then broadcast [NEWNYM_SUCCESS_MESSAGE](-n-e-w-n-y-m_-s-u-c-c-e-s-s_-m-e-s-s-a-g-e.md) if successful.

Because there is no way to easily ascertain success, we need to check
see if we've been rate limited. Being rate limited means we were **not** successful
when signaling NEWNYM, so we don't want to broadcast the success message.

See [BaseEventListener](../../io.matthewnelson.topl_core.listener/-base-event-listener/index.md) for more information on how this is done via calling the
[BaseEventListener.beginWatchingNoticeMsgs](#) &amp; [BaseEventListener.doesNoticeMsgBufferContain](#)
methods.

If the [eventListener](#) you're instantiating [OnionProxyManager](index.md) with has it's
[BaseEventListener.noticeMsg](../../io.matthewnelson.topl_core.listener/-base-event-listener/notice-msg.md) being piped to the [EventBroadcaster.broadcastNotice](../../../topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/broadcast-notice.md),
you will receive the message of being rate limited.

