/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
package io.matthewnelson.topl_service.prefs

import android.content.Context
import android.content.SharedPreferences
import io.matthewnelson.topl_service.util.ServiceConsts

/**
 * This class provides a standardized way for library users to change settings used
 * by [io.matthewnelson.topl_service.service.TorService] such that the values expressed
 * as default [io.matthewnelson.topl_core_base.TorSettings] when initializing things via
 * the [io.matthewnelson.topl_service.TorServiceController.Builder] can be updated. The
 * values saved to [TorServicePrefs] are always preferred over the defaults declared.
 *
 * See [io.matthewnelson.topl_service.onionproxy.ServiceTorSettings]
 * */
class TorServicePrefs(context: Context): ServiceConsts() {

    companion object {
        const val TOR_SERVICE_PREFS_NAME = "TorServicePrefs"
        const val NULL_INT_VALUE = Int.MIN_VALUE
        const val NULL_STRING_VALUE = "NULL_STRING_VALUE"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(TOR_SERVICE_PREFS_NAME, Context.MODE_PRIVATE)


    /////////////////
    /// Listeners ///
    /////////////////
    /**
     * Registers a [SharedPreferences.OnSharedPreferenceChangeListener] for the
     * associated SharedPreference
     * */
    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) =
        prefs.registerOnSharedPreferenceChangeListener(listener)
    /**
     * Unregisters a [SharedPreferences.OnSharedPreferenceChangeListener] for the
     * associated SharedPreference
     * */
    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) =
        prefs.unregisterOnSharedPreferenceChangeListener(listener)


    /////////////
    /// Query ///
    /////////////
    /**
     * Checks if the SharedPreference contains a value for the supplied [prefsKey].
     * Accepts the following annotation type String values:
     *  - [ServiceConsts.PrefKeyBoolean]
     *  - [ServiceConsts.PrefKeyInt]
     *  - [ServiceConsts.PrefKeyList]
     *  - [ServiceConsts.PrefKeyString]
     *
     *  @param [prefsKey] String of type ServiceConsts.PrefKey*
     *  @return True if the SharedPreference contains a value for the associated
     *   [prefsKey], false if not
     *  * */
    fun contains(@PrefKeyBoolean @PrefKeyInt @PrefKeyList @PrefKeyString prefsKey: String): Boolean =
        prefs.contains(prefsKey)

    /**
     * Returns a Boolean value for the provided [ServiceConsts.PrefKeyBoolean]. If no
     * value is stored in the SharedPreference, [defValue] will be returned.
     *
     * @param [booleanKey] String of type [ServiceConsts.PrefKeyBoolean]
     * @param [defValue] Use the [io.matthewnelson.topl_core_base.TorSettings] value
     *  associated with the [booleanKey].
     * @return The Boolean value associated with the [booleanKey], otherwise [defValue]
     * */
    fun getBoolean(@PrefKeyBoolean booleanKey: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(booleanKey, defValue)
    }

    /**
     * Returns an Int value for the provided [ServiceConsts.PrefKeyInt]. If no
     * value is stored in the SharedPreference, [defValue] will be returned.
     *
     * @param [intKey] String of type [ServiceConsts.PrefKeyInt]
     * @param [defValue] Use the [io.matthewnelson.topl_core_base.TorSettings] value
     *  associated with the [intKey].
     * @return The Int value associated with [intKey], otherwise [defValue]
     * */
    fun getInt(@PrefKeyInt intKey: String, defValue: Int?): Int? {
        val value = prefs.getInt(intKey, defValue ?: NULL_INT_VALUE)
        return if (value == NULL_INT_VALUE) {
            null
        } else {
            value
        }
    }

    /**
     * Returns a List of Strings for the provided [ServiceConsts.PrefKeyList]. If no
     * value is stored in the SharedPreference, [defValue] will be returned.
     *
     * @param [listKey] String of type [ServiceConsts.PrefKeyList]
     * @param [defValue] Use the [io.matthewnelson.topl_core_base.TorSettings] value
     *  associated with the [listKey].
     * @return The List of Strings associated with the [listKey], otherwise [defValue]
     * */
    fun getList(@PrefKeyList listKey: String, defValue: List<String>): List<String> {
        val csv: String = prefs.getString(listKey, defValue.joinToString()) ?: defValue.joinToString()
        return if (csv.trim().isEmpty()) {
            defValue
        } else {
            csv.split(", ")
        }
    }

    /**
     * Returns a String value for the provided [ServiceConsts.PrefKeyString]. If no
     * value is stored in the SharedPreference, [defValue] will be returned.
     *
     * @param [stringKey] String of type [ServiceConsts.PrefKeyString]
     * @param [defValue] Use the [io.matthewnelson.topl_core_base.TorSettings] value
     *  associated with the [stringKey].
     * @return The String value associated with [stringKey], otherwise [defValue]
     * */
    fun getString(@PrefKeyString stringKey: String, defValue: String?): String? {
        val value = prefs.getString(stringKey, defValue ?: NULL_STRING_VALUE)
        return if (value == NULL_STRING_VALUE) {
            null
        } else {
            value
        }
    }


    //////////////
    /// Modify ///
    //////////////
    /**
     * Removes from the SharedPreference the value associated with [prefsKey] if there is one.
     * Accepts the following annotation type String values:
     *  - [ServiceConsts.PrefKeyBoolean]
     *  - [ServiceConsts.PrefKeyInt]
     *  - [ServiceConsts.PrefKeyList]
     *  - [ServiceConsts.PrefKeyString]
     *
     *  @param [prefsKey] String of type ServiceConsts.PrefKey*
     *  * */
    fun remove(@PrefKeyBoolean @PrefKeyInt @PrefKeyList @PrefKeyString prefsKey: String) {
        val editor = prefs.edit().remove(prefsKey)
        if (!editor.commit())
            editor.apply()
    }

    /**
     * Inserts a Boolean value into the SharedPreference for the supplied [booleanKey].
     *
     * @param [booleanKey] String of type [ServiceConsts.PrefKeyBoolean]
     * @param [value] Your Boolean value
     * */
    fun putBoolean(@PrefKeyBoolean booleanKey: String, value: Boolean) {
        val editor = prefs.edit().putBoolean(booleanKey, value)
        if (!editor.commit())
            editor.apply()
    }

    /**
     * Inserts an Int value into the SharedPreference for the supplied [intKey].
     *
     * @param [intKey] String of type [ServiceConsts.PrefKeyInt]
     * @param [value] Your Int? value
     * */
    fun putInt(@PrefKeyInt intKey: String, value: Int?) {
        val editor = prefs.edit().putInt(intKey, value ?: NULL_INT_VALUE)
        if (!editor.commit())
            editor.apply()
    }

    /**
     * Inserts a List of Strings as a comma separated String into the SharedPreference
     * for the supplied [listKey].
     *
     * @param [listKey] String of type [ServiceConsts.PrefKeyList]
     * @param [value] Your List<String> value
     * */
    fun putList(@PrefKeyList listKey: String, value: List<String>) {
        val editor = prefs.edit().putString(listKey, value.joinToString())
        if (!editor.commit())
            editor.apply()
    }

    /**
     * Inserts a String value into the SharedPreference for the supplied [stringKey].
     *
     * @param [stringKey] String of type [ServiceConsts.PrefKeyString]
     * @param [value] Your String value
     * */
    fun putString(@PrefKeyString stringKey: String, value: String?) {
        val editor = prefs.edit().putString(stringKey, value ?: NULL_STRING_VALUE)
        if (!editor.commit())
            editor.apply()
    }
}