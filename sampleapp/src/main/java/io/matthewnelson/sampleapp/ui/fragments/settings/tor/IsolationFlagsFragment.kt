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
package io.matthewnelson.sampleapp.ui.fragments.settings.tor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import io.matthewnelson.sampleapp.R
import io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings


class IsolationFlagsFragment(
    private val bottomMarginAdjust: Int,
    private val flagType: String,
    private val serviceTorSettings: ServiceTorSettings
) : Fragment() {

    companion object {
        const val SOCKS_FLAGS = "SOCKS_FLAGS"
    }

    private val backPressHandler = BackPressHandler(true)

    private lateinit var layoutTop: LinearLayout
    private lateinit var layoutBottom: LinearLayout
    private lateinit var layoutEnd: LinearLayout
    private lateinit var layoutStart: LinearLayout
    private lateinit var layoutConstraintInner: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.apply {
            onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressHandler)
        }
        return inflater.inflate(R.layout.fragment_isolation_flags, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        setMargins()
        setClickListeners()
        enableParentViews(false)
    }

    override fun onDestroyView() {
        enableParentViews(true)
        super.onDestroyView()
    }

    private fun findViews(view: View) {
        layoutTop = view.findViewById(R.id.isolation_flags_space_top)
        layoutBottom = view.findViewById(R.id.isolation_flags_layout_bottom)
        layoutEnd = view.findViewById(R.id.isolation_flags_layout_end)
        layoutStart = view.findViewById(R.id.isolation_flags_layout_start)
        layoutConstraintInner = view.findViewById(R.id.isolation_flags_layout_constraint_inner)
    }

    private fun setMargins() {
        val marginSize = layoutConstraintInner.marginTop

        val layoutParams = layoutConstraintInner.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.apply {
            this.setMargins(marginSize, marginSize, marginSize, marginSize + bottomMarginAdjust)
        }
    }

    private fun setClickListeners() {
        layoutTop.setOnClickListener {
            removeFragment()
        }
        layoutBottom.setOnClickListener {
            removeFragment()
        }
        layoutEnd.setOnClickListener {
            removeFragment()
        }
        layoutStart.setOnClickListener {
            removeFragment()
        }
    }

    private fun removeFragment() {
        // TODO: Save Data
        parentFragmentManager.beginTransaction().apply {
            remove(this@IsolationFlagsFragment)
            commit()
        }
    }

    private fun enableParentViews(enable: Boolean) {
        parentFragment?.view?.apply {
            val layoutSecondary = findViewById<ConstraintLayout>(R.id.settings_tor_layout_constraint_scroll)
            for (i in 0 until layoutSecondary.childCount) {
                val child = layoutSecondary.getChildAt(i)
                child.isEnabled = enable
            }
            findViewById<Button>(R.id.settings_tor_button_save).isEnabled = enable
        }
    }

    private inner class BackPressHandler(enable: Boolean): OnBackPressedCallback(enable) {

        override fun handleOnBackPressed() {
            removeFragment()
        }

    }
}