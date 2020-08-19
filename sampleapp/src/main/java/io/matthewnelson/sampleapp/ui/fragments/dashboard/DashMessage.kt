package io.matthewnelson.sampleapp.ui.fragments.dashboard

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import io.matthewnelson.sampleapp.R

class DashMessage(
    val message: String,
    @DrawableRes val background: Int,
    val showLength: Long,
    @ColorRes val textColor: Int = R.color.tor_service_white
) {
    companion object {
        const val EXCEPTION = "~~~ EXCEPTION ~~~\n"
    }
}