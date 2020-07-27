/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
package io.matthewnelson.topl_service.service

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder

internal class TorServiceConnection: ServiceConnection {

    @Volatile
    var serviceBinder: TorService.TorServiceBinder? = null
        private set

    /**
     * Sets the reference to [TorService.TorServiceBinder] to `null` because
     * [onServiceDisconnected] is not always called when [Context.unbindService]
     * is made.
     * */
    fun clearServiceBinderReference() {
        serviceBinder = null
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        // TODO: Implement logic for detecting undesired calls to this method (which
        //  is primarily when this gets fired off).
        serviceBinder = null
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        serviceBinder = service as TorService.TorServiceBinder
    }

}