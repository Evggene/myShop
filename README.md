# Витрина магазина на Spring Boot

Веб-приложение вирины магазина с возможностью формирования корзины и заказов, построенное на Spring Boot 3 с использованием Hibernate/JPA и Docker.

## 📋 Технический стек

### Основные технологии
- **Backend**:
    - Spring Boot 3
    - Spring Data JPA (Hibernate)
    - Встроенный Tomcat 10
- **База данных**: PostgreSQL (в Docker)
- **Шаблонизация**: Thymeleaf 3
- **Сборка**: Maven
- **Контейнеризация**: Docker + Docker Compose

### Системные требования
- Java 21
- Maven 3.9+
- Docker 20+
- Docker Compose 2.20+

## 🛠️ Сборка и запуск

### Локальный запуск (без Docker)
## Сборка проекта
mvn clean package

## Запуск приложения
java -jar target/my-shop-0.0.1-SNAPSHOT.jar

### Запуск через Docker
## Сборка образа
docker build -t my-shop .

## Запуск контейнера
docker run -p 8080:8080 my-shop
Запуск с PostgreSQL через Docker Compose


## Запуск всего стека (приложение + БД)
docker-compose up -d

## Остановка
docker-compose down
Доступ к приложению
После запуска приложение будет доступно по адресу:
http://localhost:8080
