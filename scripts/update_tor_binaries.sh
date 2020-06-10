#!/usr/bin/env bash

# Get current directory where the script is located
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

cd "$DIR"/../external/tor-android || exit 1
git fetch

CURRENT_BRANCH=$( git status | grep "On branch " | cut -d ' ' -f 3 )
LATEST_BRANCH=
LATEST_BRANCH_VERSION_NUM=

update_latest_branch() {
  LATEST_BRANCH="$1"
  LATEST_BRANCH_VERSION_NUM=$( echo "$1" | cut -d '-' -f 5  | head -c 7 | sed "s/[\.]//g" )
}

is_branch_newer() {
  local BRANCH_VERSION=$( echo "$1" | cut -d '-' -f 5  | head -c 7 | sed "s/[\.]//g" )

  if [ "$BRANCH_VERSION" -gt "$LATEST_BRANCH_VERSION_NUM" ]; then
    return 0
  fi
  return 1
}

is_newer_binary_available() {
  local BRANCHES=
  local BRANCH=
  mapfile -t BRANCHES < <( git branch -a | grep "remotes/origin/tor-android-binary-tor-" | cut -d '/' -f 3)

  if echo "$CURRENT_BRANCH" | grep "tor-android-binary-tor-"; then
    update_latest_branch "$CURRENT_BRANCH"
  else
    update_latest_branch ${BRANCHES[0]}
  fi

  local NEWER_BINARIES_AVAILABLE=false
  for BRANCH in ${BRANCHES[*]}; do
    if is_branch_newer $BRANCH; then
      update_latest_branch $BRANCH
      NEWER_BINARIES_AVAILABLE=true
    fi
  done

  if $NEWER_BINARIES_AVAILABLE; then
    return 0
  else
    return 1
  fi
}

build() {
  if ! is_newer_binary_available; then
    exit 0
  fi

  git checkout "$LATEST_BRANCH" && git pull

  if ! ./tor-droid-make.sh fetch; then
    echo "Unable to fetch submodules for tor-android"
    return 1
  fi

  if ./tor-droid-make.sh build -b release; then
    echo "Build failed"
    return 1
  fi

  return 0
}

EXIT_ARG=0
if [ "$1" == "build" ]; then

  if [ -z "$ANDROID_HOME" ]; then
    echo "ANDROID_HOME environment variable not set"
    exit 1
  fi

  if [ -z "$ANDROID_NDK_HOME" ]; then
    echo "ANDROID_NDK_HOME environment variable not set"
    exit 1
  fi

  if build; then
    if [ "$CURRENT_BRANCH" != "master" ]; then
      git branch -D "$CURRENT_BRANCH"
    fi
  else
    git add --all
    git stash
    git stash drop
    git checkout "$CURRENT_BRANCH"
    ./tor-droid-make.sh fetch
    git branch -D "$LATEST_BRANCH"
    EXIT_ARG=1
  fi

elif [ "$1" == "update_version_number" ]; then

  TOR_VERSION=$( git -C external/tor describe --tags --always | cut -d '-' -f 2 )
  sed -i "s/TOR_BINARY_VERSION.*/TOR_BINARY_VERSION=$TOR_VERSION/" "$DIR/../.versions"
  unset TOR_VERSION

elif [ "$1" == "check_for_update" ]; then

  if is_newer_binary_available; then
    echo ""
    echo "---------------------------------------------------"
    echo "|     A newer tor binary version is available     |"
    echo "---------------------------------------------------"
    echo ""
  fi

else

  echo "Invalid Arguments"
  echo ""
  echo "Arguments:"
  echo "            build                   checks for newer version and builds tor binaries"
  echo "            update_version_number   increases TOR_BINARY_VERSION in .versions file"
  echo "            check_for_update        checks if a newer tor binary version is available"
  EXIT_ARG=1

fi

unset CURRENT_BRANCH LATEST_BRANCH LATEST_BRANCH_VERSION_NUM TOR_VERSION

exit $EXIT_ARG
