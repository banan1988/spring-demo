#!/bin/bash

VERSION=${1:-6.0.1}

gradle wrapper --gradle-version "${VERSION}"
