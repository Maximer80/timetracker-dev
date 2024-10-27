# Timetracker

## Описание

Timetracker — это приложение для учета рабочего времени сотрудников с использованием чат-бота Telegram.

## Структура проекта

.
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── timetracker
│   │   │               ├── TimetrackerApplication.java
│   │   │               ├── auth
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   ├── User.java
│   │   │               │   ├── UserRepository.java
│   │   │               │   └── UserService.java
│   │   │               ├── notifications
│   │   │               ├── reporting
│   │   │               └── time_tracking
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── timetracker
│                       ├── TimetrackerApplicationTests.java
│                       └── auth
│                           └── AuthControllerTest.java
└── target
    └── ...

## Технологии и инструменты

- Java
- Spring Boot
- PostgreSQL
- Maven
- JUnit

## Установка и запуск

1. Убедитесь, что установлены JDK_17 и Maven_3.9.9
2. Клонируйте репозиторий:
   git clone git@github.com:Maximer80/timetracker-dev.git
   cd timetracker
4. Запустите приложение:
   mvn clean install
   mvn spring-boot:run

## API

**Эндпоинты**

./auth/register: Регистрация пользователя (POST).
./auth/login: Аутентификация пользователя (POST).

**Тестирование**

- Написаны тесты для модуля авторизации, успешно прошедшие через JUnit и Postman.
- Запустите тесты:
  mvn test

**Планы на будущее**

- Реализация модуля учёта рабочего времени.
- Интеграция с Telegram Bot API.

**Лицензия**

- Указать тип лицензии (например, MIT, Apache).
