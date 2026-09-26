-- ============================================
-- EVENTHUB - EVENT & TICKET BOOKING SYSTEM
-- Database: MySQL 8.0
-- ============================================

CREATE DATABASE IF NOT EXISTS eventhub;

USE eventhub;


-- ============================================
-- 1. USERS TABLE
-- ============================================

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- ============================================
-- 2. EVENTS TABLE
-- ============================================

CREATE TABLE events (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    event_date DATE NOT NULL,
    event_time TIME NOT NULL,
    venue VARCHAR(200) NOT NULL,
    ticket_price DECIMAL(10,2) NOT NULL,
    capacity INT NOT NULL,
    status ENUM('UPCOMING', 'ONGOING', 'COMPLETED', 'CANCELLED')
        NOT NULL DEFAULT 'UPCOMING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- ============================================
-- 3. SEATS TABLE
-- ============================================

CREATE TABLE seats (
    id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    status ENUM('AVAILABLE', 'BOOKED')
        NOT NULL DEFAULT 'AVAILABLE',

    CONSTRAINT fk_seats_event
        FOREIGN KEY (event_id)
        REFERENCES events(id)
        ON DELETE CASCADE,

    CONSTRAINT unique_event_seat
        UNIQUE (event_id, seat_number)
);


-- ============================================
-- 4. BOOKINGS TABLE
-- ============================================

CREATE TABLE bookings (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('CONFIRMED', 'CANCELLED')
        NOT NULL DEFAULT 'CONFIRMED',

    CONSTRAINT fk_bookings_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_bookings_event
        FOREIGN KEY (event_id)
        REFERENCES events(id)
);


-- ============================================
-- 5. BOOKING_SEATS TABLE
-- ============================================

CREATE TABLE booking_seats (
    id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT NOT NULL,
    seat_id INT NOT NULL,

    CONSTRAINT fk_booking_seats_booking
        FOREIGN KEY (booking_id)
        REFERENCES bookings(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_booking_seats_seat
        FOREIGN KEY (seat_id)
        REFERENCES seats(id),

    CONSTRAINT unique_booking_seat
        UNIQUE (booking_id, seat_id)
);


-- ============================================
-- 6. TICKETS TABLE
-- ============================================

CREATE TABLE tickets (
    id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT NOT NULL UNIQUE,
    ticket_code VARCHAR(50) NOT NULL UNIQUE,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tickets_booking
        FOREIGN KEY (booking_id)
        REFERENCES bookings(id)
        ON DELETE CASCADE
);


-- ============================================
-- SAMPLE ADMIN ACCOUNT
-- ============================================

INSERT INTO users (name, email, password, role)
VALUES (
    'EventHub Admin',
    'admin@eventhub.com',
    'admin123',
    'ADMIN'
);


-- ============================================
-- SAMPLE USER ACCOUNT
-- ============================================

INSERT INTO users (name, email, password, role)
VALUES (
    'Harshini',
    'harshini@eventhub.com',
    'user123',
    'USER'
);


-- ============================================
-- SAMPLE EVENTS
-- ============================================

INSERT INTO events
(name, description, event_date, event_time, venue, ticket_price, capacity)
VALUES
(
    'College Fest 2026',
    'Annual college cultural and technical festival.',
    '2026-09-25',
    '10:00:00',
    'College Auditorium',
    150.00,
    25
),
(
    'Cultural Night',
    'An evening filled with music, dance and entertainment.',
    '2026-09-30',
    '18:00:00',
    'Main Hall',
    100.00,
    25
),
(
    'Tech Symposium',
    'Technical event featuring competitions and presentations.',
    '2026-10-05',
    '09:30:00',
    'Seminar Hall',
    200.00,
    25
);


-- ============================================
-- CREATE SEATS FOR EVENTS
-- ============================================

INSERT INTO seats (event_id, seat_number)
SELECT id, 'A1' FROM events WHERE name = 'College Fest 2026';

INSERT INTO seats (event_id, seat_number)
SELECT id, 'A2' FROM events WHERE name = 'College Fest 2026';

INSERT INTO seats (event_id, seat_number)
SELECT id, 'A3' FROM events WHERE name = 'College Fest 2026';

INSERT INTO seats (event_id, seat_number)
SELECT id, 'A4' FROM events WHERE name = 'College Fest 2026';

INSERT INTO seats (event_id, seat_number)
SELECT id, 'A5' FROM events WHERE name = 'College Fest 2026';


INSERT INTO seats (event_id, seat_number)
SELECT id, 'B1' FROM events WHERE name = 'College Fest 2026';

INSERT INTO seats (event_id, seat_number)
SELECT id, 'B2' FROM events WHERE name = 'College Fest 2026';

INSERT INTO seats (event_id, seat_number)
SELECT id, 'B3' FROM events WHERE name = 'College Fest 2026';

INSERT INTO seats (event_id, seat_number)
SELECT id, 'B4' FROM events WHERE name = 'College Fest 2026';

INSERT INTO seats (event_id, seat_number)
SELECT id, 'B5' FROM events WHERE name = 'College Fest 2026';


INSERT INTO seats (event_id, seat_number)
SELECT id, 'C1' FROM events WHERE name = 'College Fest 2026';

INSERT INTO seats (event_id, seat_number)
SELECT id, 'C2' FROM events WHERE name = 'College Fest 2026';

INSERT INTO seats (event_id, seat_number)
SELECT id, 'C3' FROM events WHERE name = 'College Fest 2026';

INSERT INTO seats (event_id, seat_number)
SELECT id, 'C4' FROM events WHERE name = 'College Fest 2026';

INSERT INTO seats (event_id, seat_number)
SELECT id, 'C5' FROM events WHERE name = 'College Fest 2026';


-- ============================================
-- END OF EVENTHUB DATABASE
-- ============================================