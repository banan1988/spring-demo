#!/usr/bin/env groovy

def _GLOBAL_TIMEOUT_MINUTES_ = 60
def _SCM_TIMEOUT_MINUTES_ = 10
def _SONAR_TIMEOUT_MINUTES_ = 10
def _JDK_ = 'openjdk-12'

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

//    parameters {
//        string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')
//    }

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
//                echo "Hello ${params.PERSON}"
                cleanWs()
            }
        }

        stage('Checkout repository') {
            steps {
                timeout(time: _SCM_TIMEOUT_MINUTES_, unit: 'MINUTES') {
                    checkout scm
                }
            }
        }

        stage('Build & test') {
            steps {
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
                    // https://axion-release-plugin.readthedocs.io/en/latest/configuration/ci_servers/#jenkins
                    // Because Jenkins will check out git repositories in a detached head state,
                    // two flags should be set when running the release task:
                    // -Prelease.disableChecks -Prelease.pushTagsOnly
                    sh './gradlew release -Prelease.disableChecks -Prelease.pushTagsOnly -x test --profile'
                    sh './gradlew currentVersion'
                }
            }
        }

        stage("Publish") {
            when {
                branch 'master'
            }
            steps {
                script {
                    sh './gradlew publish -x test --profile'
                    sh './gradlew currentVersion'
                }
            }
        }
    }

    post {
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
