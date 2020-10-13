#!/bin/bash

# Source https://stackoverflow.com/questions/59895/how-to-get-the-source-directory-of-a-bash-script-from-within-the-script-itself
# These variables are useful to invoke the build script regardless of the terminal current directory
SCRIPT_DIR="$(cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd)"
PROJECT_ROOT="${SCRIPT_DIR}/../"

function gradle_build { "${PROJECT_ROOT}"/gradlew bootRun -x test -p "${PROJECT_ROOT}"; }

gradle_build

