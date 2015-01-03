#!/bin/bash

# Fixes missing EOL characters in files

find . -path ./.git -prune -o -type f -print | xargs -n1 -I% bash -c "tail -c1 % | read -r _ || echo >> %"
