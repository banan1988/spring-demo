FROM jenkins/jenkins:lts

# if we want to install via apt
USER root
RUN apt-get update && apt-get install -y xmlstarlet
# drop back to the regular jenkins user - good practice
USER jenkins

# copy provided plugins to jenkins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
# install plugins
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# copy provided locale to jenkins
COPY locale.xml ~/locale.xml

# set number of executors
COPY executors.groovy /usr/share/jenkins/ref/init.groovy.d/executors.groovy
# create users
COPY users.groovy /usr/share/jenkins/ref/init.groovy.d/users.groovy

# copy script and jdks to setup jdks
COPY xmlcombine.py /tmp/xmlcombine.py
COPY jdks.xml /tmp/jdks.xml
# combine append jdks.xml to config.xml
RUN python /tmp/xmlcombine.py ~/config.xml /tmp/jdks.xml | xmlstarlet format -s 4 > /tmp/combinedConfigJdk.xml
# update config.xml
RUN cp /tmp/combinedConfigJdk.xml ~/config.xml
