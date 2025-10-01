#!/bin/bash

# action must be 'signin' or 'signup'
action=$1

TOKEN_FILE="openrunning-token.txt"
API_HOST="localhost"
API_PORT=8080

http_request() {
  curl                                                               \
    -X POST                                                          \
    -H "Content-Type: application/json"                              \
    -d '{"email": "user@monemail.com", "password": "greatpassword"}' \
    http://$API_HOST:$API_PORT/user/$1
}

if [ -z "$action" ]; then
  echo "the action parameter is required:"
  echo "usage: $0 [signup|signin]"
  exit 3
fi

case $action in
  "signup")
    http_request $action
    ;;
  "signin")
    token=$(http_request $action | cut -d ':' -f2 | cut -d '"' -f2)
    echo "saving authentication token to $TOKEN_FILE"
    echo $token > $TOKEN_FILE
    ;;
  *)
    echo "unknwon action $action"
    exit 4
esac
