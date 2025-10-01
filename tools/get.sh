#!/bin/bash

url=$1

TOKEN_FILE="openrunning-token.txt"
API_HOST="localhost"
API_PORT=8080

function http_request() {
  curl                                                               \
    -X GET                                                           \
    -H "Authorization: Bearer $1"                                    \
    -H "Content-Type: application/json"                              \
    http://$API_HOST:$API_PORT/$2
}

if [ -f $TOKEN_FILE ]; then
  token=$(cat $TOKEN_FILE)
else
  echo "token file '$TOKEN_FILE' does not exist."
  echo "sign in to the application with 'sign.sh signin'."
  exit 5
fi

if [ -z "$token" ]; then
  echo "authentication token is required, please execute 'sign.sh signin'"
fi

if [ -z "$url" ]; then
  echo "url is required as second parameter"
  echo "usage: $0 get_url"
  exit 3
fi

http_request $token $url

if [ $? -ne 0 ]; then
  echo "http request failure"
fi
