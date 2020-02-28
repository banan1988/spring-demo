#!/usr/bin/env groovy

import jenkins.model.*

def systemLocale = System.getenv("SYSTEM_LOCALE") != null ? System.getenv("SYSTEM_LOCALE") : "en_US"

def pluginWrapper = Jenkins.instance.getPluginManager().getPlugin('locale')
def plugin = pluginWrapper.getPlugin()

println "--> Set System Locale to: ${systemLocale}"
plugin.setSystemLocale(systemLocale)
plugin.setIgnoreAcceptLanguage(true)

Jenkins.instance.save()
