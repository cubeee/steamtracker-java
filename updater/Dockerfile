FROM java:openjdk-8-jre-alpine

ENTRYPOINT java -Xmx64m -jar /app/steamtracker-updater.jar --spring.profiles.active=production

COPY ./build/steamtracker-updater.jar /app/