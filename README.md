## Casino
This is a proof of concept project to demonstrate how quick and easy it is to create an application with Java and Spring Boot. The application is build using Spring Boot 3.1.1 and Java 17.

### Building and running
To build the application run gradle build
To run application use command ./gradlew bootRun


### Application Health
to check application health use actuator on http://localhost:8080/actuator/health

To test the APIs that are exposed user Swagger on http://localhost:8080/casino/swagger-ui.html