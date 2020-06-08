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

import android.os.Process
import com.msopentech.thali.universal.toronionproxy.*
import java.io.File

class AndroidOnionProxyContext
/**
 * Constructs instance of AndroidOnionProxyContext. We provide an alternativeInstallDir here. If the Android
 * installer successfully extracts the native libraries into a non-writable space, the alternativeInstallDir
 * will be ignored. It is only used if the extraction is unsuccessful.
 *
 * configDir is a writable directory for tor config and data information.
 *
 */
    (torConfig: TorConfig?, torInstaller: TorInstaller?, settings: TorSettings?) :
    OnionProxyContext(torConfig, torInstaller, settings) {
    override fun generateWriteObserver(file: File?): WriteObserver {
        return AndroidWriteObserver(file)
    }

    override val processId: String
        get() = Process.myPid().toString()
}