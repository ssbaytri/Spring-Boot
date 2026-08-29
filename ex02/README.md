# ex02 - Mails & Account Confirmation (Exercise 03)

This is the **Exercise 03** implementation for the "Spring Boot — More, Than Spring" subject (42 School).  
*Note: The folder is named `ex02` due to a numbering typo in the repository structure; the subject refers to this as **Exercice 03 : Mails**.*

## Overview

Builds upon ex01 with **email-based account confirmation**:
- Users register with `NOT_CONFIRMED` status
- Confirmation email sent with UUID link: `/confirm/{token}`
- Only `CONFIRMED` users can authenticate
- Admin users are `CONFIRMED` by default

## Features Implemented

### Account Confirmation Flow
1. **Registration** (`POST /signUp`):
   - Validates all fields (via ex01 validation)
   - Creates user with `status=NOT_CONFIRMED` + random `confirmation_token` (UUID)
   - Sends confirmation email via `JavaMailSender` (Gmail SMTP)
   - Redirects to `/signIn?registered`

2. **Email Link** (`GET /confirm/{token}`):
   - `ConfirmationController` validates token
   - If valid: sets `status=CONFIRMED`, clears token, redirects to `/signIn?confirmed`
   - If invalid/used: redirects to `/signIn?invalidToken`

3. **Sign In** (`POST /signIn`):
   - `CinemaUserDetails.isEnabled()` returns `true` only for `CONFIRMED` users
   - `CinemaAuthenticationFailureHandler` detects `DisabledException` → `/signIn?disabled`
   - Success messages shown for `registered`, `confirmed`, `disabled`, `invalidToken`

### Security Updates
- `/confirm/**` permitted for all (no auth required)
- `CinemaUserDetails.isEnabled()` enforces `CONFIRMED` status
- Custom `AuthenticationFailureHandler` for disabled accounts
- Remember-me, CSRF, role-based access from ex00/ex01 preserved

### Localization (from ex01)
- English + Spanish for all messages
- New keys: `signin.disabled`, `signin.registered`, `signin.confirmed`, `signin.invalidToken`

## Tech Stack Additions (on top of ex01)

| Component | Purpose |
|-----------|---------|
| `spring-boot-starter-mail` | `JavaMailSender` for SMTP emails |
| Gmail SMTP config | `spring.mail.*` properties in `application.properties` |
| `ConfirmationController` | Handles `/confirm/{token}` endpoint |
| `EmailService` + `EmailServiceImpl` | Sends confirmation emails |
| `UserStatus` enum | `CONFIRMED` / `NOT_CONFIRMED` |
| `confirmation_token` column | UUID stored on user record |

## Project Structure (key additions)

```
ex02/Cinema/
├── src/
│   ├── main/
│   │   ├── java/fr/_42/cinema/
│   │   │   ├── config/
│   │   │   │   ├── LocalizationConfig.java     # messageSource basename="messages/messages"
│   │   │   │   ├── ValidationConfig.java       # LocalValidatorFactoryBean, MessageCodesResolver
│   │   │   │   └── SecurityConfig.java         # permits /confirm/**, disabled handling
│   │   │   ├── controllers/
│   │   │   │   ├── SignUpController.java       # sends confirmation email
│   │   │   │   ├── SignInController.java       # handles registered/confirmed/disabled params
│   │   │   │   └── ConfirmationController.java # /confirm/{token}
│   │   │   ├── security/
│   │   │   │   ├── CinemaUserDetails.java      # isEnabled() checks UserStatus.CONFIRMED
│   │   │   │   ├── CinemaUserDetailsService.java
│   │   │   │   ├── CinemaAuthenticationFailureHandler.java
│   │   │   │   └── RoleBasedAuthenticationSuccessHandler.java
│   │   │   ├── services/
│   │   │   │   ├── EmailService.java
│   │   │   │   └── EmailServiceImpl.java       # JavaMailSender
│   │   │   ├── models/
│   │   │   │   ├── User.java                   # status, confirmationToken fields
│   │   │   │   └── UserStatus.java             # CONFIRMED, NOT_CONFIRMED
│   │   │   └── repositories/
│   │   │       └── UserRepository.java         # findByConfirmationToken(UUID)
│   │   ├── resources/
│   │   │   ├── messages/
│   │   │   │   ├── messages.properties         # English
│   │   │   │   └── messages_es.properties      # Spanish
│   │   │   ├── application.properties          # app.base-url, spring.mail.*
│   │   │   ├── sql/
│   │   │   │   ├── schema.sql                  # users.status, users.confirmation_token
│   │   │   │   └── data.sql                    # test users with CONFIRMED status
│   │   │   └── templates/
│   │   │       └── signIn.ftl                  # shows all 5 message types
```

## Configuration

### application.properties (key additions)
```properties
# Base URL for confirmation links (change for production)
app.base-url=http://localhost:8080

# Gmail SMTP - USE APP PASSWORD (not real password)
# Enable 2FA on Gmail, generate app password at: https://myaccount.google.com/apppasswords
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com
```

### SecurityConfig.java (key changes)
```java
// Permit confirmation endpoint
.requestMatchers("/confirm/**").permitAll()

// Custom failure handler for disabled (not confirmed) accounts
.failureHandler(authenticationFailureHandler())

// Remember-me, CSRF, role-based access from ex00
```

## Running the Application

### Prerequisites
1. PostgreSQL database `new_cinema` (same as ex00/ex01)
2. **Gmail account with 2FA enabled + App Password** for email sending
3. Update `application.properties` with your Gmail credentials

### Build & Deploy
```bash
cd ex02/Cinema
export JAVA_HOME=/mnt/c/Program\ Files/Java/jdk-21
./mvnw clean package -DskipTests
cp target/cinema.war /opt/tomcat/webapps/
/opt/tomcat/bin/startup.sh
```

### Access
```
http://localhost:8080/
```

## Testing Checklist

### Account Confirmation Flow
- [ ] Register new user → redirected to `/signIn?registered`
- [ ] Check email → click confirmation link → `/signIn?confirmed`
- [ ] Sign in with confirmed account → success → `/profile` (or admin panel)
- [ ] Try sign in before confirmation → `/signIn?disabled` message
- [ ] Use invalid/expired token → `/signIn?invalidToken` message
- [ ] Admin user (from data.sql) → signs in directly (CONFIRMED by default)

### Email Configuration
- [ ] Gmail App Password configured in `application.properties`
- [ ] `app.base-url` matches deployment URL
- [ ] Email received with working `/confirm/{token}` link

## Subject Requirements Mapping

| Requirement | Implementation |
|-------------|----------------|
| User model: confirmed field | `UserStatus` enum + `status` column |
| Only verified users access app | `CinemaUserDetails.isEnabled()` checks `CONFIRMED` |
| Admin verified by default | `data.sql` inserts admin with `CONFIRMED` |
| Confirmation link: `/confirm/{UUID}` | `ConfirmationController` + UUID token |
| Confirmation page → sign in | Redirects to `/signIn?confirmed` |
| Email via `JavaMailSender` | `EmailServiceImpl` with `SimpleMailMessage` |
| Gmail SMTP config | `application.properties` with app password |
| Localization preserved | All messages in `messages.properties` / `messages_es.properties` |
| Validation preserved | ex01 Bean Validation on registration form |

---

*Part of the 42 School Spring Boot curriculum. Builds on ex00 (Spring Security) and ex01 (Localization & Validation).*
