package io.matthewnelson.sampleapp.ui.fragments.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination

class CloseKeyBoardNavListener(private val view: View): NavController.OnDestinationChangedListener {
    private var startLock: Any? = null

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (startLock == null) {
            startLock = Any()
            return
        }

        closeKeyboard()
        controller.removeOnDestinationChangedListener(this)
    }

    private fun closeKeyboard() {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}