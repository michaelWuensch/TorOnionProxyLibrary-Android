package io.matthewnelson.sampleapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LogMessageAdapter: RecyclerView.Adapter<LogMessageAdapter.LogMessageHolder>(){

    companion object {
        val logMessageList = mutableListOf<String>()
        private const val maxMessages = 200
    }

    inner class LogMessageHolder(val textView: TextView): RecyclerView.ViewHolder(textView)

    fun addLogMessage(msg: String) {
        logMessageList.add(msg)
        this.notifyItemInserted(logMessageList.size - 1)
        if (logMessageList.size > maxMessages) {
            removeFirstLogMessage()
        }
    }

    private fun removeFirstLogMessage() {
        logMessageList.removeAt(0)
        this.notifyItemRemoved(0)
    }

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