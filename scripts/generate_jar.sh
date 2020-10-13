#!/bin/bash

. `dirname "$0"`/_common_helpers.sh

# Source https://stackoverflow.com/questions/59895/how-to-get-the-source-directory-of-a-bash-script-from-within-the-script-itself
# These variables are useful to invoke the build script regardless of the terminal current directory
SCRIPT_DIR="$(cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd)"
PROJECT_ROOT="${SCRIPT_DIR}/../"

function gradle_build { "${PROJECT_ROOT}"/gradlew assemble -p "${PROJECT_ROOT}"; }

log_info "Generating jar"

gradle_build

log_info "Jar can be found under build/libs/crypto-0.0.1-SNAPSHOT.jar"
log_info "To run it, go to the root project directory and run: java -jar build/libs/crypto-0.0.1-SNAPSHOT.jar"





