ALTER TABLE cars
    ADD COLUMN supplier VARCHAR(255) NOT NULL DEFAULT 'LEGACY',
    ADD COLUMN vehicle_type ENUM(
        'COMPACT',
        'FULL_SIZE',
        'LUXURY',
        'SUV',
        'PREMIUM_VAN'
    ) NOT NULL DEFAULT 'COMPACT',
    ADD COLUMN transmission_type ENUM(
        'AUTOMATIC',
        'MANUAL'
    ) NOT NULL DEFAULT 'MANUAL',
    ADD COLUMN fuel_type ENUM(
        'GASOLINE',
        'DIESEL',
        'ELECTRIC',
        'HYBRID'
    ) NOT NULL DEFAULT 'GASOLINE',
    ADD COLUMN seat_count INT NOT NULL DEFAULT 5,
    ADD COLUMN unlimited_mileage BIT(1) NOT NULL DEFAULT b'0',
    ADD COLUMN flexible_cancellation BIT(1) NOT NULL DEFAULT b'0',
    ADD COLUMN carla_cash_eligible BIT(1) NOT NULL DEFAULT b'0',
    ADD COLUMN collision_damage_waiver_included BIT(1) NOT NULL DEFAULT b'0',
    ADD COLUMN taxes_and_fees_included BIT(1) NOT NULL DEFAULT b'0';
