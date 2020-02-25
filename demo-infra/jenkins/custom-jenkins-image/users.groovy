#!/usr/bin/env groovy

import jenkins.model.*
Jenkins.instance.securityRealm.createAccount("jenkins", "jenkins")
