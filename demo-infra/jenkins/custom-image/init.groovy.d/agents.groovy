#!/usr/bin/env groovy

import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import hudson.model.Node.*
import hudson.model.Node.Mode
import hudson.plugins.sshslaves.*
import hudson.plugins.sshslaves.verifiers.*
import hudson.security.*
import hudson.slaves.*
import hudson.slaves.EnvironmentVariablesNodeProperty.Entry
import jenkins.model.*

def javaHome = System.getenv("AGENT_JAVA_HOME") != null ? System.getenv("AGENT_JAVA_HOME") : "/usr/local/openjdk-11"

def ComputerLauncher launcher = new SSHLauncher(
        "jenkins-slave", // host
        22, // port
        "agent", // credentialsId
        null, null, null, null, null, null, null,
        new NonVerifyingKeyVerificationStrategy()
)

// Define a "Permanent Agent"
def DumbSlave agent = new DumbSlave(
        "slave",
        "/home/jenkins",
        launcher
)
agent.nodeDescription = ""
agent.numExecutors = 1
agent.labelString = ""
agent.mode = Mode.NORMAL
agent.retentionStrategy = new RetentionStrategy.Always()

List<Entry> env = new ArrayList<Entry>();
env.add(new Entry("JAVA_HOME", javaHome))
EnvironmentVariablesNodeProperty envProperties = new EnvironmentVariablesNodeProperty(env);
agent.getNodeProperties().add(envProperties)

Jenkins.instance.addNode(agent)

Jenkins.instance.save()
