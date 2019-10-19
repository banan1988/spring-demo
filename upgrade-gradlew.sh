#!/bin/bash

VERSION=${1:-5.6.3}

gradle wrapper --gradle-version "${VERSION}"
