FROM openjdk:15-jdk-alpine

RUN mkdir -p /usr/src/jiraParser/
WORKDIR /usr/src/jiraParser/

COPY . /usr/src/jiraParser/

CMD ["./gradlew", "run", "--no-daemon"]
