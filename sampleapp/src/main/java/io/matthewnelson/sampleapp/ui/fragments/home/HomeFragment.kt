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
package io.matthewnelson.sampleapp.ui.fragments.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import io.matthewnelson.sampleapp.R
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.prefs.TorServicePrefs
import io.matthewnelson.topl_service.util.ServiceConsts

class HomeFragment : Fragment() {

    companion object {
        private var hasDebugLogs = false
        private var appIsBeingKilled = false
        fun appIsBeingKilled() {
            appIsBeingKilled = true
        }
    }

    private lateinit var buttonDebug: Button
    private lateinit var buttonNewId: Button
    private lateinit var buttonRestart: Button
    private lateinit var buttonStart: Button
    private lateinit var buttonStop: Button

    private lateinit var torServicePrefs: TorServicePrefs
    private lateinit var logMessageAdapter: LogMessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        torServicePrefs = TorServicePrefs(inflater.context)
        hasDebugLogs = torServicePrefs.getBoolean(
            ServiceConsts.PrefKeyBoolean.HAS_DEBUG_LOGS,
            try {
                TorServiceController.getTorSettings().hasDebugLogs
            } catch (e: RuntimeException) {
                false
            }
        )
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        initButtons(view.context)
        logMessageAdapter = LogMessageAdapter(viewLifecycleOwner, view)
    }

    override fun onStart() {
        super.onStart()
        logMessageAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun findViews(view: View) {
        buttonDebug = view.findViewById(R.id.home_button_debug)
        buttonNewId = view.findViewById(R.id.home_button_new_identity)
        buttonRestart = view.findViewById(R.id.home_button_restart)
        buttonStart = view.findViewById(R.id.home_button_start)
        buttonStop = view.findViewById(R.id.home_button_stop)
    }

    private fun initButtons(context: Context) {
        setButtonDebugText()
        buttonDebug.setOnClickListener {
            hasDebugLogs = !hasDebugLogs
            setButtonDebugText()
            torServicePrefs.putBoolean(
                ServiceConsts.PrefKeyBoolean.HAS_DEBUG_LOGS,
                hasDebugLogs
            )
        }
        buttonStart.setOnClickListener {
            if (appIsBeingKilled) return@setOnClickListener
            try {
                TorServiceController.startTor()
            } catch (e: RuntimeException) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
        buttonStop.setOnClickListener {
            TorServiceController.stopTor()
        }
        buttonRestart.setOnClickListener {
            TorServiceController.restartTor()
        }
        buttonNewId.setOnClickListener {
            TorServiceController.newIdentity()
        }
    }

    private fun setButtonDebugText() {
        buttonDebug.text = if (hasDebugLogs) {
            getString(R.string.home_button_debugging_disable)
        } else {
            getString(R.string.home_button_debugging_enable)
        }
    }
}