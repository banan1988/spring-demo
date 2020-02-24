#!/usr/bin/env groovy

def _GLOBAL_TIMEOUT_MINUTES_ = 60
def _SCM_TIMEOUT_MINUTES_ = 10
def _SONAR_TIMEOUT_MINUTES_ = 10
def _JDK_ = 'openjdk-11'
def slackNotification

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
//                    checkout scm
                    checkout([
                            $class                           : 'GitSCM',
                            branches                         : [[name: env.BRANCH_NAME]],
                            doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
                            extensions                       : scm.extensions,
                            submoduleCfg                     : scm.submoduleCfg,
                            userRemoteConfigs                : [[
                                                                        name   : 'origin',
                                                                        refspec: '+refs/heads/master:refs/remotes/origin/master +refs/heads/*:refs/remotes/origin/*',
                                                                        url    : 'https://github.com/banan1988/spring-demo.git'
                                                                ]]
                    ])
                }
            }
        }

        stage('Load libraries') {
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }

                script {
                    slackNotification = load("${env.WORKSPACE}/src/main/jenkinsfile/slackNotification.groovy")
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }

                script {
                    // Error: ./gradlew: Permission denied
                    // FIX: git update-index --chmod=+x gradlew
                    // FIX: sh 'chmod +x gradlew'
                    sh './gradlew build -x test --profile'
                }
            }
        }

        stage("Parallel tests") {
            parallel {
                stage("JUnit tests") {
                    steps {
                        script {
                            env.FAILED_STAGE_NAME = env.STAGE_NAME
                        }

                        echo "JUnit tests"
                    }
                }
                stage("Integration tests") {
                    steps {
                        script {
                            env.FAILED_STAGE_NAME = env.STAGE_NAME
                        }

                        echo "Integration tests"
                    }
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
            script {
                slackNotification('SUCCESS', "Successful !")
            }
        }
        unstable {
            script {
                slackNotification('WARN', "Unstable on stage *${env.FAILED_STAGE_NAME}* !")
            }
        }
        failure {
            script {
                slackNotification('ERROR', "Failed on stage *${env.FAILED_STAGE_NAME}* !")
            }
        }
        aborted {
            script {
                slackNotification('ERROR', "Aborted on stage *${env.FAILED_STAGE_NAME}* !")
            }
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
