package com.ecoride.ecoride.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DATABASE = "ecoride_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static final String SERVER_URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/"
            + "?useSSL=false"
            + "&useUnicode=true"
            + "&characterEncoding=UTF-8"
            + "&serverTimezone=Asia/Jakarta"
            + "&allowPublicKeyRetrieval=true";

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?useSSL=false"
            + "&useUnicode=true"
            + "&characterEncoding=UTF-8"
            + "&serverTimezone=Asia/Jakarta"
            + "&allowPublicKeyRetrieval=true";

    private static boolean initialized = false;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[DBConnection] MySQL Driver berhasil dimuat.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver tidak ditemukan: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        initializeDatabase();
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("[DBConnection] Koneksi ke MySQL berhasil.");
            return conn;
        } catch (SQLException e) {
            System.err.println("[DBConnection] GAGAL konek ke database: " + e.getMessage());
            System.err.println("[DBConnection] URL: " + URL);
            throw e;
        }
    }

    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("[DBConnection] testConnection() GAGAL: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("[DBConnection] Gagal tutup koneksi test: " + e.getMessage());
                }
            }
        }
    }

    private static synchronized void initializeDatabase() throws SQLException {
        if (initialized) {
            return;
        }

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(SERVER_URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE
                    + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            stmt.executeUpdate("USE " + DATABASE);

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS roles ("
                    + "id INT NOT NULL AUTO_INCREMENT,"
                    + "role_name VARCHAR(20) NOT NULL UNIQUE,"
                    + "PRIMARY KEY (id)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS members ("
                    + "id INT NOT NULL AUTO_INCREMENT,"
                    + "username VARCHAR(50) NOT NULL UNIQUE,"
                    + "password_hash VARCHAR(255) NOT NULL,"
                    + "balance DOUBLE DEFAULT 0,"
                    + "membership_type VARCHAR(20) DEFAULT 'Regular',"
                    + "role_id INT NOT NULL DEFAULT 2,"
                    + "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "PRIMARY KEY (id),"
                    + "KEY role_id (role_id),"
                    + "CONSTRAINT members_role_fk FOREIGN KEY (role_id) REFERENCES roles (id)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS vehicles ("
                    + "vehicle_id VARCHAR(20) NOT NULL,"
                    + "model VARCHAR(100) NOT NULL,"
                    + "battery_level DOUBLE DEFAULT 100,"
                    + "is_available TINYINT(1) DEFAULT 1,"
                    + "vehicle_type VARCHAR(20) NOT NULL,"
                    + "PRIMARY KEY (vehicle_id)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS electric_bikes ("
                    + "vehicle_id VARCHAR(20) NOT NULL,"
                    + "has_pedals TINYINT(1) DEFAULT 1,"
                    + "price_per_minute DOUBLE DEFAULT 500,"
                    + "PRIMARY KEY (vehicle_id),"
                    + "CONSTRAINT electric_bikes_vehicle_fk FOREIGN KEY (vehicle_id) "
                    + "REFERENCES vehicles (vehicle_id) ON DELETE CASCADE"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS electric_scooters ("
                    + "vehicle_id VARCHAR(20) NOT NULL,"
                    + "max_speed INT DEFAULT 25,"
                    + "price_per_minute DOUBLE DEFAULT 750,"
                    + "PRIMARY KEY (vehicle_id),"
                    + "CONSTRAINT electric_scooters_vehicle_fk FOREIGN KEY (vehicle_id) "
                    + "REFERENCES vehicles (vehicle_id) ON DELETE CASCADE"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS rental_transactions ("
                    + "transaction_id VARCHAR(30) NOT NULL,"
                    + "member_id INT NOT NULL,"
                    + "vehicle_id VARCHAR(20) NOT NULL,"
                    + "start_time DATETIME NOT NULL,"
                    + "end_time DATETIME DEFAULT NULL,"
                    + "total_cost DOUBLE DEFAULT 0,"
                    + "status VARCHAR(20) DEFAULT 'ACTIVE',"
                    + "PRIMARY KEY (transaction_id),"
                    + "KEY member_id (member_id),"
                    + "KEY vehicle_id (vehicle_id),"
                    + "CONSTRAINT rental_transactions_member_fk FOREIGN KEY (member_id) REFERENCES members (id),"
                    + "CONSTRAINT rental_transactions_vehicle_fk FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS topup_requests ("
                    + "id INT NOT NULL AUTO_INCREMENT,"
                    + "member_id INT NOT NULL,"
                    + "amount DOUBLE NOT NULL,"
                    + "payment_method VARCHAR(30) NOT NULL,"
                    + "payer_account VARCHAR(100) NOT NULL,"
                    + "reference_code VARCHAR(40) NOT NULL UNIQUE,"
                    + "status VARCHAR(20) NOT NULL DEFAULT 'PENDING',"
                    + "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "approved_at DATETIME DEFAULT NULL,"
                    + "approved_by INT DEFAULT NULL,"
                    + "PRIMARY KEY (id),"
                    + "KEY member_id (member_id),"
                    + "KEY approved_by (approved_by),"
                    + "CONSTRAINT topup_requests_member_fk FOREIGN KEY (member_id) REFERENCES members (id),"
                    + "CONSTRAINT topup_requests_admin_fk FOREIGN KEY (approved_by) REFERENCES members (id)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

            seedData(stmt);
            initialized = true;
            System.out.println("[DBConnection] Database siap digunakan.");
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { System.err.println("[DBConnection] Gagal tutup statement init: " + e.getMessage()); }
            }
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { System.err.println("[DBConnection] Gagal tutup koneksi init: " + e.getMessage()); }
            }
        }
    }

    private static void seedData(Statement stmt) throws SQLException {
        stmt.executeUpdate("INSERT IGNORE INTO roles (id, role_name) VALUES (1, 'Admin'), (2, 'User')");

        stmt.executeUpdate(
                "INSERT IGNORE INTO members (id, username, password_hash, balance, membership_type, role_id) VALUES "
                + "(1, 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 0, 'Admin', 1),"
                + "(2, 'rifky', '3c8fe512459a840fbf6f4f69705b09cc0a35396c601a4de2d6b3098102fe816b', 50000, 'Regular', 2),"
                + "(3, 'budi', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5', 75000, 'Premium', 2)");

        stmt.executeUpdate(
                "INSERT IGNORE INTO vehicles (vehicle_id, model, battery_level, is_available, vehicle_type) VALUES "
                + "('BIKE-001', 'Polygon E-Bike Pro', 95, 1, 'BIKE'),"
                + "('BIKE-002', 'Exotic E-Folding', 80, 1, 'BIKE'),"
                + "('SCOOT-001', 'Xiaomi Mi Scooter 3', 100, 1, 'SCOOTER'),"
                + "('SCOOT-002', 'Segway Ninebot E45', 70, 1, 'SCOOTER')");

        stmt.executeUpdate(
                "INSERT IGNORE INTO electric_bikes (vehicle_id, has_pedals, price_per_minute) VALUES "
                + "('BIKE-001', 1, 500),"
                + "('BIKE-002', 1, 450)");

        stmt.executeUpdate(
                "INSERT IGNORE INTO electric_scooters (vehicle_id, max_speed, price_per_minute) VALUES "
                + "('SCOOT-001', 25, 750),"
                + "('SCOOT-002', 30, 800)");
    }

    private DBConnection() {
        throw new UnsupportedOperationException("DBConnection adalah utility class.");
    }
}