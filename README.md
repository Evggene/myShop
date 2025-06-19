# 🛍️ Витрина магазина (Reactive Spring Boot)

Веб-приложение витрины магазина с возможностью формирования корзины и оформления заказов, построенное на реактивном стеке Spring Boot 3.

## 🚀 Технический стек

### 📦 Основные технологии
| Категория       | Технологии                          |
|-----------------|-------------------------------------|
| **Backend**     | Spring Boot 3 (WebFlux), R2DBC, Netty |
| **База данных** | PostgreSQL с реактивным драйвером   |
| **Frontend**    | Thymeleaf 3 (реактивные шаблоны)    |
| **Инфраструктура** | Docker, Docker Compose             |

### ⚙️ Системные требования
- **Java**: 21+
- **Сборка**: Maven 3.9+
- **Контейнеризация**:
    - Docker 20+
    - Docker Compose 2.20+

## 🛠️ Инструкция по запуску

### 1. Локальная сборка
```bash
mvn clean package
java -jar target/my-shop-*.jar
```
### 2. Запуск в Docker
```bash
# Сборка и запуск
docker build -t my-shop .
docker run -p 8080:8080 my-shop
```
### 3. Полный стек (с PostgreSQL)
```bash
docker-compose up -d
Приложение будет доступно по адресу: http://localhost:8080
```
