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

# Get current directory where the script is located
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

# Find all file paths within projectDir/docs/ that contains http:FIX_DOKKA_LINKDS/
mapfile -t INPUT_FILES < <(grep -rnwl "$DIR/../docs/" -e "(http://FIX_DOKKA_LINKS/.*)")

# Replace the urls with appropriate file path for Mkdocs to generate proper urls
for INPUT_FILE in ${INPUT_FILES[*]}; do
  echo "Fixing line in $INPUT_FILE"
  sed -i 's+http://FIX_DOKKA_LINKS+../../..+gI' $INPUT_FILE
done