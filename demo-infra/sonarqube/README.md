# How to start ?
* Start postgres
* Create user and database
```shell script
./createdb.sh
```
* Start sonar
```shell script
./up.sh
```
* Open in browser http://localhost:9000

# Sonar fails because of:
```docker
[2] bootstrap checks failed
[1]: max file descriptors [10240] for elasticsearch process is too low, increase to at least [65535]
[2]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
```
Execute command before run up.sh:
```shell script
sudo sysctl -w vm.max_map_count=262144
sudo sysctl -w fs.file-max=65535
```
Create and add new lines to /etc/security/limits.d/docker.conf
```shell script
#Each line describes a limit for a user in the form:
#
#<domain>  <type>  <item>    <value>

*	hard	nofile		65535
*	soft	nofile		65535
*	hard	nproc	 	65535
*	soft	nproc	 	65535
```
