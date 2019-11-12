FROM jenkins/jenkins:lts

# copy provided plugins to jenkins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
# install plugins
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# set number of executors
COPY executors.groovy /usr/share/jenkins/ref/init.groovy.d/executors.groovy

# if we want to install via apt
USER root
RUN apt-get update && apt-get install -y ruby make more-thing-here
# drop back to the regular jenkins user - good practice
USER jenkins
