package io.matthewnelson.sampleapp.ui.fragments.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import io.matthewnelson.sampleapp.R
import io.matthewnelson.sampleapp.topl_android.MyEventBroadcaster
import io.matthewnelson.topl_service.TorServiceController

class DashboardFragment : Fragment() {

    private lateinit var textViewBandwidth: TextView
    private lateinit var textViewNetworkState: TextView
    private lateinit var textViewState: TextView

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun findViews(view: View) {
        textViewBandwidth = view.findViewById(R.id.dash_text_view_bandwidth)
        textViewNetworkState = view.findViewById(R.id.dash_text_view_tor_network_state)
        textViewState = view.findViewById(R.id.dash_text_view_tor_state)
    }

    private fun initObservers(owner: LifecycleOwner) {
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
    }
}