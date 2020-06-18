package io.matthewnelson.topl_service_prefs

import android.content.Context
import android.content.SharedPreferences

/**
 * This class provides a standardized way for library users to change settings used
 * by `TorService` (ultimately TOPL) such that the values expressed as default
 * `TorSettings` sent to `TorService` via the Builder can be updated and then used after
 * restarting Tor.
 *
 * See the `topl-service-settings` module's `TorServiceSettings` class, which uses the
 * default `TorSettings` to fall back on if nothing is in SharedPreferences.
 * */
class TorServicePrefs(context: Context): PrefsKeys() {

    companion object {
        const val TOR_SERVICE_PREFS_NAME = "TorServicePrefs"
        const val NULL_INT_VALUE = Int.MIN_VALUE
        const val NULL_STRING_VALUE = "NULL_STRING_VALUE"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(TOR_SERVICE_PREFS_NAME, Context.MODE_PRIVATE)

    fun contains(@BooleanKey @IntKey @ListKey @StringKey prefsKey: String): Boolean =
        prefs.contains(prefsKey)

    fun remove(@BooleanKey @IntKey @ListKey @StringKey prefsKey: String) =
        prefs.edit().remove(prefsKey).apply()

    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) =
        prefs.registerOnSharedPreferenceChangeListener(listener)

    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) =
        prefs.unregisterOnSharedPreferenceChangeListener(listener)

    // Read
    fun getBoolean(@BooleanKey booleanKey: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(booleanKey, defValue)
    }

    fun getInt(@IntKey intKey: String, defValue: Int?): Int? {
        val value = prefs.getInt(intKey, defValue ?: NULL_INT_VALUE)
        return if (value == NULL_INT_VALUE) {
            null
        } else {
            value
        }
    }

    fun getList(@ListKey listKey: String, defValue: List<String>): List<String> {
        val csv: String = prefs.getString(listKey, defValue.joinToString()) ?: defValue.joinToString()
        return if (csv.trim().isEmpty()) {
            defValue
        } else {
            csv.split(", ")
        }
    }

    fun getString(@StringKey stringKey: String, defValue: String?): String? {
        val value = prefs.getString(stringKey, defValue ?: NULL_STRING_VALUE)
        return if (value == NULL_STRING_VALUE) {
            null
        } else {
            value
        }
    }


    // Write
    fun putBoolean(@BooleanKey booleanKey: String, value: Boolean) {
        val editor = prefs.edit().putBoolean(booleanKey, value)
        if (!editor.commit())
            editor.apply()
    }

    fun putInt(@IntKey intKey: String, value: Int?) {
        val editor = prefs.edit().putInt(intKey, value ?: NULL_INT_VALUE)
        if (!editor.commit())
            editor.apply()
    }

    fun putList(@ListKey listKey: String, value: List<String>) {
        val editor = prefs.edit().putString(listKey, value.joinToString())
        if (!editor.commit())
            editor.apply()
    }

    fun putString(@StringKey stringKey: String, value: String?) {
        val editor = prefs.edit().putString(stringKey, value ?: NULL_STRING_VALUE)
        if (!editor.commit())
            editor.apply()
    }
}