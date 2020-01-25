#!/usr/bin/env groovy

def _GLOBAL_TIMEOUT_MINUTES_ = 60
def _SCM_TIMEOUT_MINUTES_ = 10
def _SONAR_TIMEOUT_MINUTES_ = 10
def _JDK_ = 'openjdk-11'

pipeline {
//    agent any
    agent {
        // run on node with 'master' label
        label 'master'
        // run on node with any label except 'master'
        // label '!master'
    }

    tools {
        jdk _JDK_
    }

    environment {
        DUMMY_ENV = ""
    }

    options {
        timeout(time: _GLOBAL_TIMEOUT_MINUTES_, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '2'))
        ansiColor('xterm')
        timestamps()
        parallelsAlwaysFailFast()
    }

    stages {
        stage('Preparing') {
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }

                cleanWs()
            }
        }

        stage('Checkout repository') {
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }

                timeout(time: _SCM_TIMEOUT_MINUTES_, unit: 'MINUTES') {
                    checkout scm
                }
            }
        }

        stage('Build & test') {
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }

                script {
                    // Error: ./gradlew: Permission denied
                    // FIX: git update-index --chmod=+x gradlew
                    // FIX: sh 'chmod +x gradlew'
                    sh './gradlew build --profile'
                }
            }
        }

        stage('SonarQube analysis') {
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }

                sshagent(['302022a7-0abc-47f9-b20c-51939d278171']) {
                    // sonar needs to know where is master to calculate coverage of new code
                    sh "git fetch origin +refs/heads/master:refs/remotes/origin/master"
                }

                timeout(time: _SONAR_TIMEOUT_MINUTES_, unit: 'MINUTES') {
                    withSonarQubeEnv('demo') {
                        script {
                            // Jenkins is hosted on docker - so requires docker's host of sonar
                            sh './gradlew sonarqube -x test --profile -Dsonar.host.url=http://sonarqube:9000'
                        }
                    }
                }
            }
        }

        stage("Quality Gate") {
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }

                timeout(time: _SONAR_TIMEOUT_MINUTES_, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage("Release") {
            when {
                branch 'master'
            }
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }

                sshagent(['302022a7-0abc-47f9-b20c-51939d278171']) {
                    // https://axion-release-plugin.readthedocs.io/en/latest/configuration/ci_servers/#jenkins
                    // Because Jenkins will check out git repositories in a detached head state,
                    // two flags should be set when running the release task:
                    // -Prelease.disableChecks -Prelease.pushTagsOnly
                    sh './gradlew release -Prelease.disableChecks -Prelease.pushTagsOnly -x test --profile'
                }

                script {
                    env.CURRENT_VERSION = sh(script: './gradlew currentVersion -q -Prelease.quiet', returnStdout: true).trim()
                    slackNotification('SUCCESS', "Released version - ${env.CURRENT_VERSION}")
                }
            }
        }

        stage("Publish") {
            when {
                branch 'master'
            }
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }

                script {
                    sh './gradlew publish -x test --profile'
                    sh './gradlew currentVersion'

                    slackNotification('SUCCESS', "Published version - ${env.CURRENT_VERSION}")
                }
            }
        }
    }

    post {
        success {
            slackNotification('SUCCESS', "Successful !")
        }
        unstable {
            slackNotification('WARN', "Unstable on stage *${env.FAILED_STAGE_NAME}* !")
        }
        failure {
            slackNotification('ERROR', "Failed on stage *${env.FAILED_STAGE_NAME}* !")
        }
        aborted {
            slackNotification('ERROR', "Aborted on stage *${env.FAILED_STAGE_NAME}* !")
        }
        always {
            echo "Archive JARs:"
            archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true

            echo "Publish Gradle Test Report:"
            publishHTML([
                    allowMissing         : false,
                    alwaysLinkToLastBuild: false,
                    keepAll              : true,
                    reportDir            : 'build/reports/tests/test',
                    reportFiles          : 'index.html',
                    reportName           : 'Gradle Test Report',
                    reportTitles         : ''
            ])

            echo "Publish Gradle Profile Reports:"
            publishHTML([
                    allowMissing         : false,
                    alwaysLinkToLastBuild: false,
                    keepAll              : true,
                    reportDir            : 'build/reports/profile',
                    reportFiles          : 'profile-*.html',
                    reportName           : 'Gradle Profile Reports',
                    reportTitles         : ''
            ])

            echo "Records test results from main project:"
            junit 'build/test-results/**/*.xml'
            echo "Records test results from subprojects:"
            junit '**/build/test-results/**/*.xml'
        }
    }
}

def slackNotification(String level, String msg) {
    def color = '#439fe0'
    def message = "Build <${env.BUILD_URL}|${env.JOB_NAME}#${env.BUILD_NUMBER}>/<${env.RUN_DISPLAY_URL}|BlueOcean>: ${msg}" as Object
    if (level == 'SUCCESS') {
        color = '#27a21b'
    } else if (level == 'WARN') {
        color = '#ff4500'
    } else if (level == 'ERROR') {
        color = '#ce2231'
    }

    slackSend channel: 'jenkins', color: color, message: message, teamDomain: 'banan1988', tokenCredentialId: '11ca9396-177f-417d-bc3c-9d65f7369f0a'
}
