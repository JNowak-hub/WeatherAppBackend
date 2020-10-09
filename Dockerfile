FROM maven:3.6.3-jdk-11 AS build
ADD . /backend
WORKDIR /backend
RUN mvn install
RUN mv /backend/target/weather-*.jar /weather/backend.jar
FROM gcr.io/distroless/java:11
COPY --from=build /weather/backend.jar /weather/backend.jar
WORKDIR /backend
EXPOSE 8080
CMD /weather/backend.jar