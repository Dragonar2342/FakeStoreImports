# Fake Store API Integration

Spring Boot приложение для работы с товарами из Fake Store API с возможностью локального управления данными.

## Технологии

- Java 21
- Spring Boot 3.x
- PostgreSQL 17 (или MySQL/MariaDB)
- Spring Data JPA
- Spring Web

## Функционал

### Основные возможности

1. Импорт товаров из Fake Store API с сохранением в локальную БД
2. CRUD операции с товарами
3. Фильтрация товаров по цене
4. Получение списка уникальных категорий
5. Пагинация для списков товаров

### Дополнительные возможности

1. Получение товаров по категории
2. Повторный импорт с обновлением существующих товаров
3. Сортировка по цене и категории
4. Unit-тесты

## API Endpoints

### Products

1. **Импорт продуктов из внешнего API**
   - `GET /api/products/import`

2. **Получить все продукты (с пагинацией)**
   - `GET /api/products?page={page}&size={size}`
   - Параметры:
     - `page` - номер страницы (начинается с 0)
     - `size` - количество элементов на странице

3. **Получить продукты по категории**
   - `GET /api/products/category/{categoryName}?page={page}&size={size}`

4. **Фильтрация по цене**
   - `GET /api/products/filter?minPrice={minPrice}&maxPrice={maxPrice}&page={page}&size={size}`
   - Параметры:
     - `minPrice` - минимальная цена (опционально)
     - `maxPrice` - максимальная цена (опционально)

5. **Сортировка продуктов**
   - `GET /api/products/sorted?priceDirection={asc/desc}&categoryDirection={asc/desc}&page={page}&size={size}`
   - Параметры:
     - `priceDirection` - направление сортировки по цене (asc или desc)
     - `categoryDirection` - направление сортировки по категории (asc или desc)

6. **Получить продукт по ID**
   - `GET /api/products/{id}`

7. **Создать новый продукт**
   - `POST /api/products`
   - Тело запроса:
     ```json
     {
         "title": "New Product",
         "price": 19.99,
         "description": "Product description",
         "categoryName": "electronics",
         "image": "image.jpg",
         "rating": {
             "rate": 4.5,
             "count": 100
         }
     }
     ```

8. **Обновить продукт**
   - `PUT /api/products/{id}`
   - Тело запроса:
     ```json
     {
         "id": 21,
         "title": "Updated Product",
         "price": 29.99,
         "description": "Updated description",
         "category": {
             "id": 1,
             "name": "electronics"
         },
         "image": "updated.jpg",
         "rating": {
             "rate": 4.7,
             "count": 120
         }
     }
     ```

9. **Удалить продукт**
   - `DELETE /api/products/{id}`

### Categories

10. **Получить все категории**
    - `GET /api/products/categories`

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
