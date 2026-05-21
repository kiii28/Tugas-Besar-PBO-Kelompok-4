-- ============================================================
--  EcoRide Database
--  Pemrograman Berorientasi Objek - Kelompok 4 IF-48-02
--  Universitas Telkom 2025
-- ============================================================

CREATE DATABASE IF NOT EXISTS ecoride_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE ecoride_db;

-- ------------------------------------------------------------
--  Tabel: roles
--  Menyimpan jenis role: Admin atau User
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS roles (
    id          INT          NOT NULL AUTO_INCREMENT,
    role_name   VARCHAR(20)  NOT NULL UNIQUE,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

INSERT INTO roles (role_name) VALUES ('Admin'), ('User');

-- ------------------------------------------------------------
--  Tabel: members
--  Merepresentasikan class Member (extends Account)
--  password disimpan dalam bentuk SHA-256 hash
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS members (
    id               INT            NOT NULL AUTO_INCREMENT,
    username         VARCHAR(50)    NOT NULL UNIQUE,
    password_hash    VARCHAR(255)   NOT NULL,
    balance          DOUBLE         NOT NULL DEFAULT 0.0,
    membership_type  VARCHAR(30)    NOT NULL DEFAULT 'Regular',
    role_id          INT            NOT NULL DEFAULT 2,
    created_at       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_member_role FOREIGN KEY (role_id) REFERENCES roles(id)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
--  Tabel: vehicles
--  Merepresentasikan class Vehicle (ElectricBike & ElectricScooter)
--  kolom vehicle_type membedakan jenis kendaraan
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS vehicles (
    id              INT            NOT NULL AUTO_INCREMENT,
    vehicle_id      VARCHAR(20)    NOT NULL UNIQUE,
    model           VARCHAR(100)   NOT NULL,
    vehicle_type    ENUM('bike','scooter') NOT NULL,
    battery_level   DOUBLE         NOT NULL DEFAULT 100.0,
    is_available    TINYINT(1)     NOT NULL DEFAULT 1,
    -- khusus ElectricBike
    has_pedals      TINYINT(1)     NULL DEFAULT NULL,
    -- khusus ElectricScooter
    max_speed       INT            NULL DEFAULT NULL,
    -- harga sewa per menit
    rent_per_minute DOUBLE         NOT NULL DEFAULT 500.0,
    created_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
--  Tabel: rental_transactions
--  Merepresentasikan class RentalTransaction
--  relasi: 1 member bisa punya banyak transaksi (0..*)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS rental_transactions (
    id              INT            NOT NULL AUTO_INCREMENT,
    transaction_id  VARCHAR(30)    NOT NULL UNIQUE,
    member_id       INT            NOT NULL,
    vehicle_id      INT            NOT NULL,
    start_time      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_time        DATETIME       NULL DEFAULT NULL,
    total_cost      DOUBLE         NOT NULL DEFAULT 0.0,
    status          ENUM('active','completed','cancelled') NOT NULL DEFAULT 'active',
    PRIMARY KEY (id),
    CONSTRAINT fk_trx_member  FOREIGN KEY (member_id)  REFERENCES members(id),
    CONSTRAINT fk_trx_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
) ENGINE=InnoDB;

-- ============================================================
--  DATA AWAL (Seed Data)
-- ============================================================

-- Admin default (password: admin123)
-- SHA-256 dari "admin123" = 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
INSERT INTO members (username, password_hash, balance, membership_type, role_id)
VALUES ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 0.0, 'Admin', 1);

-- Member contoh (password: user123)
-- SHA-256 dari "user123" = 483af8814bd0c6e0f3b59f672739e3e8ab0a81bc5e2e61ac51b039a40e76e0c3
INSERT INTO members (username, password_hash, balance, membership_type, role_id)
VALUES ('john_doe', '483af8814bd0c6e0f3b59f672739e3e8ab0a81bc5e2e61ac51b039a40e76e0c3', 150000.0, 'Regular', 2);

-- Kendaraan contoh - ElectricBike
INSERT INTO vehicles (vehicle_id, model, vehicle_type, battery_level, is_available, has_pedals, max_speed, rent_per_minute)
VALUES
    ('BIKE-001', 'Polygon Zinq E3',   'bike', 95.0, 1, 1, NULL, 300.0),
    ('BIKE-002', 'United Trekko E',   'bike', 80.0, 1, 1, NULL, 300.0),
    ('BIKE-003', 'Element Ecostar',   'bike', 60.0, 1, 0, NULL, 250.0);

-- Kendaraan contoh - ElectricScooter
INSERT INTO vehicles (vehicle_id, model, vehicle_type, battery_level, is_available, has_pedals, max_speed, rent_per_minute)
VALUES
    ('SCOOT-001', 'Xiaomi Mi Scooter 3', 'scooter', 100.0, 1, NULL, 25, 600.0),
    ('SCOOT-002', 'Segway Ninebot E25',  'scooter',  90.0, 1, NULL, 25, 600.0),
    ('SCOOT-003', 'Ninebot Max G30',     'scooter',  75.0, 0, NULL, 30, 700.0);

-- ============================================================
--  VIEW: transaksi lengkap (berguna untuk halaman admin)
-- ============================================================
CREATE OR REPLACE VIEW v_transactions_detail AS
SELECT
    rt.id,
    rt.transaction_id,
    m.username          AS member_username,
    m.membership_type,
    v.vehicle_id        AS vehicle_code,
    v.model             AS vehicle_model,
    v.vehicle_type,
    rt.start_time,
    rt.end_time,
    TIMESTAMPDIFF(MINUTE, rt.start_time, IFNULL(rt.end_time, NOW())) AS duration_minutes,
    rt.total_cost,
    rt.status
FROM rental_transactions rt
JOIN members  m ON rt.member_id  = m.id
JOIN vehicles v ON rt.vehicle_id = v.id;

-- ============================================================
--  VIEW: kendaraan tersedia
-- ============================================================
CREATE OR REPLACE VIEW v_available_vehicles AS
SELECT
    id,
    vehicle_id,
    model,
    vehicle_type,
    battery_level,
    has_pedals,
    max_speed,
    rent_per_minute
FROM vehicles
WHERE is_available = 1
  AND battery_level > 10;
