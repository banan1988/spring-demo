#!/usr/bin/env groovy

import hudson.security.*
import jenkins.model.*

def username = System.getenv("SYSTEM_USERNAME") != null ? System.getenv("SYSTEM_USERNAME") : "changeMe"
def password = System.getenv("SYSTEM_PASSWORD") != null ? System.getenv("SYSTEM_PASSWORD") : "changeMe"
def allowsSignup = false
def allowAnonymousRead = false

println "--> Added system user: ${username}"
def hudsonRealm = new HudsonPrivateSecurityRealm(allowsSignup)
hudsonRealm.createAccount(username, password)
Jenkins.instance.setSecurityRealm(hudsonRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(allowAnonymousRead)
Jenkins.instance.setAuthorizationStrategy(strategy)

Jenkins.instance.save()
