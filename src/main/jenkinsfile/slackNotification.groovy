#!/usr/bin/env groovy

def call(String level, String msg) {
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

return this
