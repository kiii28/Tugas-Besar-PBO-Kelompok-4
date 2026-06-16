package com.ecoride.ecoride.dao;

import com.ecoride.ecoride.dao.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopUpDAO {

    public boolean createRequest(int memberId, double amount, String paymentMethod,
                                 String payerAccount, String referenceCode) {
        String sql = "INSERT INTO topup_requests "
                   + "(member_id, amount, payment_method, payer_account, reference_code, status) "
                   + "VALUES (?, ?, ?, ?, ?, 'PENDING')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ps.setDouble(2, amount);
            ps.setString(3, paymentMethod);
            ps.setString(4, payerAccount);
            ps.setString(5, referenceCode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TopUpDAO] createRequest() error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> getPendingRequests() {
        String sql = "SELECT tr.id, m.username, tr.amount, tr.payment_method, "
                   + "tr.payer_account, tr.reference_code, tr.status, tr.created_at "
                   + "FROM topup_requests tr "
                   + "JOIN members m ON tr.member_id = m.id "
                   + "WHERE tr.status = 'PENDING' "
                   + "ORDER BY tr.created_at ASC";
        return queryRequests(sql);
    }

    public int countPendingRequests() {
        String sql = "SELECT COUNT(*) FROM topup_requests WHERE status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.err.println("[TopUpDAO] countPendingRequests() error: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public boolean approveRequest(int requestId, int adminId) {
        String selectSql = "SELECT member_id, amount, status FROM topup_requests WHERE id = ? FOR UPDATE";
        String updateBalanceSql = "UPDATE members SET balance = balance + ? WHERE id = ?";
        String updateRequestSql = "UPDATE topup_requests "
                + "SET status = 'APPROVED', approved_at = NOW(), approved_by = ? "
                + "WHERE id = ? AND status = 'PENDING'";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int memberId;
            double amount;
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, requestId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next() || !"PENDING".equals(rs.getString("status"))) {
                        conn.rollback();
                        return false;
                    }
                    memberId = rs.getInt("member_id");
                    amount = rs.getDouble("amount");
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(updateBalanceSql)) {
                ps.setDouble(1, amount);
                ps.setInt(2, memberId);
                if (ps.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(updateRequestSql)) {
                ps.setInt(1, adminId);
                ps.setInt(2, requestId);
                if (ps.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("[TopUpDAO] approveRequest() error: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("[TopUpDAO] gagal tutup koneksi approve: " + e.getMessage());
                }
            }
        }
    }

    public boolean rejectRequest(int requestId, int adminId) {
        String sql = "UPDATE topup_requests "
                   + "SET status = 'REJECTED', approved_at = NOW(), approved_by = ? "
                   + "WHERE id = ? AND status = 'PENDING'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TopUpDAO] rejectRequest() error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private List<Object[]> queryRequests(String sql) {
        List<Object[]> requests = new ArrayList<Object[]>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                requests.add(new Object[] {
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getDouble("amount"),
                    rs.getString("payment_method"),
                    rs.getString("payer_account"),
                    rs.getString("reference_code"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
                });
            }
        } catch (SQLException e) {
            System.err.println("[TopUpDAO] queryRequests() error: " + e.getMessage());
            e.printStackTrace();
        }
        return requests;
    }
}
