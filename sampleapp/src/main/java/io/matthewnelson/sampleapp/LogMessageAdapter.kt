/*
* TorOnionProxyLibrary-Android (a.k.a. topl-android) is a derivation of
* work from the Tor_Onion_Proxy_Library project that started at commit
* hash `74407114cbfa8ea6f2ac51417dda8be98d8aba86`. Contributions made after
* said commit hash are:
*
*     Copyright (C) 2020 Matthew Nelson
*
*     This program is free software: you can redistribute it and/or modify it
*     under the terms of the GNU General Public License as published by the
*     Free Software Foundation, either version 3 of the License, or (at your
*     option) any later version.
*
*     This program is distributed in the hope that it will be useful, but
*     WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
*     or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
*     for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with this program. If not, see <https://www.gnu.org/licenses/>.
*
* `===========================================================================`
* `+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++`
* `===========================================================================`
*
* The following exception is an additional permission under section 7 of the
* GNU General Public License, version 3 (“GPLv3”).
*
*     "The Interfaces" is henceforth defined as Application Programming Interfaces
*     that are publicly available classes/functions/etc (ie: do not contain the
*     visibility modifiers `internal`, `private`, `protected`, or are within
*     classes/functions/etc that contain the aforementioned visibility modifiers)
*     to TorOnionProxyLibrary-Android users that are needed to implement
*     TorOnionProxyLibrary-Android and reside in ONLY the following modules:
*
*      - topl-core-base
*      - topl-service
*
*     The following are excluded from "The Interfaces":
*
*       - All other code
*
*     Linking TorOnionProxyLibrary-Android statically or dynamically with other
*     modules is making a combined work based on TorOnionProxyLibrary-Android.
*     Thus, the terms and conditions of the GNU General Public License cover the
*     whole combination.
*
*     As a special exception, the copyright holder of TorOnionProxyLibrary-Android
*     gives you permission to combine TorOnionProxyLibrary-Android program with free
*     software programs or libraries that are released under the GNU LGPL and with
*     independent modules that communicate with TorOnionProxyLibrary-Android solely
*     through "The Interfaces". You may copy and distribute such a system following
*     the terms of the GNU GPL for TorOnionProxyLibrary-Android and the licenses of
*     the other code concerned, provided that you include the source code of that
*     other code when and as the GNU GPL requires distribution of source code and
*     provided that you do not modify "The Interfaces".
*
*     Note that people who make modified versions of TorOnionProxyLibrary-Android
*     are not obligated to grant this special exception for their modified versions;
*     it is their choice whether to do so. The GNU General Public License gives
*     permission to release a modified version without this exception; this exception
*     also makes it possible to release a modified version which carries forward this
*     exception. If you modify "The Interfaces", this exception does not apply to your
*     modified version of TorOnionProxyLibrary-Android, and you must remove this
*     exception when you distribute your modified version.
* */
package io.matthewnelson.sampleapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @suppress
 * */
class LogMessageAdapter(
    activity: MainActivity
): RecyclerView.Adapter<LogMessageAdapter.LogMessageHolder>(){

    companion object {
        private val logMessageList = mutableListOf<String>()
        private const val maxMessages = 200

        private val liveNotifyInserted = MutableLiveData(false)
        private fun observeLiveNotifyInserted(): LiveData<Boolean> {
            liveNotifyInserted.value = null
            return liveNotifyInserted
        }

        private val liveNotifyRemoved = MutableLiveData(false)
        private fun observeLiveNotifyRemoved(): LiveData<Boolean> {
            liveNotifyRemoved.value = null
            return liveNotifyRemoved
        }

        fun addLogMessageNotifyAndCurate(msg: String) {
            logMessageList.add(msg)
            if (liveNotifyInserted.hasActiveObservers()) {
                liveNotifyInserted.value = liveNotifyInserted.value != true
            }
            if (logMessageList.size > maxMessages) {
                removeFirstLogMessageAndNotify()
            }
        }

        private fun removeFirstLogMessageAndNotify() {
            logMessageList.removeAt(0)

            if (!liveNotifyRemoved.hasActiveObservers()) return
            liveNotifyRemoved.value = liveNotifyRemoved.value != true
        }
    }

    init {
        activity.findViewById<RecyclerView>(R.id.recycler_view_log_messages).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = this@LogMessageAdapter
        }

        observeLiveNotifyInserted().observe(activity, Observer {
            if (it == null) return@Observer
            this.notifyItemInserted(logMessageList.size - 1)
        })

        observeLiveNotifyRemoved().observe(activity, Observer {
            if (it == null) return@Observer
            this.notifyItemRemoved(0)
        })
    }

    inner class LogMessageHolder(val textView: TextView): RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogMessageHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.log_message, parent, false) as TextView
        return LogMessageHolder(textView)
    }

    override fun getItemCount(): Int =
        logMessageList.size

    override fun onBindViewHolder(holder: LogMessageHolder, position: Int) {
        holder.textView.text = logMessageList[position]
    }

}