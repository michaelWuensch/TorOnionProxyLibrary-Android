package io.matthewnelson.sampleapp.samplecode

import android.app.Application
import android.content.Context
import androidx.core.app.NotificationCompat
import io.matthewnelson.sampleapp.*
import io.matthewnelson.topl_core_base.EventBroadcaster
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_service.TorServiceController
import java.io.File

/**
 * This class is for sample code that is to be used while creating Dokka Docs/MkDocs.
 * @suppress
 * */
object SampleCode {

    /**
     * Contains all possible options for initializing [TorServiceController.Builder]
     * */
    fun setupTorServices(
        application: Application,
        eventBroadcaster: EventBroadcaster,
        torConfigFiles: TorConfigFiles
    ) {
//        fun setupTorServices(
//            application: Application,
//            eventBroadcaster: EventBroadcaster,
//            torConfigFiles: TorConfigFiles
//        ) {

            TorServiceController.Builder(
                application = application,
                buildConfigVersionCode = BuildConfig.VERSION_CODE,
                torSettings = MyTorSettings(),

                // These should live somewhere in your project application's assets directory
                geoipAssetPath = "common/geoip",
                geoip6AssetPath = "common/geoip6"
            )

                .setBuildConfigDebug(BuildConfig.DEBUG)
                .setEventBroadcaster(eventBroadcaster)
                .useCustomTorConfigFiles(torConfigFiles)

                // Notification customization
                .customizeNotification(
                    channelName = "TorService Channel",
                    channelDescription = "Tor Channel",
                    channelID = "My Sample Application",
                    notificationID = 615
                )
                .setActivityToBeOpenedOnTap(
                    clazz = MainActivity::class.java,
                    intentExtrasKey = null,
                    intentExtras = null,
                    intentRequestCode = null
                )
                .setImageTorNetworkingEnabled(R.drawable.tor_stat_network_enabled)
                .setImageTorNetworkingDisabled(R.drawable.tor_stat_network_disabled)
                .setImageTorDataTransfer(R.drawable.tor_stat_network_dataxfer)
                .setImageTorErrors(R.drawable.tor_stat_notifyerr)
                .setCustomColor(R.color.tor_service_white)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .enableTorRestartButton(enable = true)
                .enableTorStopButton(enable = true)
                .showNotification(show = true)

                // Will return a Builder object to continue with non-notification related options
                .applyNotificationSettings()

                .build()
//        }
    }

    fun customTorConfigFilesSetup(context: Context): TorConfigFiles {
//        fun customTorConfigFilesSetup(context: Context): TorConfigFiles {

            // This is modifying the directory hierarchy from TorService's
            // default setup. For example, if you are using binaries for Tor that
            // are named differently that that expressed in TorConfigFiles.createConfig()

            // Post Android API 28 requires that executable files be contained in your
            // application's data/app directory, as they can no longer execute from data/data.
            val installDir = File(context.applicationInfo.nativeLibraryDir)

            // Will create a directory within your application's data/data dir
            val configDir = context.getDir("torservice", Context.MODE_PRIVATE)

            val builder = TorConfigFiles.Builder(installDir, configDir)

            // Customize the tor executable file name. Requires that the executable file
            // be in your project's src/main/jniLibs directory. If you are getting your
            // executable files via a dependency, be sure to consult that Libraries documentation.
            builder.torExecutable(File(installDir, "libtor.so"))

            // customize further via the builder methods...

            return builder.build()
//        }
    }
}