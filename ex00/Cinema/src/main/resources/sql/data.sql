-- data.sql
-- Test data for the Spring Boot Cinema application.
-- Run this manually after schema.sql, e.g.:
--   psql -d new_cinema -f schema.sql -f data.sql
-- Passwords (BCrypt):
--   admin     / admin123
--   alice     / password

-- ============================================================
-- Users
-- ============================================================

INSERT INTO users (first_name, last_name, phone_number, email, password_hash, role, status)
VALUES ('Admin', 'Cinema', '+7(700)000000', 'admin@cinema.com',
        '$2b$10$okzkZ3Hd.jx9KcSoC1tUq.Saj7rkP.qCkuaL2Wj6OIbs3LLn3Ng1W',
        'ADMIN', 'CONFIRMED');

INSERT INTO users (first_name, last_name, phone_number, email, password_hash, role, status)
VALUES ('Alice', 'Smith', '+7(777)777777', 'alice@example.com',
        '$2b$10$IKDYeMJJy/iF/wj98Lxqp.YPUYM.ZqzS4a4dSUanVGBtJNsQBZm1.',
        'USER', 'CONFIRMED');

-- ============================================================
-- Halls
-- ============================================================

INSERT INTO hall (serial_number, seats_number) VALUES ('H-01', 120);
INSERT INTO hall (serial_number, seats_number) VALUES ('H-02', 80);
INSERT INTO hall (serial_number, seats_number) VALUES ('H-03', 200);

-- ============================================================
-- Films
-- ============================================================

INSERT INTO film (title, release_year, age_restriction, description)
VALUES ('Inception', 2010, 12, 'A thief who steals corporate secrets through dream-sharing technology.');

INSERT INTO film (title, release_year, age_restriction, description)
VALUES ('Interstellar', 2014, 12, 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity''s survival.');

INSERT INTO film (title, release_year, age_restriction, description)
VALUES ('The Dark Knight', 2008, 12, 'Batman raises the stakes in his war on crime against the Joker.');

-- ============================================================
-- Sessions
-- ============================================================

INSERT INTO session (film_id, hall_id, date_time, ticket_price)
VALUES (1, 1, '2026-08-20 18:30:00', 500.00);
INSERT INTO session (film_id, hall_id, date_time, ticket_price)
VALUES (1, 2, '2026-08-20 21:00:00', 450.00);
INSERT INTO session (film_id, hall_id, date_time, ticket_price)
VALUES (2, 3, '2026-08-21 19:00:00', 600.00);
INSERT INTO session (film_id, hall_id, date_time, ticket_price)
VALUES (3, 1, '2026-08-22 20:00:00', 550.00);
