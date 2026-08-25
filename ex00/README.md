# ex00 - Spring Security Cinema Application

This is the **Exercise 00** implementation for the "Spring Boot — More, Than Spring" subject (42 School).

## Overview

A cinema management web application built with **Spring Boot 4.1.0** demonstrating:
- Role-based authentication & authorization (ADMIN / USER)
- Custom login, registration, and logout pages
- Remember-me functionality
- CSRF protection
- AJAX-based session search
- Real-time WebSocket chat per film (STOMP over SockJS)
- User profile with avatar upload and login history
- Admin panels for halls, films, and sessions management

## Tech Stack

| Component | Version |
|-----------|---------|
| Spring Boot | 4.1.0 |
| Java | 21 (LTS) |
| Database | PostgreSQL |
| ORM | Spring Data JPA (Hibernate) |
| Security | Spring Security 6 |
| Templates | FreeMarker (.ftl) |
| WebSocket | STOMP over SockJS |
| Build | Maven (wrapper) |
| Packaging | WAR |

## Project Structure

```
ex00/Cinema/
├── src/
│   ├── main/
│   │   ├── java/fr/_42/cinema/
│   │   │   ├── config/           # SecurityConfig, WebSocketConfig
│   │   │   ├── controllers/      # All MVC controllers
│   │   │   ├── dto/              # Data transfer objects
│   │   │   ├── models/           # JPA entities
│   │   │   ├── repositories/     # Spring Data repositories
│   │   │   ├── services/         # Business logic
│   │   │   └── CinemaApplication.java
│   │   ├── resources/
│   │   │   ├── static/
│   │   │   │   ├── css/style.css
│   │   │   │   └── js/chat.js
│   │   │   ├── templates/
│   │   │   │   ├── SignIn.ftl
│   │   │   │   ├── SignUp.ftl
│   │   │   │   ├── profile.ftl
│   │   │   │   ├── admin/        # halls, films, sessions
│   │   │   │   ├── films/chat.ftl
│   │   │   │   └── session/      # search, session-detail
│   │   │   ├── sql/
│   │   │   │   ├── schema.sql
│   │   │   │   └── data.sql
│   │   │   └── application.properties
│   └── test/
└── pom.xml
```

## Features

### Authentication & Authorization
- **Custom `/signIn`** page with email/password + remember-me checkbox
- **Custom `/signUp`** page (firstName, lastName, phone, email, password)
- **Role-based redirects**: ADMIN → `/admin/panel/halls`, USER → `/profile`
- **Remember-me** token (30 days, secure cookie)
- **CSRF** protection with `CookieCsrfTokenRepository` (excluded for `/ws/**` for SockJS)
- **Logout** via POST `/signOut` → redirects to `/signIn`

### Admin Panel (ADMIN only)
- `/admin/panel/halls` — CRUD for cinema halls
- `/admin/panel/films` — CRUD for films + poster upload
- `/admin/panel/sessions` — CRUD for sessions (film, hall, datetime, price)

### Public Pages (Authenticated users)
- `/session/search` — Live AJAX search by film title
- `/sessions/{id}` — Session details with **Join Film Chat** button
- `/films/{id}/chat` — Real-time chat per film (STOMP WebSocket)
- `/profile` — User info, login history, avatar upload, uploaded files list

### Chat (WebSocket)
- Endpoint: `/ws` (SockJS)
- Broker: `/films` (topic per film)
- App prefix: `/app`
- Send: `/app/films/{filmId}/chat/send` → `{ content }`
- Receive: `/films/{filmId}/chat/messages` → `{ userId, firstName, lastName, content, sentAt }`
- Identity derived from authenticated `Principal` (never client-sent)

### File Upload
- POST `/images` with multipart `avatar` field
- Context: `AVATAR` (unified pool per user)
- Unique stored filenames (UUID)
- Files served at `/images/{storedName}`
- Return-to redirect supported via `returnTo` parameter

## Database Setup

The application uses **manual SQL initialization** (no `spring.sql.init`):

```bash
# Create database and user
psql -U postgres -c "CREATE DATABASE new_cinema;"
psql -U postgres -c "CREATE USER cinema WITH PASSWORD 'cinema';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE new_cinema TO cinema;"

# Run schema then data
psql -U cinema -d new_cinema -f src/main/resources/sql/schema.sql
psql -U cinema -d new_cinema -f src/main/resources/sql/data.sql
```

### Schema Highlights
- `users` — email, password (BCrypt), firstName, lastName, phone, role, status
- `halls`, `films`, `sessions` — cinema domain
- `chat_message` — film_id, user_id, content, sent_at
- `authentication_log` — user_id, ip_address, authenticated_at
- `uploaded_file` — user_id, original_name, stored_name, mime_type, size, context (AVATAR/POSTER)

### Seed Data (data.sql)
| Email | Password | Role |
|-------|----------|------|
| admin@cinema.com | admin123 | ADMIN |
| alice@example.com | password | USER |

Plus 3 halls, 3 films, 4 sessions, 2 chat messages.

## Building & Running

### Prerequisites
- Java 21
- Maven wrapper (`./mvnw`)
- PostgreSQL running on localhost:5432
- Tomcat 10+ (for WAR deployment) or run via `spring-boot:run`

### Build WAR
```bash
cd ex00/Cinema
./mvnw clean package -DskipTests
# Output: target/cinema.war
```

### Deploy to Tomcat
```bash
cp target/cinema.war /opt/tomcat/webapps/
/opt/tomcat/bin/startup.sh
```

### Or Run via Maven (embedded Tomcat)
```bash
./mvnw spring-boot:run
```

### Access
```
http://localhost:8080/cinema/
```

## Key URLs

| Path | Access | Description |
|------|--------|-------------|
| `/signIn` | Public | Login page |
| `/signUp` | Public | Registration page |
| `/signOut` | Authenticated | Logout (POST) |
| `/profile` | USER, ADMIN | User profile |
| `/session/search` | Authenticated | Session search (AJAX) |
| `/sessions/{id}` | Authenticated | Session details + chat link |
| `/films/{id}/chat` | Authenticated | Film chat room |
| `/admin/panel/halls` | ADMIN | Halls management |
| `/admin/panel/films` | ADMIN | Films management |
| `/admin/panel/sessions` | ADMIN | Sessions management |
| `/images` | Authenticated | Avatar upload (POST) |
| `/images/{storedName}` | Authenticated | Serve uploaded file |
| `/ws` | Authenticated | SockJS WebSocket endpoint |

## Configuration

`src/main/resources/application.properties`:
```properties
server.port=8080
server.servlet.context-path=/cinema

spring.datasource.url=jdbc:postgresql://localhost:5432/new_cinema
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

spring.freemarker.suffix=.ftl
spring.freemarker.template-loader-path=classpath:/templates/
```

## Security Rules

| Resource | ADMIN | USER | Anonymous |
|----------|-------|------|-----------|
| `/admin/**` | ✅ | ❌ | ❌ |
| `/profile` | ✅ | ✅ | ❌ |
| `/session/**` | ✅ | ✅ | ❌ |
| `/films/**` | ✅ | ✅ | ❌ |
| `/images` | ✅ | ✅ | ❌ |
| `/signIn`, `/signUp` | ❌* | ❌* | ✅ |
| `/ws/**` | ✅ | ✅ | ❌ |

* Authenticated users redirected to `/profile` (USER) or `/admin/panel/halls` (ADMIN)

---

*Built as part of the 42 curriculum. Spring Boot 4.1.0, Java 21, PostgreSQL.*
