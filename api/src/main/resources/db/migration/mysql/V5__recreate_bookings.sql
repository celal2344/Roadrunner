-- Recreates the bookings table after V4 removed the discontinued feature.
-- Kept as a new migration because applied Flyway migrations must stay immutable.
CREATE TABLE bookings (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NULL,
    user_id BIGINT NOT NULL,
    car_id BIGINT NOT NULL,
    start_at DATETIME(6) NOT NULL,
    end_at DATETIME(6) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED') NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    CONSTRAINT pk_bookings PRIMARY KEY (id),
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_bookings_car FOREIGN KEY (car_id) REFERENCES cars (id),
    CONSTRAINT chk_bookings_date_range CHECK (end_at > start_at),
    CONSTRAINT chk_bookings_total_price CHECK (total_price > 0)
);

CREATE INDEX idx_bookings_car_dates_status
    ON bookings (car_id, start_at, end_at, status);

CREATE INDEX idx_bookings_user
    ON bookings (user_id);
