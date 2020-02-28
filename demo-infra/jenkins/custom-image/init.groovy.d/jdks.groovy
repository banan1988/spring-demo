#!/usr/bin/env groovy

import hudson.model.JDK
import hudson.tools.InstallSourceProperty
import hudson.tools.ZipExtractionInstaller

javaTools = [
        ['name': 'openjdk-11', 'url': 'https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_linux-x64_bin.tar.gz', 'subdir': 'jdk-11.0.2'],
]

def descriptor = new JDK.DescriptorImpl()
def List<JDK> installations = []
javaTools.each { javaTool ->
    println("Setting up tool: ${javaTool.name}")
    def installer = new ZipExtractionInstaller(javaTool.label as String, javaTool.url as String, javaTool.subdir as String)
    def jdk = new JDK(javaTool.name as String, null, [new InstallSourceProperty([installer])])
    installations.add(jdk)
}
descriptor.setInstallations(installations.toArray(new JDK[installations.size()]))
descriptor.save()
