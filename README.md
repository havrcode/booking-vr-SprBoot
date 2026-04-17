# BookingVR (Spring Boot)

## Поточний стан проєкту

Станом на зараз репозиторій містить базовий каркас застосунку:
- Spring Boot 4.0.5;
- Maven проєкт із Java 17;
- мінімальну точку входу та smoke-test контексту;
- підключені стартери для Web MVC, Thymeleaf, Validation, JPA та PostgreSQL.

Іншими словами: це добрий clean-start, але бізнес-функціональність бронювання ще не реалізована.

## Рекомендований напрямок розробки

Нижче — практичний план, як рухатися далі від «каркаса» до працюючого MVP.

### Етап 1. Домен і модель даних (MVP foundation)

1. Визначити ключові сутності:
   - `VrClub` / `Location`
   - `Room` або `Station`
   - `TimeSlot`
   - `Booking`
   - `User`
2. Зафіксувати базові правила бронювання:
   - заборона перетину слотів;
   - статуси бронювання (`CREATED`, `CONFIRMED`, `CANCELLED`, `EXPIRED`);
   - обмеження по часовому вікну та тривалості.
3. Додати Liquibase/Flyway для керованих міграцій БД.

**Definition of Done:** є ER-модель, міграції, JPA-сутності та репозиторії.

### Етап 2. Сервісний шар

1. Реалізувати `BookingService`:
   - перевірка доступності слота;
   - створення бронювання в транзакції;
   - скасування бронювання;
   - базова ідемпотентність для повторних запитів.
2. Винести бізнес-правила в окремі валідатори/політики.
3. Покрити критичні сценарії unit- та інтеграційними тестами.

**Definition of Done:** сервіси обробляють ключові кейси, тести покривають ризикові сценарії.

### Етап 3. API / UI

1. Зробити REST API для клієнтських застосунків:
   - `GET /api/slots?date=...`
   - `POST /api/bookings`
   - `DELETE /api/bookings/{id}`
2. Паралельно або після REST — мінімальний Thymeleaf UI:
   - календар слотів;
   - форма створення бронювання;
   - сторінка «мої бронювання».

**Definition of Done:** користувач може знайти слот і створити/скасувати бронювання end-to-end.

### Етап 4. Безпека та ролі

1. Додати Spring Security:
   - роль `CLIENT` (створення своїх бронювань);
   - роль `ADMIN` (керування слотами/бронюваннями).
2. Обмежити доступ до приватних ресурсів.
3. Додати audit поля (`createdBy`, `updatedBy`, `createdAt`, `updatedAt`).

### Етап 5. Надійність та експлуатація

1. Конфігурації профілів (`dev`, `test`, `prod`).
2. Логування + кореляційний id запиту.
3. Actuator + health checks.
4. Docker Compose (app + postgres).
5. CI pipeline (build, test, static checks).

## Технічний backlog (найближчі задачі)

### P0 (наступний спринт)
- [ ] Додати Flyway або Liquibase.
- [ ] Описати доменну модель (Booking/Slot/User).
- [ ] Зробити першу міграцію для таблиць.
- [ ] Реалізувати `BookingService#createBooking` з перевіркою конфлікту слотів.
- [ ] Написати інтеграційний тест сценарію «успішне бронювання».
- [ ] Написати інтеграційний тест сценарію «конфлікт часу».

### P1
- [ ] REST контролери для слотів/бронювань.
- [ ] Глобальний exception handling (`@ControllerAdvice`).
- [ ] DTO + мапери (MapStruct або manual).

### P2
- [ ] Базова авторизація користувачів.
- [ ] Адмін-панель керування слотами.
- [ ] Нотифікації (email/telegram) про підтвердження бронювання.

## Архітектурні орієнтири

Пропонована структура пакетів:

```text
ua.com.havrcode.bookingvr
  ├─ booking
  │   ├─ api
  │   ├─ service
  │   ├─ domain
  │   ├─ repo
  │   └─ mapper
  ├─ slot
  ├─ user
  └─ common
      ├─ config
      ├─ exception
      └─ util
```

Принципи:
- контролери не містять бізнес-логіку;
- правила бронювання централізовані в сервісах/політиках;
- транзакційні межі в сервісному шарі;
- DTO відокремлені від JPA-сутностей.

## Що робити прямо зараз (практичний старт)

1. Обрати інструмент міграцій (рекомендовано **Flyway** для старту).
2. Створити перші 3 сутності: `User`, `VrStation`, `Booking`.
3. Реалізувати endpoint `POST /api/bookings`.
4. Додати перевірку перетину часових інтервалів на рівні сервісу.
5. Покрити двома інтеграційними тестами (success/conflict).

---









Яку архітектуру закладаємо одразу

Для твого Booking VR я хочу, щоб ти відразу привчився до нормального поділу.

Ось правильний мінімальний каркас:

booking-vr/
├─ pom.xml
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ com/bookingvr/
│  │  │     ├─ BookingVrApplication.java
│  │  │     ├─ controller/
│  │  │     ├─ service/
│  │  │     ├─ repository/
│  │  │     ├─ entity/
│  │  │     ├─ dto/
│  │  │     ├─ config/
│  │  │     └─ exception/
│  │  └─ resources/
│  │     ├─ templates/
│  │     ├─ static/
│  │     │  ├─ css/
│  │     │  ├─ js/
│  │     │  └─ images/
│  │     └─ application.properties
│  └─ test/

Spring Boot рекомендує тримати головний клас у кореневому пакеті вище за інші класи, щоб component scan і пошук @Entity ішли по твоєму проєкту правильно. За замовчуванням Spring Boot також віддає статичні ресурси з /static, а для Thymeleaf стандартно використовується classpath:/templates/.

За що відповідає кожна папка
controller/

Тут лежать класи, які приймають HTTP-запити.

Наприклад:

PublicBookingController
AdminBookingController

Їхня задача:

прийняти запит
викликати сервіс
повернути HTML-сторінку або редірект

Controller не повинен рахувати бізнес-логіку.

service/

Це серце проєкту.

Тут буде:

логіка календаря
логіка доступності слотів
логіка створення бронювання
логіка ціни

Наприклад:

CalendarService
BookingService
PricingService
AdminService
repository/

Тут інтерфейси для доступу до БД.

Наприклад:

DayConfigRepository
TimeSlotRepository
BookingRepository
PricingSettingsRepository

Spring Data JPA якраз дає цей стиль роботи через repository-інтерфейси.

entity/

Тут сутності, які зберігаються в БД.

Для першої версії я бачу такі:

DayConfig
TimeSlot
Booking
PricingSettings
BookingStatus
PricingMode
dto/

Тут об’єкти для передачі даних між шарами або для форм.

Наприклад:

BookingRequestDto
DayViewDto
SlotViewDto
PricingPreviewDto

DTO — це не база даних, а форма зручної передачі даних.

config/

Тут конфігурація застосунку, якщо вона знадобиться:

форматер дати
базова конфігурація MVC
початкові тестові дані
exception/

Тут свої винятки:

SlotFullyBookedException
DayClosedException
InvalidBookingRequestException

Поки можна навіть не чіпати, але папку закласти нормально.

templates/

HTML-шаблони для Thymeleaf.

Наприклад:

booking-calendar.html
booking-day.html
booking-form.html
booking-success.html
admin-calendar.html
admin-pricing.html

Thymeleaf — server-side шаблонізатор, який добре підходить саме для такого класичного HTML UI.

static/

Тут:

CSS
JS
картинки

Spring Boot за замовчуванням обслуговує статичний контент із /static.