package io.matthewnelson.topl_android_service

import android.content.Context
import android.content.SharedPreferences
import io.matthewnelson.topl_android.OnionProxyManager

internal object TorServiceManager {

    // SharedPreferences Keys
    private const val PREFS_APP_VERSION = "PREFS_APP_VERSION"

    private lateinit var appContext: Context
    private lateinit var prefs: SharedPreferences

}