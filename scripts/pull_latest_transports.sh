#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

cd "$DIR"/../external/AndroidPluggableTransports || exit 1
git pull
exit 0