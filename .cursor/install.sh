#!/usr/bin/env bash
# Idempotent Cloud Agent setup for word-indexer.
# The project targets Java 8 and uses Gradle 6.3 / Kotlin 1.3.70, which are not
# compatible with the Java 21 that ships in the base image. We therefore install
# a pinned Temurin JDK 8, expose it via a stable symlink, and build the boot jars.
set -euo pipefail

REPO_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
JDKS_DIR="$HOME/jdks"
JDK_LINK="$JDKS_DIR/jdk8"
JDK_URL="https://api.adoptium.net/v3/binary/latest/8/ga/linux/x64/jdk/hotspot/normal/eclipse?project=jdk"

mkdir -p "$JDKS_DIR"

if [ ! -x "$JDK_LINK/bin/java" ]; then
  # Reuse an already-extracted Temurin 8 if present, otherwise download it.
  extracted="$(ls -d "$JDKS_DIR"/jdk8u* 2>/dev/null | sort | tail -n1 || true)"
  if [ -z "$extracted" ] || [ ! -x "$extracted/bin/java" ]; then
    echo "Installing Temurin JDK 8..."
    tmp_tar="$(mktemp /tmp/jdk8.XXXXXX.tar.gz)"
    curl -fL -o "$tmp_tar" "$JDK_URL"
    tar -xzf "$tmp_tar" -C "$JDKS_DIR"
    rm -f "$tmp_tar"
    # Adoptium extracts into a versioned dir like jdk8u504-b01.
    extracted="$(ls -d "$JDKS_DIR"/jdk8u* 2>/dev/null | sort | tail -n1)"
  fi
  # Point the stable symlink at the resolved JDK directory.
  ln -sfn "$extracted" "$JDK_LINK"
fi

export JAVA_HOME="$JDK_LINK"
export PATH="$JAVA_HOME/bin:$PATH"
java -version

cd "$REPO_DIR"
# Build the runnable boot jars (compiles Kotlin + assembles gateway.jar/node.jar).
./gradlew assemble --no-daemon --console=plain
