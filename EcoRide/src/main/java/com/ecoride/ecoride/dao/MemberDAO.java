/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author rifky
 */
package com.ecoride.ecoride.dao;

import com.ecoride.ecoride.model.Member;
import com.ecoride.ecoride.model.Role;
import com.ecoride.ecoride.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    private String lastError;

    public String getLastError() {
        return lastError;
    }

    // =========================================================
    // LOGIN
    // =========================================================
    public Member login(String username, String password) {
        String sql = "SELECT m.id, m.username, m.password_hash, m.balance, "
                   + "m.membership_type, r.role_name "
                   + "FROM members m "
                   + "JOIN roles r ON m.role_id = r.id "
                   + "WHERE m.username = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                if (PasswordUtil.verify(password, storedHash)) {
                    Member member = mapRowToMember(rs);
                    System.out.println("[MemberDAO] login() berhasil: " + member.toString());
                    return member;
                } else {
                    System.err.println("[MemberDAO] login() — password salah untuk: " + username);
                }
            } else {
                System.err.println("[MemberDAO] login() — username tidak ditemukan: " + username);
            }
        } catch (SQLException e) {
            System.err.println("[MemberDAO] login() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(rs, ps, conn);
        }
        return null;
    }

    // =========================================================
    // REGISTER
    // =========================================================
    public boolean register(Member member) {
        lastError = null;
        String sql = "INSERT INTO members (username, password_hash, balance, membership_type, role_id) "
                   + "VALUES (?, ?, 0.0, 'Regular', 2)";

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            String hashed = PasswordUtil.hash(member.getPassword());
            if (hashed == null) {
                lastError = "Password tidak valid.";
                System.err.println("[MemberDAO] register() — gagal hash password.");
                return false;
            }
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setString(1, member.getUsername());
            ps.setString(2, hashed);
            boolean ok = ps.executeUpdate() > 0;
            System.out.println("[MemberDAO] register() — " + (ok ? "berhasil" : "gagal")
                    + " untuk: " + member.getUsername());
            return ok;
        } catch (SQLException e) {
            lastError = buildUserFriendlyError(e);
            System.err.println("[MemberDAO] register() error: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            tutup(null, ps, conn);
        }
    }

    private String buildUserFriendlyError(SQLException e) {
        String message = e.getMessage() != null ? e.getMessage() : "";
        String lowerMessage = message.toLowerCase();
        String sqlState = e.getSQLState() != null ? e.getSQLState() : "";

        if (sqlState.startsWith("08") || lowerMessage.contains("communications link failure")
                || lowerMessage.contains("connection refused")) {
            return "Database MySQL belum berjalan. Jalankan MySQL/XAMPP dulu, lalu coba daftar lagi.";
        }
        if (lowerMessage.contains("access denied")) {
            return "Username/password database salah. Cek konfigurasi DBConnection.java.";
        }
        if (lowerMessage.contains("duplicate")) {
            return "Username sudah dipakai, coba yang lain.";
        }
        if (lowerMessage.contains("unknown database")) {
            return "Database belum tersedia dan gagal dibuat otomatis. Pastikan MySQL berjalan dengan user root.";
        }
        return "Registrasi gagal karena masalah database: " + message;
    }

    // =========================================================
    // CEK USERNAME
    // =========================================================
    public boolean isUsernameTaken(String username) {
        String sql = "SELECT id FROM members WHERE username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("[MemberDAO] isUsernameTaken() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(rs, ps, conn);
        }
        return false;
    }

    // =========================================================
    // TOP UP
    // =========================================================
    public boolean topUp(int memberId, double amount) {
        String sql = "UPDATE members SET balance = balance + ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setDouble(1, amount);
            ps.setInt(2, memberId);
            boolean ok = ps.executeUpdate() > 0;
            System.out.println("[MemberDAO] topUp() id=" + memberId
                    + " amount=" + amount + " — " + (ok ? "OK" : "GAGAL"));
            return ok;
        } catch (SQLException e) {
            System.err.println("[MemberDAO] topUp() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(null, ps, conn);
        }
        return false;
    }

    // =========================================================
    // DEDUCT BALANCE
    // =========================================================
    public boolean deductBalance(int memberId, double amount) {
        String sql = "UPDATE members SET balance = balance - ? WHERE id = ? AND balance >= ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setDouble(1, amount);
            ps.setInt(2, memberId);
            ps.setDouble(3, amount);
            boolean ok = ps.executeUpdate() > 0;
            System.out.println("[MemberDAO] deductBalance() id=" + memberId
                    + " amount=" + amount + " — " + (ok ? "OK" : "Saldo tidak cukup/GAGAL"));
            return ok;
        } catch (SQLException e) {
            System.err.println("[MemberDAO] deductBalance() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(null, ps, conn);
        }
        return false;
    }

    // =========================================================
    // FIND BY ID
    // =========================================================
    public Member findById(int id) {
        String sql = "SELECT m.id, m.username, m.password_hash, m.balance, "
                   + "m.membership_type, r.role_name "
                   + "FROM members m "
                   + "JOIN roles r ON m.role_id = r.id "
                   + "WHERE m.id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToMember(rs);
            }
        } catch (SQLException e) {
            System.err.println("[MemberDAO] findById() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(rs, ps, conn);
        }
        return null;
    }

    // =========================================================
    // GET ALL MEMBERS
    // =========================================================
    public List<Member> getAllMembers() {
        List<Member> list = new ArrayList<Member>();
        String sql = "SELECT m.id, m.username, m.password_hash, m.balance, "
                   + "m.membership_type, r.role_name "
                   + "FROM members m "
                   + "JOIN roles r ON m.role_id = r.id "
                   + "ORDER BY m.id";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            rs   = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToMember(rs));
            }
            System.out.println("[MemberDAO] getAllMembers() — ditemukan: " + list.size());
        } catch (SQLException e) {
            System.err.println("[MemberDAO] getAllMembers() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(rs, ps, conn);
        }
        return list;
    }

    // =========================================================
    // UPDATE MEMBERSHIP — dipanggil AdminServlet.doUpgradeMembership()
    // =========================================================
    public boolean updateMembership(int memberId, String membershipType) {
        String sql = "UPDATE members SET membership_type = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setString(1, membershipType);
            ps.setInt(2, memberId);
            boolean ok = ps.executeUpdate() > 0;
            System.out.println("[MemberDAO] updateMembership() id=" + memberId
                    + " type=" + membershipType + " — " + (ok ? "OK" : "GAGAL"));
            return ok;
        } catch (SQLException e) {
            System.err.println("[MemberDAO] updateMembership() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(null, ps, conn);
        }
        return false;
    }

    // =========================================================
    // DELETE MEMBER — dipanggil AdminServlet.doDeleteMember()
    // Catatan: karena rental_transactions punya FK ke members,
    // pastikan di SQL ada ON DELETE CASCADE atau hapus transaksi dulu.
    // =========================================================
    public boolean delete(int memberId) {
        // Hapus transaksi member dulu (hindari FK constraint error)
        String sqlTopUp = "DELETE FROM topup_requests WHERE member_id = ?";
        String sqlTx  = "DELETE FROM rental_transactions WHERE member_id = ?";
        String sqlMem = "DELETE FROM members WHERE id = ? AND role_id != 1";
        // role_id != 1 → tidak bisa hapus Admin

        Connection conn = null;
        PreparedStatement ps0 = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            ps0 = conn.prepareStatement(sqlTopUp);
            ps0.setInt(1, memberId);
            ps0.executeUpdate();

            // Hapus transaksi dulu
            ps1 = conn.prepareStatement(sqlTx);
            ps1.setInt(1, memberId);
            ps1.executeUpdate();

            // Baru hapus member
            ps2 = conn.prepareStatement(sqlMem);
            ps2.setInt(1, memberId);
            int rows = ps2.executeUpdate();

            if (rows > 0) {
                conn.commit();
                System.out.println("[MemberDAO] delete() id=" + memberId + " — OK");
                return true;
            } else {
                conn.rollback();
                System.err.println("[MemberDAO] delete() id=" + memberId
                        + " — GAGAL (tidak ditemukan atau mencoba hapus Admin)");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("[MemberDAO] delete() error: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            tutup(null, ps2, null);
            tutup(null, ps1, null);
            tutup(null, ps0, conn);
        }
        return false;
    }

    // =========================================================
    // Helper — Map ResultSet ke objek Member
    // =========================================================
    private Member mapRowToMember(ResultSet rs) throws SQLException {
        int    id             = rs.getInt("id");
        String username       = rs.getString("username");
        String passwordHash   = rs.getString("password_hash");
        double balance        = rs.getDouble("balance");
        String membershipType = rs.getString("membership_type");
        String roleName       = rs.getString("role_name");

        if (roleName == null || roleName.trim().isEmpty()) {
            System.err.println("[MemberDAO] mapRowToMember() — role_name null untuk id="
                    + id + ", fallback ke 'User'");
            roleName = Role.USER;
        }

        Member member = new Member(id, username, passwordHash, balance, membershipType, roleName);
        System.out.println("[MemberDAO] mapRowToMember() — " + member.toString());
        return member;
    }

    // =========================================================
    // Helper — Tutup resource JDBC secara aman
    // =========================================================
    private void tutup(ResultSet rs, PreparedStatement ps, Connection conn) {
        if (rs != null) {
            try { rs.close(); }
            catch (SQLException e) { System.err.println("[MemberDAO] tutup rs: " + e.getMessage()); }
        }
        if (ps != null) {
            try { ps.close(); }
            catch (SQLException e) { System.err.println("[MemberDAO] tutup ps: " + e.getMessage()); }
        }
        if (conn != null) {
            try { conn.close(); }
            catch (SQLException e) { System.err.println("[MemberDAO] tutup conn: " + e.getMessage()); }
        }
    }
}