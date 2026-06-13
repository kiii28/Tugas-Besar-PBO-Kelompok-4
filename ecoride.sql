-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 13 Jun 2026 pada 19.28
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ecoride_db`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `electric_bikes`
--

CREATE TABLE `electric_bikes` (
  `vehicle_id` varchar(20) NOT NULL,
  `has_pedals` tinyint(1) DEFAULT 1,
  `price_per_minute` double DEFAULT 500
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `electric_bikes`
--

INSERT INTO `electric_bikes` (`vehicle_id`, `has_pedals`, `price_per_minute`) VALUES
('BIKE-001', 1, 500),
('BIKE-002', 1, 450);

-- --------------------------------------------------------

--
-- Struktur dari tabel `electric_scooters`
--

CREATE TABLE `electric_scooters` (
  `vehicle_id` varchar(20) NOT NULL,
  `max_speed` int(11) DEFAULT 25,
  `price_per_minute` double DEFAULT 750
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `electric_scooters`
--

INSERT INTO `electric_scooters` (`vehicle_id`, `max_speed`, `price_per_minute`) VALUES
('SCOOT-001', 25, 750),
('SCOOT-002', 30, 800);

-- --------------------------------------------------------

--
-- Struktur dari tabel `members`
--

CREATE TABLE `members` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `balance` double DEFAULT 0,
  `membership_type` varchar(20) DEFAULT 'Regular',
  `role_id` int(11) NOT NULL DEFAULT 2,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `members`
--

INSERT INTO `members` (`id`, `username`, `password_hash`, `balance`, `membership_type`, `role_id`, `created_at`) VALUES
(1, 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 0, 'Admin', 1, '2026-06-12 04:19:19'),
(2, 'rifky', '3c8fe512459a840fbf6f4f69705b09cc0a35396c601a4de2d6b3098102fe816b', 50000, 'Regular', 2, '2026-06-12 04:19:19'),
(3, 'budi', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5', 75000, 'Premium', 2, '2026-06-12 04:19:19');

-- --------------------------------------------------------

--
-- Struktur dari tabel `rental_transactions`
--

CREATE TABLE `rental_transactions` (
  `transaction_id` varchar(30) NOT NULL,
  `member_id` int(11) NOT NULL,
  `vehicle_id` varchar(20) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  `total_cost` double DEFAULT 0,
  `status` varchar(20) DEFAULT 'ACTIVE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `role_name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `roles`
--

INSERT INTO `roles` (`id`, `role_name`) VALUES
(1, 'Admin'),
(2, 'User');

-- --------------------------------------------------------

--
-- Struktur dari tabel `vehicles`
--

CREATE TABLE `vehicles` (
  `vehicle_id` varchar(20) NOT NULL,
  `model` varchar(100) NOT NULL,
  `battery_level` double DEFAULT 100,
  `is_available` tinyint(1) DEFAULT 1,
  `vehicle_type` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `vehicles`
--

INSERT INTO `vehicles` (`vehicle_id`, `model`, `battery_level`, `is_available`, `vehicle_type`) VALUES
('BIKE-001', 'Polygon E-Bike Pro', 95, 1, 'BIKE'),
('BIKE-002', 'Exotic E-Folding', 80, 1, 'BIKE'),
('SCOOT-001', 'Xiaomi Mi Scooter 3', 100, 1, 'SCOOTER'),
('SCOOT-002', 'Segway Ninebot E45', 70, 1, 'SCOOTER');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `electric_bikes`
--
ALTER TABLE `electric_bikes`
  ADD PRIMARY KEY (`vehicle_id`);

--
-- Indeks untuk tabel `electric_scooters`
--
ALTER TABLE `electric_scooters`
  ADD PRIMARY KEY (`vehicle_id`);

--
-- Indeks untuk tabel `members`
--
ALTER TABLE `members`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `role_id` (`role_id`);

--
-- Indeks untuk tabel `rental_transactions`
--
ALTER TABLE `rental_transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `member_id` (`member_id`),
  ADD KEY `vehicle_id` (`vehicle_id`);

--
-- Indeks untuk tabel `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `vehicles`
--
ALTER TABLE `vehicles`
  ADD PRIMARY KEY (`vehicle_id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `members`
--
ALTER TABLE `members`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `electric_bikes`
--
ALTER TABLE `electric_bikes`
  ADD CONSTRAINT `electric_bikes_ibfk_1` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `electric_scooters`
--
ALTER TABLE `electric_scooters`
  ADD CONSTRAINT `electric_scooters_ibfk_1` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `members`
--
ALTER TABLE `members`
  ADD CONSTRAINT `members_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);

--
-- Ketidakleluasaan untuk tabel `rental_transactions`
--
ALTER TABLE `rental_transactions`
  ADD CONSTRAINT `rental_transactions_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`),
  ADD CONSTRAINT `rental_transactions_ibfk_2` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
