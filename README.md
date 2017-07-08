<h1 align="center">
  <a href="https://steam.0x7ff.com/">SteamTracker</a>
  <br>
</h1>

<h4 align="center">A Steam player and game tracker powered by <a href="https://projects.spring.io/spring-boot/" target="_blank">Spring Boot</a>.</h4>

<p align="center">
  <a href="https://travis-ci.org/cubeee/steamtracker">
    <img src="https://camo.githubusercontent.com/4cac641208f0e7594b76ff22fd85b02d270093df/68747470733a2f2f7472617669732d63692e6f72672f6375626565652f737465616d747261636b65722e7376673f6272616e63683d6d6173746572">
  </a>
</p>
<br>
<p align="center">
  <img src="https://github.com/cubeee/steamtracker/raw/master/screenshot.png" />
</p>

## Features

* Leverages microservice architecture
* Easy deploying with Ansible
* Automatic player updating
* Pages for tracked games and players for detailed statistics

## Download

First, fork and/or clone the project

```
git clone git@github.com:cubeee/steamtracker.git
```

## Setup

SteamTracker comes with templates for files that may contain sensitive information.

Copy these files and remove the .dist-extension, then fill with your information:

* backend/src/main/resources/application-development.yml.dist
* backend/src/main/resources/application-production.yml.dist
* updater/src/main/resources/application-development.yml.dist
* updater/src/main/resources/application-production.yml.dist
* shared/src/main/resources/application.yml.dist
* deploy/roles/backend/vars/main.yml.dist
* deploy/roles/updater/vars/main.yml.dist
* deploy/hosts.dist
* deploy/shared-vars.yml.dist

Note: the contents of these template files will always contain the minimum required properties. Keep your private versions up-to-date to avoid any problems caused by configuration mismatches.

## Running

SteamTracker uses a Gradle wrapper to ensure all contributors are on the same version.

The wrapper can be downloaded and updated with the following command:
```
./gradlew wrapper
```

The following are some common commands for each module:
```
# Backend:
./gradlew :backend:bootRun # Run the web application
 
# Updater:
./gradlew :updater:bootRun # Run the player updater
 
# Frontend:
./gradlew :frontend:build # Build and package the frontend assets
./gradlew :frontend:webpack # Build the frontend assets
./gradlew :frontend:webpackWatch # Watch for frontend changes
```

## Deploying

Ansible is used to deploy SteamTracker. Its files are found in the ```deploy/``` directory. Ansible installation instructions can be found [here](https://docs.ansible.com/ansible/intro_installation.html).

Each deployable module has a ```deploy.sh``` script in the module's root directory that are used to perform a deployment.

These deployment playbooks only cover the application itself so the following have to be configured on the remote server before a successful deployment can happen:

* A user with sudo access and a SSH key (default username is ```deployer```, can be changed in ```deploy/shared-vars.yml```)
* Java 8
* Python 2.7 (needed by Ansible)
* PostgreSQL 9.4 with matching configuration from ```<module>/src/main/resources/application-production.yml```:
  - Database (default: ```steamtracker```)
  - User with the specified password (default for both is ```steamtracker```) and privileges for the database

Flyway migrations handle the schema, only the database has to exist.

## Contributing

_work in progress_

for now: fork, code, submit pull requests, make issues