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
package io.matthewnelson.sampleapp.ui.fragments.dashboard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import io.matthewnelson.sampleapp.App
import io.matthewnelson.sampleapp.R
import io.matthewnelson.sampleapp.topl_android.MyEventBroadcaster
import io.matthewnelson.sampleapp.ui.MainActivity
import io.matthewnelson.sampleapp.ui.fragments.home.HomeFragment
import io.matthewnelson.topl_core_base.BaseConsts.TorState
import io.matthewnelson.topl_service.TorServiceController
import kotlinx.coroutines.*
import kotlin.system.exitProcess

class DashboardFragment : Fragment() {

    companion object {
        private val _liveAppRestartButtonShow = MutableLiveData<Boolean>(false)
        private val liveAppRestartButtonShow: LiveData<Boolean> = _liveAppRestartButtonShow
        fun librarySettingsWereChanged() {
            if (liveAppRestartButtonShow.value != true)
                _liveAppRestartButtonShow.value = true
        }

        private val _liveDashMessage = MutableLiveData<DashMessage?>(null)
        private val liveDashMessage: LiveData<DashMessage?> = _liveDashMessage
        fun showMessage(dashboardMessage: DashMessage) {
            _liveDashMessage.value = dashboardMessage
        }
    }

    // Top row
    private lateinit var textViewBandwidth: TextView
    private lateinit var textViewNetworkState: TextView
    private lateinit var textViewState: TextView

    // Bottom row
    private lateinit var textViewDnsPort: TextView
    private lateinit var textViewHttpPort: TextView
    private lateinit var textViewSocksPort: TextView

    // Messages
    private lateinit var textViewMessage: TextView

    // Button
    private lateinit var buttonAppRestart: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        initObservers(viewLifecycleOwner)
        initButtons(view.context, viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun findViews(view: View) {
        textViewBandwidth = view.findViewById(R.id.dash_text_view_bandwidth)
        textViewNetworkState = view.findViewById(R.id.dash_text_view_tor_network_state)
        textViewState = view.findViewById(R.id.dash_text_view_tor_state)
        textViewDnsPort = view.findViewById(R.id.dash_text_view_port_dns)
        textViewHttpPort = view.findViewById(R.id.dash_text_view_port_http)
        textViewSocksPort = view.findViewById(R.id.dash_text_view_port_socks)
        textViewMessage = view.findViewById(R.id.dash_text_view_message)
        buttonAppRestart = view.findViewById(R.id.dash_button_app_restart)
    }

    private fun initObservers(owner: LifecycleOwner) {
        liveAppRestartButtonShow.observe(owner, Observer {
            if (it == true) {
                buttonAppRestart.visibility = View.VISIBLE
            }
        })
        liveDashMessage.observe(owner, Observer {
            if (it != null) {
                textViewMessage.text = it.message
                val colorHex = colorResToHexString(textViewMessage.context, it.textColor)
                textViewMessage.setTextColor(Color.parseColor(colorHex))
                textViewMessage.background = ContextCompat.getDrawable(textViewMessage.context, it.background)
                textViewMessage.visibility = View.VISIBLE
                launchCleanUpMessageJob(it.showLength)
            } else {
                textViewMessage.visibility = View.GONE
            }
        })
        TorServiceController.appEventBroadcaster?.let {
            (it as MyEventBroadcaster).liveBandwidth.observe(owner, Observer { string ->
                if (string.isNullOrEmpty()) return@Observer
                textViewBandwidth.text = string
            })
        }
        TorServiceController.appEventBroadcaster?.let {
            (it as MyEventBroadcaster).liveTorState.observe(owner, Observer { data ->
                if (data == null) return@Observer
                textViewState.text = data.state
                textViewNetworkState.text = data.networkState
            })
        }
        TorServiceController.appEventBroadcaster?.let {
            (it as MyEventBroadcaster).liveTorPortInfo.observe(owner, Observer { data ->
                textViewDnsPort.text = data?.dnsPort ?: "-----"
                textViewHttpPort.text = data?.httpPort ?: "-----"
                textViewSocksPort.text = data?.socksPort ?: "-----"
            })
        }
    }

    private fun colorResToHexString(context: Context, @ColorRes colorRes: Int): String =
        "#${Integer.toHexString(ContextCompat.getColor(context, colorRes))}"

    private var showMessageCleanUpJob: Job? = null

    private fun launchCleanUpMessageJob(delayLength: Long) {
        showMessageCleanUpJob?.cancel()

        showMessageCleanUpJob = lifecycleScope.launch {
            delay(delayLength)
            _liveDashMessage.value = null
        }
    }

    private var killAppJob: Job? = null

    private fun initButtons(context: Context, owner: LifecycleOwner) {
        buttonAppRestart.setOnClickListener {
            buttonAppRestart.isEnabled = false
            HomeFragment.appIsBeingKilled()

            showMessage(
                DashMessage(
                    "Application is being killed",
                    R.drawable.dash_message_color_red,
                    10_000
                )
            )

            TorServiceController.appEventBroadcaster?.let {
                (it as MyEventBroadcaster).liveTorState.observe(owner, Observer { data ->
                    if (data != null) {

                        when (data.state) {
                            TorState.ON,
                            TorState.STARTING -> {
                                killAppJob?.cancel()
                                TorServiceController.stopTor()
                            }
                            TorState.OFF -> {
                                if (killAppJob?.isActive == true)
                                    killAppJob?.cancel()

                                killAppJob = CoroutineScope(Dispatchers.Main).launch {
                                    delay(500L + App.stopTorDelaySettingAtAppStartup.toLong())
                                    killApplication(context)
                                }
                            }
                        }

                    }
                })
            }

        }
    }

    private fun killApplication(context: Context) {
        // Build notification to restart the Application
        val intent = Intent(context.applicationContext, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context.applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationManager =
            context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        // Setup new notification channel
        val channelId = "${context.packageName}.restart_application"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = App.PREFS_NAME
            val importance = NotificationManager.IMPORTANCE_HIGH
            val description = "Channel to restart the application."

            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setSound(null, null)

            notificationManager?.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context.applicationContext, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSound(null)
            .setContentTitle("Restart TOPL-Android Demo")
            .setContentText("Click to launch")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager?.notify(9999, notificationBuilder.build())
        exitProcess(0)
    }
}