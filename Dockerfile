# Stage 1: Build the application
FROM maven:3.8.6-openjdk-8 AS build

# Argument to specify the working directory
ARG WORK_DIR

# Set working directory
WORKDIR /app
CMD  apt update  && apt install net-tools

# Copy the project files
COPY ${WORK_DIR}/pom.xml ./pom.xml
COPY ./start.sh ./start.sh

# Copy source files
COPY ${WORK_DIR}/src ./src

# Build the application
RUN mvn -B -f /app/pom.xml package

# Stage 2: Create runtime image
FROM openjdk:8-jdk-alpine

# Set working directory
WORKDIR /opt/egov

# Copy the built jar file and start script from the build stage
COPY --from=build /app/target/*.jar /app/start.sh /opt/auro/

# Make start script executable
RUN chmod +x /opt/auro/start.sh

# Set environment variables
#ENV ENVIRONMENT=development
ENV JAVA_OPTS="-Xms256m -Xmx512m -Dspring.profiles.active=local"

# Run the application
CMD ["/opt/auro/start.sh"]
