#! /bin/bash

go build
# Enable TLSv1.3 on Go 1.12
export GODEBUG=tls13=1
export BIND_ADDRESS=:8085
exec ./federation-tester
