package io.matthewnelson.sampleapp.ui.fragments.settings.tor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
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