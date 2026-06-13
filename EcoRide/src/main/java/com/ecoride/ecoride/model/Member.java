/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author rifky
 */
package com.ecoride.ecoride.model;

/**
 * Member — Subclass dari Account
 *
 * Merepresentasikan pengguna yang bisa menyewa kendaraan.
 * Mewarisi: id, username, password, balance dari Account.
 *
 * Tambahan:
 *   - membershipType : "Regular" atau "Premium"
 *   - role           : objek Role (Admin / User)
 *   - topUpBalance() : tambah saldo
 *   - deductBalance(): kurangi saldo
 *   - getDiscount()  : diskon berdasarkan membership
 *   - isAdmin()      : cek apakah admin
 */
public class Member extends Account {

    // =========================================================
    // Atribut tambahan (Private)
    // =========================================================
    private String membershipType;  // "Regular" atau "Premium"
    private Role   role;            // objek Role dari Role.java

    // =========================================================
    // Constructor
    // =========================================================

    /** Constructor kosong — dipakai framework/DAO */
    public Member() {
        super();
    }

    /**
     * Constructor register — user baru.
     * Role default: User, Membership default: Regular.
     */
    public Member(String username, String password) {
        super(0, username, password, 0.0);
        this.membershipType = "Regular";
        this.role           = new Role(Role.USER);
    }

    /**
     * Constructor lengkap — dipakai MemberDAO.mapRowToMember()
     * setelah membaca data dari database.
     */
    public Member(int id, String username, String password,
                  double balance, String membershipType, String roleName) {
        super(id, username, password, balance);
        this.membershipType = membershipType;
        // Bungkus roleName dari DB menjadi objek Role
        this.role = new Role(roleName);
    }

    // =========================================================
    // Implementasi abstract method dari Account
    // =========================================================
    @Override
    public void login() {
        System.out.println("[Member] " + getUsername() + " login.");
    }

    @Override
    public void logout() {
        System.out.println("[Member] " + getUsername() + " logout.");
    }

    @Override
    public void updateProfile() {
        System.out.println("[Member] " + getUsername() + " updateProfile.");
    }

    // =========================================================
    // Method bisnis
    // =========================================================

    /**
     * Tambah saldo member (top-up).
     * Dipanggil TopUpServlet setelah validasi jumlah.
     */
    public void topUpBalance(double amount) {
        if (amount <= 0) {
            System.err.println("[Member] topUpBalance() — amount harus > 0.");
            return;
        }
        setBalance(getBalance() + amount);
        System.out.println("[Member] Top-up berhasil. Saldo baru: " + getBalance());
    }

    /**
     * Diskon berdasarkan membership.
     * Regular → 0.0 (tidak ada diskon)
     * Premium → 0.15 (diskon 15%)
     */
    public double getDiscount() {
        if ("Premium".equalsIgnoreCase(membershipType)) {
            return 0.15;
        }
        return 0.0;
    }

    /**
     * Kurangi saldo — dipakai RentServlet saat transaksi selesai.
     *
     * @return true jika saldo cukup dan berhasil dikurangi
     */
    public boolean deductBalance(double amount) {
        if (getBalance() < amount) {
            System.err.println("[Member] deductBalance() — saldo tidak cukup.");
            return false;
        }
        setBalance(getBalance() - amount);
        System.out.println("[Member] Saldo dipotong " + amount + ". Sisa: " + getBalance());
        return true;
    }

    /**
     * Cek apakah member ini adalah Admin.
     * Null-safe: kalau role null, return false.
     */
    public boolean isAdmin() {
        if (role == null) {
            System.err.println("[Member] isAdmin() — role null untuk user: " + getUsername());
            return false;
        }
        return role.isAdmin();
    }

    // =========================================================
    // Getter & Setter
    // =========================================================
    public String getMembershipType()               { return membershipType; }
    public void setMembershipType(String type)      { this.membershipType = type; }

    public Role getRole()                           { return role; }
    public void setRole(Role role)                  { this.role = role; }

    // =========================================================
    // toString
    // =========================================================
    @Override
    public String toString() {
        return "Member{"
                + "id="          + getId()
                + ", username='" + getUsername() + "'"
                + ", balance="   + getBalance()
                + ", membership='" + membershipType + "'"
                + ", role='"     + (role != null ? role.getRoleName() : "NULL") + "'"
                + ", isAdmin="   + isAdmin()
                + "}";
    }
}
