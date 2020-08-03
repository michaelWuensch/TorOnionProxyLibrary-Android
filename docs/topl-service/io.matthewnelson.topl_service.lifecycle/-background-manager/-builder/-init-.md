[topl-service](../../../index.md) / [io.matthewnelson.topl_service.lifecycle](../../index.md) / [BackgroundManager](../index.md) / [Builder](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Builder()`

This [BackgroundManager.Builder](index.md) sets how you want the service to operate while your
app is in the background (the Recent App's tray or lock screen), such that things run
reliably based off of your application's needs.

When your application is brought back into the foreground your [Policy](-policy/index.md) is canceled
and, if [BaseService.lastAcceptedServiceAction](#) was **not** to Stop the service, a
startup command is issued to bring it back to the started state no matter if it is still
running or not.

``` kotlin
//  private fun generateBackgroundManagerPolicy(): BackgroundManager.Builder.Policy {
        return BackgroundManager.Builder()
            .respectResourcesWhileInBackground(secondsFrom5To45 = 20)

//  }
```

