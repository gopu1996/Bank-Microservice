#Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

#Information around who maintains the image
MAINTAINER pandey.gopal212@gmail.com

# Add the application's jar to the image
COPY target/account-0.0.1-SNAPSHOT.jar account-0.0.1-SNAPSHOT.jar

# execute the application
ENTRYPOINT ["java", "-jar", "account-0.0.1-SNAPSHOT.jar"]


# run this docker file in cmd as follow

# docker build . -t dockergopu/account:account-microservices
#                        |        |              |
#             docker user name   image         tag name

# docker run -p 8080:8080 dockergopu/account:account-microservices
#             |           |            |              |
#           port   docker user name   image         tag name
