#!/bin/bash

# This script checks that line terminators in all repository files (excluding
# those in the .git directory) feature unix line terminators.
#
# Usage:
#
# ./check_line_terminators.sh
#
# The script will emit exit code 1 if any files that do not use unix line
# terminators are found, 0 otherwise.

# cd to the root of the repository
cd `dirname $0`/..

# Find and print files with non-unix line terminators
if find . -path './.git/*' -prune -o -type f -print0 | xargs -0 grep -I -l $'\r$'; then
    echo -e '\e[31mERROR: found files with CRLF line endings. See above.\e[39m'
    exit 1
fi
