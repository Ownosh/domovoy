# Domovoy API

REST API для системы управления домом на Java Spring Boot.

## Требования

- Java 17 или выше
- Maven 3.6+
- MySQL 8.0+

## Установка и настройка

1. Клонируйте репозиторий или откройте проект

2. Настройте базу данных в файле `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/domovoy?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Создайте базу данных MySQL с именем `domovoy` и выполните SQL скрипт создания таблиц (предоставлен в запросе)

4. Запустите приложение:
```bash
mvn spring-boot:run
```

Или скомпилируйте и запустите jar:
```bash
mvn clean package
java -jar target/domovoy-api-1.0.0.jar
```

API будет доступно по адресу: `http://localhost:8080/api`

## Структура API

### Основные endpoints:

#### Users (Пользователи)
- `GET /api/users` - Получить всех пользователей
- `GET /api/users/{id}` - Получить пользователя по ID
- `GET /api/users/email/{email}` - Получить пользователя по email
- `POST /api/users` - Создать пользователя
- `PUT /api/users/{id}` - Обновить пользователя
- `DELETE /api/users/{id}` - Удалить пользователя

#### Residential Complexes (Жилые комплексы)
- `GET /api/complexes` - Получить все комплексы
- `GET /api/complexes/{id}` - Получить комплекс по ID
- `POST /api/complexes` - Создать комплекс
- `PUT /api/complexes/{id}` - Обновить комплекс
- `DELETE /api/complexes/{id}` - Удалить комплекс

#### Buildings (Здания)
- `GET /api/buildings` - Получить все здания
- `GET /api/buildings/{id}` - Получить здание по ID
- `GET /api/buildings/complex/{complexId}` - Получить здания по комплексу
- `POST /api/buildings` - Создать здание
- `PUT /api/buildings/{id}` - Обновить здание
- `DELETE /api/buildings/{id}` - Удалить здание

#### News (Новости)
- `GET /api/news` - Получить все новости
- `GET /api/news/published` - Получить опубликованные новости
- `GET /api/news/{id}` - Получить новость по ID
- `POST /api/news` - Создать новость
- `PUT /api/news/{id}` - Обновить новость
- `DELETE /api/news/{id}` - Удалить новость

#### Requests (Заявки)
- `GET /api/requests` - Получить все заявки
- `GET /api/requests/user/{userId}` - Получить заявки пользователя
- `GET /api/requests/status/{status}` - Получить заявки по статусу
- `GET /api/requests/{id}` - Получить заявку по ID
- `POST /api/requests` - Создать заявку
- `PUT /api/requests/{id}` - Обновить заявку
- `DELETE /api/requests/{id}` - Удалить заявку

#### Notifications (Уведомления)
- `GET /api/notifications` - Получить все уведомления
- `GET /api/notifications/{id}` - Получить уведомление по ID
- `GET /api/notifications/user/{userId}` - Получить уведомления пользователя
- `POST /api/notifications` - Создать уведомление
- `PUT /api/notifications/{id}` - Обновить уведомление
- `PUT /api/notifications/user-notifications/{id}/read` - Отметить как прочитанное
- `DELETE /api/notifications/{id}` - Удалить уведомление

#### Area Categories (Категории объектов района)
- `GET /api/area-categories` - Получить все категории
- `GET /api/area-categories/{id}` - Получить категорию по ID
- `POST /api/area-categories` - Создать категорию
- `PUT /api/area-categories/{id}` - Обновить категорию
- `DELETE /api/area-categories/{id}` - Удалить категорию

#### Area Objects (Объекты района)
- `GET /api/area-objects` - Получить все объекты
- `GET /api/area-objects/category/{categoryId}` - Получить объекты по категории
- `GET /api/area-objects/{id}` - Получить объект по ID
- `POST /api/area-objects` - Создать объект
- `PUT /api/area-objects/{id}` - Обновить объект
- `DELETE /api/area-objects/{id}` - Удалить объект

#### User Verifications (Верификация пользователей)
- `GET /api/user-verifications` - Получить все верификации
- `GET /api/user-verifications/user/{userId}` - Получить верификации пользователя
- `GET /api/user-verifications/status/{status}` - Получить верификации по статусу
- `GET /api/user-verifications/{id}` - Получить верификацию по ID
- `POST /api/user-verifications` - Создать верификацию
- `PUT /api/user-verifications/{id}` - Обновить верификацию
- `DELETE /api/user-verifications/{id}` - Удалить верификацию

## Примеры использования

### Создание пользователя
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "passwordHash": "hashed_password",
    "fullName": "Иван Иванов",
    "phone": "+79001234567",
    "role": "resident"
  }'
```

### Получение всех новостей
```bash
curl http://localhost:8080/api/news
```

### Создание заявки
```bash
curl -X POST http://localhost:8080/api/requests \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "category": "repair",
    "subject": "Протечка крана",
    "description": "Требуется ремонт крана на кухне",
    "priority": "high"
  }'
```

## Структура проекта

```
src/main/java/ru/domovoy/
├── DomovoyApplication.java      # Главный класс приложения
├── controller/                   # REST контроллеры
├── service/                      # Сервисы (бизнес-логика)
├── repository/                   # Репозитории (доступ к БД)
├── model/                        # Entity классы
├── converter/                    # Конвертеры для enum
└── exception/                    # Обработчики исключений
```

## Примечания

- API использует CORS с разрешением для всех источников (`@CrossOrigin(origins = "*")`)
- Все endpoints возвращают JSON
- Для production рекомендуется настроить аутентификацию и авторизацию (Spring Security)
- Рекомендуется использовать DTO классы вместо прямого использования Entity классов в контроллерах

