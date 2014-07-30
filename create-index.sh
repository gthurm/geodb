#!/bin/sh

curl -X DELETE localhost:9200/location
echo
curl -X POST   localhost:9200/location
echo
curl -X PUT    localhost:9200/location/completer/_mapping -d @mapping.json
echo
