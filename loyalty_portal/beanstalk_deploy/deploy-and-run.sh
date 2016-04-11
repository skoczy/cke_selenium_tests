#!/bin/sh
export JAVA_OPTIONS="$JAVA_OPTIONS -Djava.security.egd=file:/dev/./urandom"
/usr/bin/java -Dapplication.environment=$Env -jar /opt/sitemaster.jar