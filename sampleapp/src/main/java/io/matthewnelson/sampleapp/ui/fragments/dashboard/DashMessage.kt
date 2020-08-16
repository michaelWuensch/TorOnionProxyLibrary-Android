package io.matthewnelson.sampleapp.ui.fragments.dashboard

import androidx.annotation.DrawableRes

class DashMessage(
    val message: String,
    @DrawableRes val background: Int,
    val showLength: Long
) {
    companion object {
        const val EXCEPTION = "~~~ EXCEPTION ~~~\n"
    }
}