#!/usr/bin/env bash
function waitForEndpoint {
  URL=$1
  DELAY=$2
  INTERVAL=$3
  COUNTER=$4
  sleep $DELAY
  echo "Checking endpoint $URL ... "
  while [ $COUNTER -gt 0 ]; do
      CODE=`curl -sL -w "%{http_code}\\n" $URL -o /dev/null`
      COUNTER=$(( $COUNTER - 1 ))
      if [ $CODE -eq 200 ]; then
        echo "Endpoint $URL is up and running!"
        return 0
      fi
      sleep $INTERVAL
  done
  echo "Endpoint is down :( Sadness.."
  return 1
 }
