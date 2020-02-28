FROM jenkins/jenkins:lts-jdk11

# if we want to install via apt
USER root
RUN apt-get update && apt-get install -y htop
# drop back to the regular jenkins user - good practice
USER jenkins

# copy provided plugins to jenkins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
# install plugins
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# copy init scripts
COPY init.groovy.d /usr/share/jenkins/ref/init.groovy.d
