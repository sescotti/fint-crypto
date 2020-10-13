#!/bin/bash

. `dirname "$0"`/_common_helpers.sh

VERSION=$1

if [ -z $VERSION ]; then
	echo "You must provide a version to redeploy"
	exit 1
fi

verify_installs docker

IMAGE_NAME="crypto"
IMAGE="$IMAGE_NAME:$VERSION"
CONTAINER_NAME="${IMAGE_NAME}_${VERSION}_container"
OLD_CONTAINER=$(docker ps -a | grep $IMAGE | awk '{print $1}')

# Source https://stackoverflow.com/questions/59895/how-to-get-the-source-directory-of-a-bash-script-from-within-the-script-itself
# These variables are useful to invoke the build script regardless of the terminal current directory
SCRIPT_DIR="$(cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd)"
PROJECT_ROOT="${SCRIPT_DIR}/../"

function gradle_build { "${PROJECT_ROOT}"/gradlew assemble -p "${PROJECT_ROOT}"; }

if [ ! -z $OLD_CONTAINER ]; then
  log_info "Another container with the same version already exists. Stopping and removing that one first."
	docker stop $OLD_CONTAINER
	docker rm $OLD_CONTAINER
fi

log_info "Generating jar"
gradle_build

docker rmi $IMAGE 2>/dev/null

docker build -f `dirname "$0"`/../Dockerfile -t $IMAGE .

docker run --name "$CONTAINER_NAME" -P -d $IMAGE

NEW_CONTAINER=$(docker ps -a | grep $IMAGE | awk '{print $1}')

PORT=$(docker port $CONTAINER_NAME | awk -F':' '{print $2}')

echo ""
log_info "Container running on localhost:$PORT"