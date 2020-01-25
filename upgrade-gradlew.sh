#!/bin/bash

VERSION=${1:-6.1}

gradle wrapper --gradle-version "${VERSION}"
