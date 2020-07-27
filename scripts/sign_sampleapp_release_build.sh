#!/usr/bin/env bash
# /*
# * TorOnionProxyLibrary-Android (a.k.a. topl-android) is a derivation of
# * work from the Tor_Onion_Proxy_Library project that started at commit
# * hash `74407114cbfa8ea6f2ac51417dda8be98d8aba86`. Contributions made after
# * said commit hash are:
# *
# *     Copyright (C) 2020 Matthew Nelson
# *
# *     This program is free software: you can redistribute it and/or modify it
# *     under the terms of the GNU General Public License as published by the
# *     Free Software Foundation, either version 3 of the License, or (at your
# *     option) any later version.
# *
# *     This program is distributed in the hope that it will be useful, but
# *     WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
# *     or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
# *     for more details.
# *
# *     You should have received a copy of the GNU General Public License
# *     along with this program. If not, see <https://www.gnu.org/licenses/>.
# *
# * `===========================================================================`
# * `+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++`
# * `===========================================================================`
# *
# * The following exception is an additional permission under section 7 of the
# * GNU General Public License, version 3 (“GPLv3”).
# *
# *     "The Interfaces" is henceforth defined as Application Programming Interfaces
# *     that are publicly available classes/functions/etc (ie: do not contain the
# *     visibility modifiers `internal`, `private`, `protected`, or are within
# *     classes/functions/etc that contain the aforementioned visibility modifiers)
# *     to TorOnionProxyLibrary-Android users that are needed to implement
# *     TorOnionProxyLibrary-Android and reside in ONLY the following modules:
# *
# *      - topl-core-base
# *      - topl-service
# *
# *     The following are excluded from "The Interfaces":
# *
# *       - All other code
# *
# *     Linking TorOnionProxyLibrary-Android statically or dynamically with other
# *     modules is making a combined work based on TorOnionProxyLibrary-Android.
# *     Thus, the terms and conditions of the GNU General Public License cover the
# *     whole combination.
# *
# *     As a special exception, the copyright holder of TorOnionProxyLibrary-Android
# *     gives you permission to combine TorOnionProxyLibrary-Android program with free
# *     software programs or libraries that are released under the GNU LGPL and with
# *     independent modules that communicate with TorOnionProxyLibrary-Android solely
# *     through "The Interfaces". You may copy and distribute such a system following
# *     the terms of the GNU GPL for TorOnionProxyLibrary-Android and the licenses of
# *     the other code concerned, provided that you include the source code of that
# *     other code when and as the GNU GPL requires distribution of source code and
# *     provided that you do not modify "The Interfaces".
# *
# *     Note that people who make modified versions of TorOnionProxyLibrary-Android
# *     are not obligated to grant this special exception for their modified versions;
# *     it is their choice whether to do so. The GNU General Public License gives
# *     permission to release a modified version without this exception; this exception
# *     also makes it possible to release a modified version which carries forward this
# *     exception. If you modify "The Interfaces", this exception does not apply to your
# *     modified version of TorOnionProxyLibrary-Android, and you must remove this
# *     exception when you distribute your modified version.
# * */

if [ "$ANDROID_SDK" == "" ]; then
  echo "ANDROID_SDK environment variable not set"
  exit 1
fi

# Get current directory where the script is located
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

# Check for pkcs11 config file
PKCS11_CFG="$DIR/.pkcs11_java.cfg"
if [ ! -f "$PKCS11_CFG" ]; then
  echo "File not found:"
  echo "$PKCS11_CFG"
  exit 1
fi

# Get Build Tools Version from project's /gradle/dependencies.gradle file
FILE="$DIR/../gradle/dependencies.gradle"

if [ ! -f "$FILE" ]; then
  echo "File not found:"
  echo "$FILE"
  exit 1
fi

BUILD_TOOLS_VERSION=$(cat $FILE | grep 'buildTools' | sed 's/ //g' | cut -d '"' -f 2)


# Get Min/Max SDK Version
MIN_SDK_VERSION=$(cat $FILE | grep 'minSdk' | grep -o '[[:digit:]]*')
MAX_SDK_VERSION=$(cat $FILE | grep 'compileSdk' | grep -o '[[:digit:]]*')


# Locate unsigned release
UNSIGNED_APK_DIR="$DIR/../sampleapp/build/outputs/apk/release"
UNSIGNED_APK_DIR_FILE_LIST=$(ls "$UNSIGNED_APK_DIR"/)

# Get Yubikey PIN
read -p "Please enter your Yubikey PIV pin: " YUBI_PIN

for FILE in $UNSIGNED_APK_DIR_FILE_LIST; do
  if echo "$FILE" | grep -q ".*release-unsigned.apk"; then
    # Zipalign
    echo "zipaligning the apk"
    echo ""
    "$ANDROID_SDK"/build-tools/"$BUILD_TOOLS_VERSION"/zipalign 4 \
    "$UNSIGNED_APK_DIR"/"$FILE" \
    "$UNSIGNED_APK_DIR"/"$FILE".tmp &&
    mv -vf "$UNSIGNED_APK_DIR"/"$FILE".tmp \
    "$UNSIGNED_APK_DIR"/"$FILE"

    echo ""
    echo "Signing"
    echo ""

    SIGNED_APK_NAME=$(echo "$FILE" | sed 's+unsigned+signed+g')

    # Signing
    "$ANDROID_SDK"/build-tools/"$BUILD_TOOLS_VERSION"/apksigner sign \
    --ks NONE \
    --ks-pass "pass:$YUBI_PIN" \
    --min-sdk-version "$MIN_SDK_VERSION" \
    --max-sdk-version "$MAX_SDK_VERSION" \
    --provider-class sun.security.pkcs11.SunPKCS11 \
    --provider-arg "$PKCS11_CFG" \
    --ks-type PKCS11 \
    --out "$UNSIGNED_APK_DIR"/"$SIGNED_APK_NAME" \
    "$UNSIGNED_APK_DIR"/"$FILE"

    # Output Verfication
    "$ANDROID_SDK"/build-tools/"$BUILD_TOOLS_VERSION"/apksigner verify --verbose \
    "$UNSIGNED_APK_DIR"/"$SIGNED_APK_NAME"
  fi
done

unset YUBI_PIN
exit 0
