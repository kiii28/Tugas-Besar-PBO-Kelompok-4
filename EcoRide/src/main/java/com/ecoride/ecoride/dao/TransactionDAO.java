/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecoride.ecoride.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * TransactionDAO
 *
 * Struktur tabel:
 * rental_transactions:
 *   transaction_id (PK), member_id, vehicle_id,
 *   start_time, end_time, total_cost, status
 */
public class TransactionDAO {

    // =========================================================
    // BUAT TRANSAKSI BARU
    // =========================================================
    public boolean create(int memberId, String vehicleId, int durasiJam) {
        String sql = "INSERT INTO rental_transactions "
                   + "(transaction_id, member_id, vehicle_id, start_time, end_time, total_cost, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')";

        String txId       = "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Date now          = new Date();
        Date endTime      = new Date(now.getTime() + (long) durasiJam * 60 * 60 * 1000);

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setString(1, txId);
            ps.setInt(2, memberId);
            ps.setString(3, vehicleId);
            ps.setTimestamp(4, new Timestamp(now.getTime()));
            ps.setTimestamp(5, new Timestamp(endTime.getTime()));
            ps.setDouble(6, 0.0); // total_cost diisi nanti saat complete
            boolean ok = ps.executeUpdate() > 0;
            System.out.println("[TransactionDAO] create() — txId=" + txId + " " + (ok ? "OK" : "GAGAL"));
            return ok;
        } catch (SQLException e) {
            System.err.println("[TransactionDAO] create() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(null, ps, conn);
        }
        return false;
    }

    // =========================================================
    // BUAT TRANSAKSI SELESAI
    // =========================================================
    public boolean createCompleted(int memberId, String vehicleId, int durasiJam, double totalCost) {
        String sql = "INSERT INTO rental_transactions "
                   + "(transaction_id, member_id, vehicle_id, start_time, end_time, total_cost, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, 'COMPLETED')";

        String txId       = "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Date now          = new Date();
        Date endTime      = new Date(now.getTime() + (long) durasiJam * 60 * 60 * 1000);

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setString(1, txId);
            ps.setInt(2, memberId);
            ps.setString(3, vehicleId);
            ps.setTimestamp(4, new Timestamp(now.getTime()));
            ps.setTimestamp(5, new Timestamp(endTime.getTime()));
            ps.setDouble(6, totalCost);
            boolean ok = ps.executeUpdate() > 0;
            System.out.println("[TransactionDAO] createCompleted() â€” txId=" + txId + " " + (ok ? "OK" : "GAGAL"));
            return ok;
        } catch (SQLException e) {
            System.err.println("[TransactionDAO] createCompleted() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(null, ps, conn);
        }
        return false;
    }

    // =========================================================
    // SELESAIKAN TRANSAKSI
    // =========================================================
    public boolean complete(String transactionId, double totalCost) {
        String sql = "UPDATE rental_transactions "
                   + "SET status = 'COMPLETED', total_cost = ?, end_time = NOW() "
                   + "WHERE transaction_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setDouble(1, totalCost);
            ps.setString(2, transactionId);
            boolean ok = ps.executeUpdate() > 0;
            System.out.println("[TransactionDAO] complete() — txId=" + transactionId
                    + " cost=" + totalCost + " " + (ok ? "OK" : "GAGAL"));
            return ok;
        } catch (SQLException e) {
            System.err.println("[TransactionDAO] complete() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(null, ps, conn);
        }
        return false;
    }

    // =========================================================
    // GET TRANSAKSI AKTIF MEMBER
    // =========================================================
    public Object[] getActiveByMember(int memberId) {
        String sql = "SELECT t.transaction_id, t.vehicle_id, t.start_time, t.end_time "
                   + "FROM rental_transactions t "
                   + "WHERE t.member_id = ? AND t.status = 'ACTIVE' "
                   + "ORDER BY t.start_time DESC LIMIT 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setInt(1, memberId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Object[]{
                    rs.getString("transaction_id"),
                    rs.getString("vehicle_id"),
                    rs.getTimestamp("start_time"),
                    rs.getTimestamp("end_time")
                };
            }
        } catch (SQLException e) {
            System.err.println("[TransactionDAO] getActiveByMember() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(rs, ps, conn);
        }
        return null;
    }

    // =========================================================
    // GET RIWAYAT TRANSAKSI MEMBER
    // format tiap row: [txId, vehicleId, durasiJam, totalCost, startTime, status]
    // =========================================================
    public List<Object[]> getByMember(int memberId) {
        List<Object[]> list = new ArrayList<Object[]>();
        String sql = "SELECT t.transaction_id, t.vehicle_id, "
                   + "TIMESTAMPDIFF(HOUR, t.start_time, t.end_time) AS durasi, "
                   + "t.total_cost, t.start_time, t.status "
                   + "FROM rental_transactions t "
                   + "WHERE t.member_id = ? "
                   + "ORDER BY t.start_time DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setInt(1, memberId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("transaction_id"),
                    rs.getString("vehicle_id"),
                    rs.getInt("durasi"),
                    rs.getDouble("total_cost"),
                    rs.getTimestamp("start_time"),
                    rs.getString("status")
                });
            }
            System.out.println("[TransactionDAO] getByMember(" + memberId + ") — " + list.size() + " transaksi");
        } catch (SQLException e) {
            System.err.println("[TransactionDAO] getByMember() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(rs, ps, conn);
        }
        return list;
    }

    // =========================================================
    // GET SEMUA TRANSAKSI (admin)
    // format tiap row: [txId, username, vehicleId, durasiJam, totalCost, startTime, status]
    // =========================================================
    public List<Object[]> getAll() {
        List<Object[]> list = new ArrayList<Object[]>();
        String sql = "SELECT t.transaction_id, m.username, t.vehicle_id, "
                   + "TIMESTAMPDIFF(HOUR, t.start_time, t.end_time) AS durasi, "
                   + "t.total_cost, t.start_time, t.status "
                   + "FROM rental_transactions t "
                   + "JOIN members m ON t.member_id = m.id "
                   + "ORDER BY t.start_time DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            rs   = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("transaction_id"),  // t[0]
                    rs.getString("username"),         // t[1]
                    rs.getString("vehicle_id"),       // t[2]
                    rs.getInt("durasi"),              // t[3]
                    rs.getDouble("total_cost"),       // t[4]
                    rs.getTimestamp("start_time"),    // t[5]
                    rs.getString("status")            // t[6]
                });
            }
            System.out.println("[TransactionDAO] getAll() — " + list.size() + " transaksi");
        } catch (SQLException e) {
            System.err.println("[TransactionDAO] getAll() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(rs, ps, conn);
        }
        return list;
    }

    // =========================================================
    // TOTAL PENDAPATAN
    // =========================================================
    public double getTotalPendapatan() {
        String sql = "SELECT COALESCE(SUM(total_cost), 0) AS total "
                   + "FROM rental_transactions WHERE status = 'COMPLETED'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            rs   = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("[TransactionDAO] getTotalPendapatan() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(rs, ps, conn);
        }
        return 0.0;
    }

    // =========================================================
    // HITUNG TOTAL TRANSAKSI MEMBER
    // =========================================================
    public int countByMember(int memberId) {
        String sql = "SELECT COUNT(*) FROM rental_transactions WHERE member_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setInt(1, memberId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[TransactionDAO] countByMember() error: " + e.getMessage());
        } finally {
            tutup(rs, ps, conn);
        }
        return 0;
    }

    // =========================================================
    // Helper — tutup resource JDBC
    // =========================================================
    private void tutup(ResultSet rs, PreparedStatement ps, Connection conn) {
        if (rs != null)   { try { rs.close();   } catch (SQLException e) { e.printStackTrace(); } }
        if (ps != null)   { try { ps.close();   } catch (SQLException e) { e.printStackTrace(); } }
        if (conn != null) { try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
    }
}
