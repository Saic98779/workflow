#!/usr/bin/env bash
# Runs on the target server. Replaces the running workflow container with the given image.
# Usage: deploy.sh <image:tag>
set -euo pipefail

IMAGE="${1:?usage: deploy.sh <image:tag>}"
CONTAINER=workflow
PORT=8082
FILES_DIR=/home/metaverseedu/public_html/workflowfiles

mkdir -p "$FILES_DIR" 2>/dev/null || sudo mkdir -p "$FILES_DIR"

# Run as the owner of the upload dir so files written by the app keep the existing ownership
RUN_AS="$(stat -c '%u:%g' "$FILES_DIR")"

docker rm -f "$CONTAINER" >/dev/null 2>&1 || true

docker run -d \
  --name "$CONTAINER" \
  --restart unless-stopped \
  --user "$RUN_AS" \
  -p "$PORT:8082" \
  -v "$FILES_DIR:$FILES_DIR" \
  "$IMAGE"

echo "Waiting for $CONTAINER to start..."
for i in $(seq 1 30); do
  if curl -s -o /dev/null "http://localhost:$PORT/workflow/v3/api-docs"; then
    echo "$CONTAINER is up on port $PORT"
    # Keep only the last 3 workflow images
    docker images workflow --format '{{.Repository}}:{{.Tag}}' | grep -v ':latest$' | tail -n +4 | xargs -r docker rmi >/dev/null 2>&1 || true
    exit 0
  fi
  sleep 5
done

echo "$CONTAINER did not respond within 150s. Last logs:"
docker logs --tail 100 "$CONTAINER"
exit 1
