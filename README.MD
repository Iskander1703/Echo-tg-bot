# Delayed Echo Telegram Bot
## Описание перменных:
- skipTests: Параметр указывает на пропуск запуска тестов при сборке проекта. Это может быть полезно в среде разработки или для быстрого развертывания.

- spring.datasource.driverClassName: Имя JDBC-драйвера для подключения к базе данных. Например, org.postgresql.Driver для PostgreSQL.
- spring.datasource.url: URL-адрес подключения к базе данных. Например, jdbc:postgresql://localhost:5432/mydatabase для подключения к базе данных PostgreSQL с именем "mydatabase".
- spring.datasource.username: Имя пользователя базы данных для аутентификации.
- spring.datasource.password: Пароль пользователя базы данных для аутентификации.
- spring.jpa.properties.hibernate.dialect: Диалект Hibernate для работы с базой данных. Например, org.hibernate.dialect.PostgreSQLDialect для PostgreSQL.
- spring.jpa.properties.hibernate.show_sql: Указывает, нужно ли выводить SQL-запросы в журнал. Значение true позволяет просматривать SQL-запросы, генерируемые Hibernate.
- flyway.user: Имя пользователя для подключения к базе данных Flyway.
- flyway.password: Пароль пользователя для подключения к базе данных Flyway.
- flyway.schemas: Схема базы данных, используемая Flyway.
- flyway.url: URL-адрес подключения к базе данных Flyway.
- bot.name: Имя телеграм-бота.
- thread.count - Кол-во потоко, которые обрабатывают сообщения
- echo.delay - Начальная задержка перед отправкой сообщения
- 
## Инструкиция по запуску
### 1. Запуск приложения с использованием готового JAR-файла из папки target/ с помощью Docker Compose, включающего два контейнера: контейнер для Spring Boot и контейнер для PostgreSQL.
#### 1. Переопределите переменные в docker-compose.yml
```
      - BOT_NAME=
      - BOT_TOKEN=
```
#### 2. Запустите команду:
```
docker-compose up
```

### 2. Запуск с использованием maven без jar файла. Весь процесс сборки проекта, включая загрузку зависимостей и упаковку JAR-файла, будет выполнен внутри контейнера.


bot.token: Токен доступа для телеграм-бота.
#### 1. Закомментируйте строчки в DockerFile:
```
#FROM openjdk:17-oracle
#EXPOSE 8080
#RUN mkdir /app
#COPY target/echo-bot-tg-1.0.0-SNAPSHOT.jar /app/echo-bot-tg-1.0.0-SNAPSHOT.jar
#WORKDIR /app
#CMD ["java", "-jar", "echo-bot-tg-1.0.0-SNAPSHOT.jar"]
```
#### 2. Раскомментируйте строчки в DockerFile:
```
FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y maven
EXPOSE 8080
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package \
    -DskipTests \
    -Dspring.datasource.driverClassName= \
    -Dspring.datasource.url= \
    -Dspring.datasource.username= \
    -Dspring.datasource.password= \
    -Dspring.jpa.properties.hibernate.dialect= \
    -Dspring.jpa.properties.hibernate.show_sql= \
    -Dflyway.user= \
    -Dflyway.password= \
    -Dflyway.schemas= \
    -Dflyway.url= \
    -Dbot.name= \
    -Dbot.token= \
CMD ["java", "-jar", "target/echo-bot-tg-1.0.0-SNAPSHOT.jar"]
```
#### 3. Переопределите переменные в DockerFile
```
    -DskipTests 
    -Dspring.datasource.driverClassName= \
    -Dspring.datasource.url= \
    -Dspring.datasource.username= \
    -Dspring.datasource.password= \
    -Dspring.jpa.properties.hibernate.dialect= \
    -Dspring.jpa.properties.hibernate.show_sql= \
    -Dflyway.user= \
    -Dflyway.password= \
    -Dflyway.schemas= \
    -Dflyway.url= \
    -Dbot.name= \ -
    -Dbot.token= \ -
```
#### 3.1. По желанию можно переопределить переменные
- thread.count
-  echo.delay
#### 4. Выполните команду из корневой директории проекта. В этом случае все зависимости и сборка jar будет происходить непосредственно в контейнере
```
docker-compose up
```
## Api документация swagger доступна по адресам
```
/swagger-documentation.html
/swagger-ui/index.html
```
## P.S. Если совсем не получается запустить, то вспользуйтесь командой, подставив свои значения
```
clean spring-boot:run -Dspring.datasource.url=
-Dspring.datasource.username=
-Dspring.datasource.password=
-Dspring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
-Dspring.jpa.properties.hibernate.show_sql=true
-Dflyway.user=
-Dflyway.password=
-Dflyway.schemas=public
-Dflyway.url=
-Dflyway.locations=classpath:db/migration
-Dbot.name=
-Dbot.token=
```