#!/bin/sh
# Maven Wrapper script - downloads and runs Maven
set -e

MAVEN_VERSION="3.9.6"
MAVEN_HOME="$HOME/.m2/wrapper/dists/apache-maven-${MAVEN_VERSION}"
MAVEN_BIN="${MAVEN_HOME}/bin/mvn"

if [ ! -f "${MAVEN_BIN}" ]; then
    echo "Downloading Maven ${MAVEN_VERSION}..."
    mkdir -p "${MAVEN_HOME}"
    DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MAVEN_VERSION}/apache-maven-${MAVEN_VERSION}-bin.zip"
    TEMP_FILE=$(mktemp /tmp/maven-XXXXXX.zip)
    curl -fsSL "${DOWNLOAD_URL}" -o "${TEMP_FILE}"
    unzip -qo "${TEMP_FILE}" -d "${MAVEN_HOME}/.."
    mv "${MAVEN_HOME}/../apache-maven-${MAVEN_VERSION}"/* "${MAVEN_HOME}/" 2>/dev/null || true
    rm -f "${TEMP_FILE}"
    rmdir "${MAVEN_HOME}/../apache-maven-${MAVEN_VERSION}" 2>/dev/null || true
    chmod +x "${MAVEN_BIN}"
fi

exec "${MAVEN_BIN}" "$@"
