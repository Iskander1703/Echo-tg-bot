#C Jar
FROM openjdk:17-oracle
EXPOSE 8080
RUN mkdir /app
COPY target/echo-bot-tg-1.0.0-SNAPSHOT.jar /app/echo-bot-tg-1.0.0-SNAPSHOT.jar
WORKDIR /app
CMD ["java", "-jar", "echo-bot-tg-1.0.0-SNAPSHOT.jar"]

#Без Jar
#FROM openjdk:17-jdk-slim
#RUN apt-get update && apt-get install -y maven
#EXPOSE 8080
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package \
#    -DskipTests \
#    -Dspring.datasource.driverClassName= \
#    -Dspring.datasource.url= \
#    -Dspring.datasource.username= \
#    -Dspring.datasource.password= \
#    -Dspring.jpa.properties.hibernate.dialect= \
#    -Dspring.jpa.properties.hibernate.show_sql= \
#    -Dflyway.user= \
#    -Dflyway.password= \
#    -Dflyway.schemas= \
#    -Dflyway.url= \
#    -Dbot.name= \
#    -Dbot.token= \
#CMD ["java", "-jar", "target/echo-bot-tg-1.0.0-SNAPSHOT.jar"]