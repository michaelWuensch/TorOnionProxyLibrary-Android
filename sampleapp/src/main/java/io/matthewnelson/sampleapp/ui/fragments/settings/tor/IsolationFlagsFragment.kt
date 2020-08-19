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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.matthewnelson.sampleapp.R
import io.matthewnelson.topl_core_base.BaseConsts.IsolationFlag
import io.matthewnelson.topl_core_base.TorSettings

class IsolationFlagsFragment(
    private val bottomMarginAdjust: Int,
    private val portType: String,
    private val defaultTorSettings: TorSettings,
    private val enabledIsolationFlags: MutableSet<@IsolationFlag String>
) : Fragment() {

    companion object {
        const val SOCKS_FLAGS = "Socks Isolation Flags"
        const val HTTP_FLAGS = "HTTP Isolation Flags"
        const val DNS_FLAGS = "DNS Isolation Flags"
        const val TRANS_FLAGS = "Trans Isolation Flags"
    }

    private val backPressHandler = BackPressHandler(true)

    private lateinit var layoutTop: LinearLayout
    private lateinit var layoutBottom: LinearLayout
    private lateinit var layoutEnd: LinearLayout
    private lateinit var layoutStart: LinearLayout
    private lateinit var layoutConstraintInner: ConstraintLayout
    private lateinit var buttonReset: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var isolationFlagsAdapter: IsolationFlagsAdapter

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
        setResetButtonText()
        setMargins()
        initRecyclerView(view.context)
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
        recyclerView = view.findViewById(R.id.recycler_view_isolation_flags)
        buttonReset = view.findViewById(R.id.isolation_flags_button_reset)
    }

    private fun setResetButtonText() {
        when (portType) {
            // SOCKS_FLAGS is default string
            HTTP_FLAGS -> {
                buttonReset.text = getString(R.string.isolation_fragment_reset_http)
            }
            DNS_FLAGS -> {
                buttonReset.text = getString(R.string.isolation_fragment_reset_dns)
            }
            TRANS_FLAGS -> {
                buttonReset.text = getString(R.string.isolation_fragment_reset_trans)
            }
        }
    }

    private fun setMargins() {
        val marginSize = layoutConstraintInner.marginTop

        val layoutParams = layoutConstraintInner.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.apply {
            this.setMargins(marginSize, marginSize, marginSize, marginSize + bottomMarginAdjust)
        }
    }

    private fun initRecyclerView(context: Context) {
        isolationFlagsAdapter = IsolationFlagsAdapter()
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = isolationFlagsAdapter
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
        buttonReset.setOnClickListener {
            isolationFlagsAdapter.resetToDefaults()
            isolationFlagsAdapter.notifyDataSetChanged()
        }
    }

    private fun removeFragment() {
        parentFragment?.let {
            (it as SettingsTorFragment).setIsolationFlags(portType, enabledIsolationFlags.toList())
        }
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

    private inner class IsolationFlagsAdapter: RecyclerView.Adapter<IsolationFlagsAdapter.IsolationFlagHolder>() {

        private val items = IsolationFlag.getAll()

        fun resetToDefaults() {
            enabledIsolationFlags.clear()
            when (portType) {
                SOCKS_FLAGS -> {
                    defaultTorSettings.socksPortIsolationFlags?.let {
                        enabledIsolationFlags.addAll(it)
                    }
                }
                HTTP_FLAGS -> {
                    defaultTorSettings.httpTunnelPortIsolationFlags?.let {
                        enabledIsolationFlags.addAll(it)
                    }
                }
                DNS_FLAGS -> {
                    defaultTorSettings.dnsPortIsolationFlags?.let {
                        enabledIsolationFlags.addAll(it)
                    }
                }
                TRANS_FLAGS -> {
                    defaultTorSettings.transPortIsolationFlags?.let {
                        enabledIsolationFlags.addAll(it)
                    }
                }
            }
        }

        inner class IsolationFlagHolder(view: View): RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IsolationFlagHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.holder_isolation_flag, parent, false)
            return IsolationFlagHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: IsolationFlagHolder, position: Int) {
            val isolationFlag = items[position]
            val layout = holder.itemView.findViewById<ConstraintLayout>(R.id.holder_isolation_flag_layout_constraint)
            val textView = holder.itemView.findViewById<TextView>(R.id.holder_isolation_flag_text_view)
            val checkBox = holder.itemView.findViewById<CheckBox>(R.id.holder_isolation_flag_check_box)

            textView.text = isolationFlag
            checkBox.isChecked = enabledIsolationFlags.contains(isolationFlag)

            checkBox.setOnClickListener {
                clicked(isolationFlag, checkBox)
            }
            layout.setOnClickListener {
                clicked(isolationFlag, checkBox)
            }
        }

        private fun clicked(@IsolationFlag isolationFlag: String, checkBox: CheckBox) {
            if (enabledIsolationFlags.contains(isolationFlag)) {
                enabledIsolationFlags.remove(isolationFlag)
                checkBox.isChecked = false
            } else {
                enabledIsolationFlags.add(isolationFlag)
                checkBox.isChecked = true
            }
        }
    }
}