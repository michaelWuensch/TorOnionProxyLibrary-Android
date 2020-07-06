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
package io.matthewnelson.sampleapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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