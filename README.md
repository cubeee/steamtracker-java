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
* Easy deploying as images with Docker
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

SteamTracker in production is easiest to run as a Docker container. ``backend`` and ``updater`` modules have Dockerfiles and ``buildDockerImage`` tasks that can be used to build their respective images.

You should make sure that there is a database and its user set up for SteamTracker prior to running the containers, Flyway will handle the migrations on start.

Example run command:
````
docker run -p 8891:8891 --link postgres:postgres --name steamtracker-backend steamtracker
````

## Contributing

for now: fork, code, submit pull requests, make issues