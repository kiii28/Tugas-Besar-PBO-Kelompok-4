package com.ecoride.ecoride.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection — Utility Class
 *
 * Mengelola koneksi ke database MySQL.
 * Dipanggil oleh semua DAO untuk mendapatkan koneksi.
 */
public class DBConnection {

    // =========================================================
    // Konfigurasi — sesuaikan dengan MySQL kamu
    // =========================================================
    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DATABASE = "ecoride_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";  // kosong jika tidak ada password

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?useSSL=false"
            + "&useUnicode=true"
            + "&characterEncoding=UTF-8"
            + "&serverTimezone=Asia/Jakarta";

    // =========================================================
    // Load driver sekali saat class pertama dipakai
    // =========================================================
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[DBConnection] MySQL Driver berhasil dimuat.");
        } catch (ClassNotFoundException e) {
            System.err.println("[DBConnection] GAGAL memuat MySQL Driver!");
            e.printStackTrace();
            throw new RuntimeException("MySQL Driver tidak ditemukan: " + e.getMessage(), e);
        }
    }

    // =========================================================
    // Ambil koneksi baru
    // =========================================================
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("[DBConnection] Koneksi ke MySQL berhasil!");
            return conn;
        } catch (SQLException e) {
            System.err.println("[DBConnection] GAGAL konek ke database!");
            System.err.println("[DBConnection] URL      : " + URL);
            System.err.println("[DBConnection] Username : " + USERNAME);
            System.err.println("[DBConnection] Pesan    : " + e.getMessage());
            System.err.println("[DBConnection] SQLState : " + e.getSQLState());
            System.err.println("[DBConnection] ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
            throw e;
        }
    }

    // =========================================================
    // Test koneksi — dipanggil dari LoginServlet (debug)
    // =========================================================
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

    // =========================================================
    // Constructor private — tidak boleh di-instantiate
    // =========================================================
    private DBConnection() {
        throw new UnsupportedOperationException("DBConnection adalah utility class.");
    }
}
