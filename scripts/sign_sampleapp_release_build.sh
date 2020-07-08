#!/usr/bin/env bash
# /*
# * Copyright (C) 2020 Matthew Nelson
# *
# * This program is free software: you can redistribute it and/or modify
# * it under the terms of the GNU General Public License as published by
# * the Free Software Foundation, either version 3 of the License, or
# * (at your option) any later version.
# *
# * This program is distributed in the hope that it will be useful,
# * but WITHOUT ANY WARRANTY; without even the implied warranty of
# * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# * GNU General Public License for more details.
# *
# * You should have received a copy of the GNU General Public License
# * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
