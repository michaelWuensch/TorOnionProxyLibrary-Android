package io.matthewnelson.sampleapp.ui.fragments.dashboard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.sampleapp.App
import io.matthewnelson.sampleapp.R
import io.matthewnelson.sampleapp.topl_android.MyEventBroadcaster
import io.matthewnelson.sampleapp.ui.MainActivity
import io.matthewnelson.sampleapp.ui.fragments.settings.library.LibraryPrefs
import io.matthewnelson.topl_core_base.BaseConsts
import io.matthewnelson.topl_service.TorServiceController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class DashboardFragment : Fragment() {

    companion object {
        private val _liveAppRestartButtonShow = MutableLiveData<Boolean>(false)
        private val liveAppRestartButtonShow: LiveData<Boolean> = _liveAppRestartButtonShow

        fun librarySettingsWereChanged() {
            if (liveAppRestartButtonShow.value != true)
                _liveAppRestartButtonShow.value = true
        }
    }

    // Top row
    private lateinit var textViewBandwidth: TextView
    private lateinit var textViewNetworkState: TextView
    private lateinit var textViewState: TextView

    // Bottom row
    private lateinit var textViewControlPort: TextView
    private lateinit var textViewSocksPort: TextView
    private lateinit var textViewHttpPort: TextView

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
        textViewControlPort = view.findViewById(R.id.dash_text_view_port_control)
        textViewSocksPort = view.findViewById(R.id.dash_text_view_port_socks)
        textViewHttpPort = view.findViewById(R.id.dash_text_view_port_http)
        buttonAppRestart = view.findViewById(R.id.dash_button_app_restart)
    }

    private fun initObservers(owner: LifecycleOwner) {
        liveAppRestartButtonShow.observe(owner, Observer {
            if (it == true) {
                buttonAppRestart.visibility = View.VISIBLE
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
            (it as MyEventBroadcaster).liveControlPortAddress.observe(owner, Observer { data ->
                textViewControlPort.text = data ?: "None"
            })
        }
        TorServiceController.appEventBroadcaster?.let {
            (it as MyEventBroadcaster).liveSocksPortAddress.observe(owner, Observer { data ->
                textViewSocksPort.text = data ?: "None"
            })
        }
        TorServiceController.appEventBroadcaster?.let {
            (it as MyEventBroadcaster).liveHttpPortAddress.observe(owner, Observer { data ->
                textViewHttpPort.text = data ?: "None"
            })
        }
    }

    private fun initButtons(context: Context, owner: LifecycleOwner) {
        buttonAppRestart.setOnClickListener {
            Toast.makeText(context, "Killing Application", Toast.LENGTH_LONG).show()
            val prefs = Prefs.createUnencrypted(App.PREFS_NAME, context)

            TorServiceController.appEventBroadcaster?.let {
                (it as MyEventBroadcaster).liveTorState.observe(owner, Observer { data ->
                    if (data != null) {
                        var delayLength = 500L + LibraryPrefs.getControllerStopDelaySetting(prefs)
                        if (data.state == BaseConsts.TorState.OFF)
                            delayLength = 200L

                        TorServiceController.stopTor()
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(delayLength)
                            killApplication(context)
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