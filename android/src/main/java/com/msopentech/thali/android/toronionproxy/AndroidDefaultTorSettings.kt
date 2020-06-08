package com.msopentech.thali.android.toronionproxy

import android.content.Context
import com.msopentech.thali.universal.toronionproxy.DefaultSettings

class AndroidDefaultTorSettings(private val context: Context) : DefaultSettings() {
    override fun hasBridges(): Boolean {
        return true
    }

}