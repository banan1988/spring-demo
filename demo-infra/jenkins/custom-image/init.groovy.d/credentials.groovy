#!/usr/bin/env groovy

import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import hudson.security.*
import jenkins.model.*

def scope = CredentialsScope.GLOBAL
def domain = Domain.global()
def store = SystemCredentialsProvider.getInstance().getStore()

// random UUID
//java.util.UUID.randomUUID().toString()

def githubUsername = System.getenv("CREDENTIALS_GITHUB_USERNAME") != null ? System.getenv("CREDENTIALS_GITHUB_USERNAME") : "changeMe"
def githubPassword = System.getenv("CREDENTIALS_GITHUB_PASSWORD") != null ? System.getenv("CREDENTIALS_GITHUB_PASSWORD") : "changeMe"

def agentSshKeyPath = System.getenv("CREDENTIALS_AGENT_SSH_KEY_PATH") != null ? System.getenv("CREDENTIALS_AGENT_SSH_KEY_PATH") : "/tmp/changeMe"
def agentSshKeyUsername = System.getenv("CREDENTIALS_AGENT_SSH_KEY_USERNAME") != null ? System.getenv("CREDENTIALS_AGENT_SSH_KEY_USERNAME") : "changeMe"
def agentSshKeyPassword = System.getenv("CREDENTIALS_AGENT_SSH_KEY_PASSWORD") != null ? System.getenv("CREDENTIALS_AGENT_SSH_KEY_PASSWORD") : "changeMe"

println "--> Create github User/Pass Credentials for: ${githubUsername}"
def github = new UsernamePasswordCredentialsImpl(scope, "github", "", githubUsername, githubPassword)
store.addCredentials(domain, github)

println "--> Create agent SSH Credentials for: ${agentSshKeyUsername}"
def agent = new BasicSSHUserPrivateKey(scope, "agent",
        agentSshKeyUsername,
        new BasicSSHUserPrivateKey.FileOnMasterPrivateKeySource(agentSshKeyPath),
        agentSshKeyPassword,
        "")
store.addCredentials(domain, agent)

Jenkins.instance.save()
