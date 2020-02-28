#!/usr/bin/env groovy

import jenkins.model.*

def numExecutors = System.getenv("SYSTEM_NUMBER_OF_EXECUTORS") != null ? Integer.parseInt(System.getenv("SYSTEM_NUMBER_OF_EXECUTORS")) : 1

println "--> Set Number of Executors to: ${numExecutors}"
Jenkins.instance.setNumExecutors(numExecutors)
