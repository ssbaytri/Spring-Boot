# 🎬 Cinema — Spring Boot Edition

> *"More, Than Spring"* — a movie-theater booking application rebuilt on
> **Spring Boot**, merging two prior, deliberately incompatible prototypes
> ([FWA](../FWA), raw Servlets + hand-rolled auth, and Cinema, Spring
> MVC + Hibernate with no auth at all) into one coherent, production-shaped
> backend: real authentication, role-based access, i18n, Bean Validation,
> and email-confirmed accounts.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21%20LTS-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6-6DB33F?logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/DB-PostgreSQL-336791?logo=postgresql)](https://www.postgresql.org/)
[![FreeMarker](https://img.shields.io/badge/Templates-FreeMarker-B4232C)](https://freemarker.apache.org/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/license-Educational-lightgrey)]()

---

## 📖 About

This repository is the **42 School "Spring Boot — More, Than Spring"**
subject, built as a progressive, three-exercise reimplementation of the
same cinema-booking domain covered by two earlier projects:

| Prior project | Stack | Had |
|---|---|---|
| **FWA** | Raw Servlets, JSP, `JdbcTemplate` | Real authenticated users, BCrypt, `/profile` |
| **Cinema** | Spring MVC + Hibernate | Catalog, live search, WebSocket chat — but **no login at all** (anonymous cookie identity) |

Neither project's identity model was compatible with the other on its
own — FWA had real users but no chat/catalog, Cinema had a rich feature
set but nobody was ever actually "logged in." This repo's first real
engineering problem, before writing a single controller, was **designing
one unified schema and identity model** that both feature sets could sit
on top of. See [🔀 Merging FWA + Cinema](#-merging-fwa--cinema) below for
how that shook out.

## 🗂️ Repository structure

```
spring-boot-cinema/
├── ex00/
│   └── Cinema/            # Spring Security: roles, custom login, remember-me, CSRF
├── ex01/
│   └── Cinema/             # + Localization (i18n) & Bean Validation
└── ex02/
    └── Cinema/             # + Email account confirmation  (subject calls this "Exercice 03" — see note below)
```

Each `Cinema/` folder is a **complete, independently buildable** Maven
project — later exercises are supersets of earlier ones. Every folder
ships its own `README.md`, `schema.sql`, and `data.sql`, per the
subject's turn-in requirements.

> **A note on the ex02 / ex03 naming:** the subject PDF's table of
> contents skips straight from *"Exercice 01"* to *"Exercice 03: Mails"*
> — there is no *Exercice 02* anywhere in the document. The folder here
> is named `ex02` to keep the turn-in directories sequential, but
> functionally it *is* the subject's "Exercice 03."

## ✨ What each exercise adds

| Exercise | Adds |
|---|---|
| **ex00 — Spring Security** | Role-based access control (`ADMIN` / `USER`), custom `/signIn` & `/signUp` pages (no Spring Security default login page), `UserDetails`/`UserDetailsService`, remember-me, CSRF protection, login history, real-time WebSocket film chat (STOMP/SockJS), unified avatar/poster upload pipeline |
| **ex01 — Localization & Validation** | Cookie-based i18n across ≥3 pages via `?lang=`, `LocaleResolver`/`LocaleChangeInterceptor`/`MessageSource` beans, Bean Validation on sign-up (name, email, phone, password strength) via `LocalValidatorFactoryBean` + a custom `@ValidPassword` constraint, fully localized field-level error messages |
| **ex02 (subject's "ex03") — Mails** | Email-confirmed accounts: `NOT_CONFIRMED` on sign-up, UUID confirmation link sent via `JavaMailSender` (Gmail SMTP), `/confirm/{token}` flips status, login blocked for unconfirmed accounts via `UserDetails.isEnabled()`, admin auto-confirmed |

## 🏗️ Architecture

```
                         ┌────────────────────────────┐
                         │       SecurityConfig       │
                         │  role matrix · CSRF ·      │
                         │  remember-me · custom login│
                         └──────────────┬─────────────┘
                                        │
 Browser ──▶ DispatcherServlet ──▶ SecurityFilterChain ──▶ Controller
                                        │                         │
                         ┌──────────────┴──────────────┐          ▼
                         │                             │       Service
                  LocaleChangeInterceptor       @Valid + BindingResult
                  (i18n, cookie "lang")          (ValidPassword, etc.)
                         │                             │          │
                         ▼                             │          ▼
                  MessageSource ◀── messages.properties│    Repository
                                                       │   (Spring Data JPA)
                                                       │          │
                                                       ▼          ▼
                                                  FreeMarker  PostgreSQL
                                                  (.ftl views)


       ┌─────────────────────────┐        ┌────────────────────────────┐
       │    WebSocket / STOMP    │        │       EmailService         │
       │ /ws (SockJS) → /app →   │        │ JavaMailSender (Gmail)     │
       │ broker "/films/{id}"    │        │ /confirm/{UUID} → status   │
       │ identity = Principal,   │        │ CONFIRMED, login gated     │
       │ never client-sent       │        │ via isEnabled()            │
       └─────────────────────────┘        └────────────────────────────┘
```

## 🔀 Merging FWA + Cinema

The most consequential decisions in this repo happened at the schema
level, before any Spring Boot code existed. A few worth calling out:

- **One `users` table, not two.** FWA's real, authenticated user replaced
  Cinema's anonymous `cinemaUserId` cookie everywhere — chat messages,
  uploads, and (what used to be) visit logs all now carry a real `user_id`
  foreign key instead of a browser-generated UUID string.
- **`user_visit` was retired entirely**, not migrated. It existed in
  Cinema only as a workaround for not having real logins — once
  `authentication_log` (from FWA) is backed by real accounts, it
  strictly subsumes what visit-logging was trying to approximate.
- **Three separate, partially-untracked upload mechanisms became one.**
  FWA tracked avatar uploads in a DB table; Cinema tracked chat avatar
  uploads in a *different* DB table keyed to the anonymous cookie; film
  poster uploads weren't tracked in any table at all — just written
  straight to disk. All three are now one `uploaded_file` table with a
  `context` (`AVATAR` / `POSTER`), served through a single
  `/images/{storedName}` endpoint.
- **Avatars are a single pool per user, not per-page.** The original
  Cinema subject only ever described *one* avatar-upload feature (the
  "latest upload wins" pattern) — there was never a real distinction
  between a "profile picture" and a "chat picture." Uploading a new
  avatar from `/profile` or from any film's chat page updates the same
  image everywhere.
- **Chat identity is never client-supplied.** Even after merging, the
  STOMP handler resolves the sender from the authenticated `Principal`
  on the WebSocket session — a message's `content` is the only thing the
  client actually sends.

## 🔧 Tech stack

| Layer | Technology |
|---|---|
| Language / runtime | Java 21 (LTS) |
| Framework | Spring Boot 4.1.0 |
| Security | Spring Security 6 — custom `UserDetails`, remember-me, CSRF |
| Persistence | Spring Data JPA (Hibernate), hand-written `schema.sql`/`data.sql` (`ddl-auto=validate`, never generates schema) |
| Database | PostgreSQL |
| Templates | FreeMarker (`.ftl`) |
| Validation | Jakarta Bean Validation (Hibernate Validator) + custom `@ValidPassword` |
| Localization | Spring `MessageSource` + `CookieLocaleResolver` |
| Real-time | WebSocket / STOMP over SockJS |
| Mail | `JavaMailSender` (Gmail SMTP) |
| Build | Maven (wrapper) |
| Packaging | WAR (deployable to Tomcat 10+, or `spring-boot:run` with embedded Tomcat) |

## 🔑 Key URLs

| Path | Access | Description |
|------|--------|-------------|
| `/signIn` | Public | Custom login page (not Spring Security's default) |
| `/signUp` | Public | Registration, validated, sends confirmation email |
| `/confirm/{token}` | Public | Confirms account, redirects to `/signIn?confirmed` |
| `/signOut` | Authenticated | Logout (POST) |
| `/profile` | Authenticated | User info, login history, avatar upload |
| `/session/search` | Authenticated | Live AJAX session search |
| `/sessions/{id}` | Authenticated | Session details, link into film chat |
| `/films/{id}/chat` | Authenticated | Real-time per-film chat |
| `/admin/panel/halls`, `/films`, `/sessions` | `ADMIN` only | Catalog management |
| `/images`, `/images/{storedName}` | Authenticated | Upload / serve avatars & posters |
| `/ws` | Authenticated | SockJS WebSocket handshake |

Unauthenticated requests to anything other than `/signIn`, `/signUp`, or
`/confirm/**` are redirected to the login page. Authenticated users
hitting `/signIn` or `/signUp` are redirected onward — `USER` → `/profile`,
`ADMIN` → `/admin/panel/halls`.

## 🚀 Getting started

Each exercise is self-contained — pick the one you want and its own
`README.md` has exact steps. In general:

### Prerequisites
- Java 21
- PostgreSQL running locally
- A Gmail account with 2‑Step Verification + an **App Password** (ex02 only — required for the confirmation email to actually send)

### Database
```bash
psql -U postgres -c "CREATE DATABASE new_cinema;"
psql -U postgres -d new_cinema -f src/main/resources/sql/schema.sql
psql -U postgres -d new_cinema -f src/main/resources/sql/data.sql
```

### Build & run
```bash
cd ex02/Cinema      # or ex00 / ex01
./mvnw clean package -DskipTests
./mvnw spring-boot:run
# or: cp target/cinema.war $CATALINA_HOME/webapps/ && $CATALINA_HOME/bin/startup.sh
```

### Seed accounts (`data.sql`)

| Email | Password | Role | Status |
|-------|----------|------|--------|
| admin@cinema.com | admin123 | `ADMIN` | `CONFIRMED` (by default) |
| alice@example.com | password | `USER` | — |

## 📚 Subject reference

Built against the `Spring Boot — More, Than Spring` subject (42 School).
See each exercise's own `README.md` for its detailed requirements
mapping, and the subject PDF itself for the authoritative rules — per
its own "General Rules" chapter, it is the sole reference; this repo
does not deviate from it except where the document contains internal
contradictions (documented case-by-case in the relevant exercise's
README and commit history).

---

*Built as part of the 42 curriculum. Spring Boot 4.1.0, Java 21, PostgreSQL — spiritual successor to [FWA](https://github.com/ssbaytri/FWA) and the original [Cinema](https://github.com/ssbaytri/Cinema) prototypes.*
