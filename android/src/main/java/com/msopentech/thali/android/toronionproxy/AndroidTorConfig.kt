/*
Copyright (c) Microsoft Open Technologies, Inc.
All Rights Reserved
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED,
INCLUDING WITHOUT LIMITATION ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE,
MERCHANTABLITY OR NON-INFRINGEMENT.

See the Apache 2 License for the specific language governing permissions and limitations under the License.
*/
package com.msopentech.thali.android.toronionproxy

import android.content.Context
import android.util.Log
import com.msopentech.thali.universal.toronionproxy.TorConfig
import java.io.File

/**
 * Creates config file that is compatible with Android.
 */
object AndroidTorConfig {
    private const val TAG = "AndroidTorConfig"

    /**
     * Creates a tor config file, where installDir is the directory containing the tor executables and native libraries.
     * This should reference the native library folder managed by Android.
     *
     * The configDirName contains the location of user writable config files and data.
     */
    fun createConfig(
        alternativeInstallDir: File,
        configDir: File?,
        context: Context
    ): TorConfig? {
        val nativeDir = File(context.applicationInfo.nativeLibraryDir)
        val torExecutable = File(nativeDir, "tor.so")
        return if (torExecutable.exists()) {
            Log.d(
                TAG,
                "Tor executable exists in native library directory: " + nativeDir.absolutePath
            )
            val builder =
                TorConfig.Builder(nativeDir, configDir)
            builder.build()
        } else {
            Log.d(
                TAG,
                "Setting Tor executable to alternative installation directory: "
                        + alternativeInstallDir.absolutePath
            )
            val builder =
                TorConfig.Builder(
                    alternativeInstallDir,
                    configDir
                )
            builder.build()
        }
    }
}