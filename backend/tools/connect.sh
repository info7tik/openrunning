#!/bin/bash

response=$(curl -X POST -H 'Content-Type: application/json' \
  -d '{"email": "toto@toto", "password": "supertoto"} ' \
  http://localhost:8080/user/signin)
echo $response

export TOKEN=$(echo $response | jq .token)

echo "execute the following command: 'export TOKEN=$TOKEN'"
echo "then, use 'curl -X GET -H \"Authorization: Bearer \$TOKEN\" -H 'Content-Type: application/json' http_url'"
