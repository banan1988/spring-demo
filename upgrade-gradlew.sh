#!/bin/bash

VERSION=${1:-5.6.2}

gradle wrapper --gradle-version "${VERSION}"
