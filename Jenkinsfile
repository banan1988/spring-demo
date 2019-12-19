#!/usr/bin/env groovy

def _GLOBAL_TIMEOUT_MINUTES_ = 60

pipeline {
    agent {
        label 'master'
    }

    triggers {
        cron(env.BRANCH_NAME == "jenkins-condition-test" ? 'H/15 * * * *' : "")
    }

    options {
        timeout(time: _GLOBAL_TIMEOUT_MINUTES_, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '2'))
        ansiColor('xterm')
        timestamps()
        parallelsAlwaysFailFast()
    }

    stages {
        stage('Start') {
            steps {
                script {
                    sh "env"
                }
            }
        }

        stage('You shall not pass ?') {
            when {
                branch 'jenkins-condition-test'
                not {
                    anyOf {
                        triggeredBy 'TimerTrigger'
                        triggeredBy 'User'
                    }
                }
            }
            steps {
                error("jenkins-condition-test branch can be triggered only by Timer or User")
            }
        }

        stage("C-mon - jenkins-condition-test or PR-") {
            when {
                anyOf {
                    branch 'jenkins-condition-test'
                    branch 'PR-*'
                }
            }
            steps {
                script {
                    sh "ls"
                }
            }
        }

        stage("C-mon") {
            steps {
                script {
                    sh "ls"
                }
            }
        }
    }
}
