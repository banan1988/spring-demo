@echo off
Rem Below script will kill the process which has already binded address/port and produce error like:
Rem ERROR: for jenkins-master  Cannot start service jenkins-master: driver failed programming external connectivity on endpoint jenkins-master (27d08c70465e93f86c7630bfce959790388a334be703abfd452483a0e72e17ad): Error starting userland proxy: listen tcp 0.0.0.0:50000: bind: Only one usage of each socket address (protocol/network address/port) is normally permitted.
Rem Encountered errors while bringing up the project.

netstat -ano | grep "%1" | awk '{print $NF}' | sort | uniq | xargs -I{} taskkill /F /PID {}
