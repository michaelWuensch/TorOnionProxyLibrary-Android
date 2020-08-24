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

# Get current directory where the script is located
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

# Find all file paths within projectDir/docs/ that contains http:FIX_DOKKA_LINKDS/
mapfile -t INPUT_FILES < <(grep -rnwl "$DIR/../docs/" -e "(http://FIX_DOKKA_LINKS/.*)")

# Replace the static string we're looking for with appropriate file path for Mkdocs to
# generate proper links to files located in different modules.
for INPUT_FILE in ${INPUT_FILES[*]}; do
  echo "Fixing line in: $INPUT_FILE"

  # Det depth of the file location to determine number of directories needed to
  # traverse back to get to "../docs/<this directory>/"
  START_COUNTING=false
  FILE_DEPTH=-1
  IFS='/' read -ra DIRECTORY_ARRAY <<< "$INPUT_FILE"
  for DIRECTORY_NAME in "${DIRECTORY_ARRAY[@]}"; do
    if $START_COUNTING; then
      (( FILE_DEPTH++ ))
    fi

    if [ "$DIRECTORY_NAME" == "docs" ]; then
      START_COUNTING=true
    fi

  done

  # Build string with number of directories needed to get back to the desired dir
  FILE_DEPTH_STRING=
  for (( i = 0; i < "$FILE_DEPTH"; i++ )); do
      FILE_DEPTH_STRING+="../"
  done

  # Replace our static string with the proper directory location of the Dokka doc
  sed -i "s+http://FIX_DOKKA_LINKS+$FILE_DEPTH_STRING+gI" "$INPUT_FILE"
done

exit 0