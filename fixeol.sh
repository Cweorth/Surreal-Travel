#!/bin/bash

# Fixes missing EOL characters in files

echo "Fixing missing EOL at the end of file"
find . -path ./.git -prune -o -type f -print | xargs -n1 -I% bash -c "tail -c1 % | read -r _ || (echo %; echo >> %)"
