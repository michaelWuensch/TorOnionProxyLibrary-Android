package io.matthewnelson.sampleapp.ui.fragments.settings.library

import androidx.core.app.NotificationCompat
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.sampleapp.BuildConfig
import io.matthewnelson.sampleapp.R

object LibraryPrefs {

    // Notification Keys
    const val NOTIFICATION_VISIBILITY = "NOTIFICATION_VISIBILITY"
    const val NOTIFICATION_COLOR_RESOURCE = "NOTIFICATION_COLOR_RESOURCE"
    const val NOTIFICATION_ENABLE_RESTART = "NOTIFICATION_ENABLE_RESTART"
    const val NOTIFICATION_ENABLE_STOP = "NOTIFICATION_ENABLE_STOP"
    const val NOTIFICATION_SHOW = "NOTIFICATION_SHOW"

    // BackgroundManager Keys
    const val BACKGROUND_MANAGER_POLICY = "BACKGROUND_MANAGER_POLICY"
    const val BACKGROUND_MANAGER_EXECUTE_DELAY = "BACKGROUND_MANAGER_EXECUTE_DELAY"
    const val BACKGROUND_MANAGER_KILL_APP = "BACKGROUND_MANAGER_KILL_APP"

    // values
    const val BACKGROUND_MANAGER_POLICY_RESPECT = "BACKGROUND_MANAGER_POLICY_RESPECT"
    const val BACKGROUND_MANAGER_POLICY_FOREGROUND = "BACKGROUND_MANAGER_POLICY_FOREGROUND"

    // Controller Keys
    const val CONTROLLER_BUILD_CONFIG_DEBUG = "CONTROLLER_BUILD_CONFIG_DEBUG"
    const val CONTROLLER_RESTART_DELAY = "CONTROLLER_RESTART_DELAY"
    const val CONTROLLER_STOP_DELAY = "CONTROLLER_STOP_DELAY"
    const val CONTROLLER_DISABLE_STOP_SERVICE_TASK_REMOVED = "CONTROLLER_DISABLE_STOP_SERVICE_TASK_REMOVED"

    // Notification Settings
    fun getNotificationVisibilitySetting(prefs: Prefs): Int =
        prefs.read(NOTIFICATION_VISIBILITY, NotificationCompat.VISIBILITY_PRIVATE)

    fun getNotificationColorSetting(prefs: Prefs): Int =
        prefs.read(NOTIFICATION_COLOR_RESOURCE, R.color.primaryColor)

    fun getNotificationRestartEnableSetting(prefs: Prefs): Boolean =
        prefs.read(NOTIFICATION_ENABLE_RESTART, true)

    fun getNotificationStopEnableSetting(prefs: Prefs): Boolean =
        prefs.read(NOTIFICATION_ENABLE_STOP, true)

    fun getNotificationShowSetting(prefs: Prefs): Boolean =
        prefs.read(NOTIFICATION_SHOW, true)

    // Background Manager Settings
    fun getBackgroundManagerPolicySetting(prefs: Prefs): String? =
        prefs.read(BACKGROUND_MANAGER_POLICY, Prefs.INVALID_STRING)

    fun getBackgroundManagerExecuteDelaySetting(prefs: Prefs): Int =
        prefs.read(BACKGROUND_MANAGER_EXECUTE_DELAY, 20)

    fun getBackgroundManagerKillAppSetting(prefs: Prefs): Boolean =
        prefs.read(BACKGROUND_MANAGER_KILL_APP, true)

    // Controller Settings
    fun getControllerRestartDelaySetting(prefs: Prefs): Long =
        prefs.read(CONTROLLER_RESTART_DELAY, 100L)

    fun getControllerStopDelaySetting(prefs: Prefs): Long =
        prefs.read(CONTROLLER_STOP_DELAY, 100L)

    fun getControllerDisableStopServiceOnTaskRemovedSetting(prefs: Prefs): Boolean =
        prefs.read(CONTROLLER_DISABLE_STOP_SERVICE_TASK_REMOVED, false)

    fun getControllerBuildConfigDebugSetting(prefs: Prefs): Boolean =
        prefs.read(CONTROLLER_BUILD_CONFIG_DEBUG, BuildConfig.DEBUG)
}