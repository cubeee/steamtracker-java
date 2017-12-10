FROM java:openjdk-8-jre-alpine

ENTRYPOINT java -Xmx64m -jar /app/steamtracker-backend.jar --spring.profiles.active=production

COPY ./build/steamtracker-backend.jar /app/