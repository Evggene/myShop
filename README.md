# 🛍️ Витрина магазина (Reactive Spring Boot)

Веб-приложение витрины магазина с реактивным стеком Spring Boot 3, включающее:
- Просмотр товаров
- Формирование корзины
- Оформление заказов
- Интеграцию с платежной системой

## 🚀 Технический стек

### 📦 Основные технологии
| Категория       | Технологии                          |
|-----------------|-------------------------------------|
| **Backend**     | Spring Boot 3 (WebFlux), R2DBC      |
| **Базы данных** | PostgreSQL (реактивный драйвер), Redis |
| **Frontend**    | Thymeleaf 3 (реактивные шаблоны)    |
| **API**         | OpenAPI 3.0 + генерация кода        |
| **Инфраструктура** | Docker             |

## 🏗️ Модульная структура
```
my-shop/
├── showcase/ # Основное веб-приложение
│ ├── Dockerfile
│ └── src/
├── payment/ # Платежный микросервис
│ ├── Dockerfile
│ └── src/
```

## 🛠️ Инструкции по запуску

### Локальный запуск
```bash
# Сборка всех модулей
mvn clean package

# Запуск основного приложения  
java -jar shop-web/target/*.jar

# Запуск платежного сервиса
java -jar payment-service/target/*.jar

# Поднять контейнеры баз данных
docker run --name my-postgres \
  -e POSTGRES_USER=app_user \
  -e POSTGRES_PASSWORD=app_password \
  -e POSTGRES_DB=app_db \
  -p 5433:5432 \
  -d postgres
  
  docker run --name my-redis \
  -d redis
```

### Доступные endpoints:
```bash
Web UI: http://localhost:8080
Payment API: http://localhost:8090 (без интерфейса)
Swagger UI: http://localhost:8090/swagger-ui.html (payment)
```
