# ex01 - Localization & Validation

This is the **Exercise 01** implementation for the "Spring Boot — More, Than Spring" subject (42 School).

## Overview

Builds upon ex00 with **localization (i18n)** and **form validation** for the registration page.

## Features Implemented

### Localization (i18n)
- **Two languages**: English (default) + Spanish
- **Cookie-based persistence**: Language choice stored in `lang` cookie (365 days)
- **URL parameter switching**: `?lang=en` or `?lang=es` on any page
- **3 pages localized**: Sign In, Sign Up, Profile
- **Language selector** in header on all 3 pages

### Validation (Bean Validation / JSR-380)
- **Custom `@ValidPassword` annotation** with `PasswordConstraintValidator`
- **Registration form validation** on all fields:
  - First Name: required
  - Last Name: required
  - Phone Number: required, pattern `+\d+(\d+)\d+` (e.g., +7(777)777777)
  - Email: required, valid email format
  - Password: required, 8+ chars, uppercase, lowercase, digit
- **Field-level error display** in localized messages
- **Input preservation** on validation failure
- **Localized error messages** in both English and Spanish

## Tech Stack Additions (on top of ex00)

| Component | Purpose |
|-----------|---------|
| `spring-boot-starter-validation` | Bean Validation API + Hibernate Validator |
| `LocalValidatorFactoryBean` | Spring-managed validator with MessageSource integration |
| `MessageCodesResolver` | Default message codes resolver |
| Custom `ValidPassword` annotation | Password complexity validation |

## Project Structure (key additions)

```
ex01/Cinema/
├── src/
│   ├── main/
│   │   ├── java/fr/_42/cinema/
│   │   │   ├── config/
│   │   │   │   └── LocalizationConfig.java    # LocaleResolver, LocaleChangeInterceptor, MessageSource, Validator, MessageCodesResolver
│   │   │   ├── dto/
│   │   │   │   └── SignUpRequestDTO.java      # Validation annotations
│   │   │   ├── validation/
│   │   │   │   ├── ValidPassword.java         # Custom annotation
│   │   │   │   └── PasswordConstraintValidator.java
│   │   │   └── controllers/
│   │   │       └── SignUpController.java      # @Valid + BindingResult
│   │   ├── resources/
│   │   │   ├── messages.properties            # English (default)
│   │   │   ├── messages_es.properties         # Spanish
│   │   │   └── templates/
│   │   │       ├── signIn.ftl                 # Localized
│   │   │       ├── signUp.ftl                 # Localized + validation errors
│   │   │       └── profile.ftl                # Localized
│   │   └── static/
│   │       └── css/style.css                  # .field-error, input.error styles
```

## Configuration

### LocalizationConfig.java
```java
@Bean LocaleResolver localeResolver()           // CookieLocaleResolver, cookie="lang", 365 days
@Bean LocaleChangeInterceptor localeChangeInterceptor() // param="lang"
@Bean MessageSource messageSource()             // basename="messages", UTF-8
@Bean LocalValidatorFactoryBean validator()     // linked to MessageSource
@Bean MessageCodesResolver messageCodesResolver() // DefaultMessageCodesResolver
```

### Validation Annotations (SignUpRequestDTO)
```java
@NotEmpty(message = "{errors.firstName.required}")
private String firstName;

@NotEmpty(message = "{errors.lastName.required}")
private String lastName;

@NotEmpty(message = "{errors.phoneNumber.required}")
@Pattern(regexp = "^\\+\\d{1,3}\\(\\d+\\)\\d+$", message = "{errors.incorrect.phone}")
private String phoneNumber;

@NotEmpty(message = "{errors.email.required}")
@Email(message = "{errors.incorrect.email}")
private String email;

@NotEmpty(message = "{errors.password.required}")
@ValidPassword(message = "{errors.incorrect.password}")
private String password;
```

### Password Validator Logic
- Minimum 8 characters
- At least 1 uppercase letter
- At least 1 lowercase letter
- At least 1 digit

## Running the Application

Same as ex00 - see [ex00 README](../ex00/README.md) for build/deploy instructions.

## Testing Checklist

### Localization
- [ ] `http://localhost:8080/cinema/signIn?lang=en` → English UI
- [ ] `http://localhost:8080/cinema/signIn?lang=es` → Spanish UI
- [ ] Language persists after browser refresh (cookie)
- [ ] Language selector works on Sign In, Sign Up, Profile pages

### Validation
- [ ] Submit empty registration form → all 5 field errors displayed in current language
- [ ] Invalid email format → "Invalid email format" / "Formato de correo inválido"
- [ ] Invalid phone format (not +7(777)777777) → pattern error in current language
- [ ] Weak password (short, no upper/lower/digit) → password error in current language
- [ ] Valid registration → redirects to `/signIn`
- [ ] Switch language after validation error → error messages translate
- [ ] Input values preserved after validation failure

### Error Message Keys (both languages)
| Key | English | Spanish |
|-----|---------|---------|
| `errors.firstName.required` | First name is required | El nombre es obligatorio |
| `errors.lastName.required` | Last name is required | El apellido es obligatorio |
| `errors.phoneNumber.required` | Phone number is required | El teléfono es obligatorio |
| `errors.incorrect.phone` | Phone must match +(code)digits... | El teléfono debe seguir el patrón... |
| `errors.email.required` | Email is required | El correo es obligatorio |
| `errors.incorrect.email` | Invalid email format | Formato de correo inválido |
| `errors.password.required` | Password is required | La contraseña es obligatoria |
| `errors.incorrect.password` | Password must be at least 8 chars... | La contraseña debe tener al menos 8... |

## Subject Requirements Mapping

| Requirement | Implementation |
|-------------|----------------|
| Two languages of choice | English + Spanish |
| Language change via `lang` param | `LocaleChangeInterceptor` |
| Localization stored in cookies | `CookieLocaleResolver` (365 days) |
| At least 3 pages localized | Sign In, Sign Up, Profile |
| Localized validation messages | All error keys in both properties files |
| Registration form validation | All 5 fields with constraints |
| First/last name non-empty | `@NotEmpty` |
| Email format validation | `@Email` |
| Phone pattern `+(code)digits` | `@Pattern(regexp="^\\+\\d{1,3}\\(\\d+\\)\\d+$")` |
| Password: 8+ chars, upper, lower, digit | Custom `@ValidPassword` + `ConstraintValidator` |
| Properties files for messages | `messages.properties`, `messages_es.properties` |
| Beans: LocaleResolver, LocaleChangeInterceptor, LocalValidatorFactoryBean, MessageSource, MessageCodesResolver | All in `LocalizationConfig` |
| `javax.validation.constraints.*` annotations | `@NotEmpty`, `@Email`, `@Pattern`, `@Valid` |
| Custom `@ValidPassword` + `ConstraintValidator` | `ValidPassword.java`, `PasswordConstraintValidator.java` |

---

*Part of the 42 School Spring Boot curriculum. Builds on ex00 (Spring Security). Next: ex03 (Mails).*