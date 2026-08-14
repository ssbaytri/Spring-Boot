-- schema.sql
-- Unified database schema for the Spring Boot Cinema application
-- Merges the authenticated-user model from FWA with the catalog/chat model from Cinema

DROP TABLE IF EXISTS uploaded_file CASCADE;
DROP TABLE IF EXISTS chat_message CASCADE;
DROP TABLE IF EXISTS session CASCADE;
DROP TABLE IF EXISTS film CASCADE;
DROP TABLE IF EXISTS hall CASCADE;
DROP TABLE IF EXISTS authentication_log CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================================
-- Identity
-- ============================================================

CREATE TABLE users
(
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    phone_number  VARCHAR(30)  NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER'
        CHECK (role IN ('ADMIN', 'USER')),
    status        VARCHAR(20)  NOT NULL DEFAULT 'NOT_CONFIRMED'
        CHECK (status IN ('CONFIRMED', 'NOT_CONFIRMED')),
    confirmation_token UUID
);

CREATE TABLE authentication_log
(
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    authenticated_at TIMESTAMP   NOT NULL DEFAULT now(),
    ip_address       VARCHAR(45) NOT NULL
);

CREATE INDEX idx_authentication_log_user_id ON authentication_log (user_id);

-- ============================================================
-- Catalog
-- ============================================================

CREATE TABLE hall
(
    id            BIGSERIAL PRIMARY KEY,
    serial_number VARCHAR(50) NOT NULL UNIQUE,
    seats_number  INTEGER     NOT NULL CHECK (seats_number > 0)
);

CREATE TABLE film
(
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    release_year    INTEGER      NOT NULL,
    age_restriction INTEGER      NOT NULL CHECK (age_restriction >= 0),
    description     TEXT,
    poster_url      VARCHAR(255)
);

CREATE TABLE session
(
    id           BIGSERIAL PRIMARY KEY,
    film_id      BIGINT        NOT NULL REFERENCES film (id) ON DELETE CASCADE,
    hall_id      BIGINT        NOT NULL REFERENCES hall (id) ON DELETE CASCADE,
    date_time    TIMESTAMP     NOT NULL,
    ticket_price NUMERIC(6, 2) NOT NULL CHECK (ticket_price >= 0)
);

CREATE INDEX idx_session_film_id ON session (film_id);
CREATE INDEX idx_session_hall_id ON session (hall_id);

-- ============================================================
-- Chat (now tied to real authenticated users)
-- ============================================================

CREATE TABLE chat_message
(
    id      BIGSERIAL PRIMARY KEY,
    film_id BIGINT    NOT NULL REFERENCES film (id) ON DELETE CASCADE,
    user_id BIGINT    NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    content TEXT      NOT NULL,
    sent_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_chat_message_film_id ON chat_message (film_id);
CREATE INDEX idx_chat_message_sent_at ON chat_message (sent_at);

-- ============================================================
-- Uploads (unifies what used to be 3 separate mechanisms:
-- FWA avatar uploads, Cinema chat avatar uploads, and untracked
-- film poster uploads)
-- ============================================================

CREATE TABLE uploaded_file
(
    id            BIGSERIAL PRIMARY KEY,
    owner_id      BIGINT REFERENCES users (id) ON DELETE CASCADE,
    context       VARCHAR(20)  NOT NULL
        CHECK (context IN ('AVATAR', 'CHAT', 'POSTER')),
    original_name VARCHAR(255) NOT NULL,
    stored_name   VARCHAR(255) NOT NULL UNIQUE,
    size_bytes    BIGINT,
    mime_type     VARCHAR(100),
    uploaded_at   TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX idx_uploaded_file_owner_id ON uploaded_file (owner_id);
CREATE INDEX idx_uploaded_file_context ON uploaded_file (context);