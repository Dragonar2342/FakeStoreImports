# Fake Store API Integration

Spring Boot приложение для работы с товарами из Fake Store API с возможностью локального управления данными.

## Технологии

- Java 21
- Spring Boot 3.x
- PostgreSQL 17
- Spring Data JPA
- Spring Web

## Функционал
Основные возможности
1. Импорт данных:
   - Автоматический импорт товаров из Fake Store API.
   - Ручной запуск импорта через API.
   - Обновление существующих товаров.

2. Управление товарами:
   - CRUD операции с товарами
   - Фильтрация по цене
   - Сортировка по цене и категории
   - Пагинация результатов

3. Работа с категориями:
   - Получение списка всех категорий
   - Получение товаров по категории

4. Дополнительные возможности
   - Автоматическая синхронизация данных (каждые 30 минут)
   - Unit-тесты
   - Разделение прав доступа (USER/ADMIN)
   - Полная документация API через Swagger UI

## REST API Endpoints

### Products

| Метод  | Endpoint                              | Описание                             | Требуемая роль |
|--------|---------------------------------------|--------------------------------------|----------------|
| GET    | /api/products/import                  | Импорт товаров из внешнего API       | USER           |
| GET    | /api/products                         | Получить все продукты (с пагинацией) | USER           |
| GET    | /api/products/category/{categoryName} | Получить продукты по категории       | USER           |
| GET    | /api/products/filter                  | Фильтрация по цене                   | USER           |
| GET    | /api/products/sorted                  | Сортировка продуктов                 | USER           |
| GET    | /api/products/{id}                    | Получить продукт по ID               | USER           |
| POST   | /api/products                         | Создать новый продукт                | ADMIN          |
| PUT    | /api/products/{id}                    | Обновить продукт                     | ADMIN          |
| DELETE | /api/products/{id}                    | Удалить продукт                      | ADMIN          |

### Categories

| Метод  | Endpoint                              | Описание                             | Требуемая роль |
|--------|---------------------------------------|--------------------------------------|----------------|
| GET	   | /api/products/categories	             | Получить все категории	             | USER           |

## Web Interface Endpoints

| Метод  | Endpoint                              | Описание                             | Требуемая роль |
|--------|---------------------------------------|--------------------------------------|----------------|
| GET    | `/`                                   | Перенаправление на страницу товаров  | PUBLIC         |
| GET    | `/login`                              | Страница авторизации                 | PUBLIC         |
| GET    | `/register`                           | Страница регистрации                 | PUBLIC         |
| POST   | `/register`                           | Обработка регистрации                | PUBLIC         |
| GET    | `/products`                           | Список всех товаров (HTML)           | USER           |
| GET    | `/products/{id}`                      | Страница товара (HTML)               | USER           |
| GET    | `/products/import`                    | Страница импорта товаров             | USER           |
| POST   | `/products/import`                    | Запуск импорта товаров               | USER           |
| GET    | `/products/new`                       | Форма создания товара                | ADMIN          |
| GET    | `/products/{id}/edit`                 | Форма редактирования товара          | ADMIN          |
| POST   | `/products/{id}/delete`               | Удаление товара                      | ADMIN          |
| GET    | `/categories`                         | Список категорий (HTML)              | USER           |
| GET    | `/categories/{name}`                  | Товары по категории (HTML)           | USER           |

Документация API доступна после запуска приложения:

Swagger UI: http://localhost:8080/swagger-ui.html

OpenAPI спецификация: http://localhost:8080/v3/api-docs

## Запуск приложения

1. Убедитесь, что установлены:
   - Java 21
   - PostgreSQL 17 (или другая поддерживаемая СУБД)
   - Maven

2. Настройте подключение к БД в `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
