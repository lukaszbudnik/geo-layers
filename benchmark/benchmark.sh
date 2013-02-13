#!/bin/bash
ab -n 10000 -c 100 -p postfile -T 'application/x-www-form-urlencoded' -H 'X-GEO-LAYERS-CLIENT-ID: lukasz.budnik@hotmail.com' -H 'X-GEO-LAYERS-CLIENT-TOKEN: token' http://localhost:9000/api/v1/locations/performanceTest
