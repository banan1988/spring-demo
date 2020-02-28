# How to install docker on Ubuntu 19.10 (Eoan Ermine)

## 1. Remove old packages and data
```shell script
sudo apt remove docker docker-engine docker.io containerd runc
sudo apt purge docker-ce
sudo rm -rf /var/lib/docker
```

## 2. Setup repository
##### 1. Update the apt package index:
```shell script
sudo apt update
```
##### 2. Install packages to allow apt to use a repository over HTTPS:
```shell script
sudo apt install \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common
```
##### 3. Add Docker’s official GPG key:
```shell script
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```
##### 4. Set up the stable repository:
```shell script
sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
```
##### *5. (2020-02-26) Repository for Ubuntu 19.10 doesn't contain containerd.io package so let's use package from older version:*
```shell script
sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   disco \
   stable"
```

## 3. Install
##### 1. Update the apt package index:
```shell script
sudo apt update
```
##### 2. Install the latest version of Docker Engine - Community and containerd, or go to the next step to install a specific version:
```shell script
sudo apt install docker-ce docker-ce-cli containerd.io
```

## 4. Post-installation
### Manage Docker as a non-root user
##### 1. Create the docker group:
```shell script
sudo groupadd docker
```
##### 2. Add your user to the docker group.
```shell script
sudo usermod -aG docker $USER
```
##### 3. Log out and log back in so that your group membership is re-evaluated.
### Configure Docker to start on boot
```shell script
sudo systemctl enable docker
```

## 5. Works ?
Check running containers:
```shell script
docker ps -a
```
*Output:*
```shell script
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
```
Or download a test image and runs it in a container:
```shell script
docker run --rm hello-world
```
*Output:*
```shell script
Hello from Docker!
This message shows that your installation appears to be working correctly.

To generate this message, Docker took the following steps:
 1. The Docker client contacted the Docker daemon.
 2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
    (amd64)
 3. The Docker daemon created a new container from that image which runs the
    executable that produces the output you are currently reading.
 4. The Docker daemon streamed that output to the Docker client, which sent it
    to your terminal.

To try something more ambitious, you can run an Ubuntu container with:
 $ docker run -it ubuntu bash

Share images, automate workflows, and more with a free Docker ID:
 https://hub.docker.com/

For more examples and ideas, visit:
 https://docs.docker.com/get-started/
```

# Install Compose on Linux systems
## 1. Run this command to download the current stable release of Docker Compose:
```shell script
sudo curl -L "https://github.com/docker/compose/releases/download/1.25.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
```
  
## 2. Apply executable permissions to the binary:
```shell script
sudo chmod +x /usr/local/bin/docker-compose
```

## 3. Works ?
```shell script
docker-compose --version
```
