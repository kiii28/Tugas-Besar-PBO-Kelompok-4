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
 * Mewarisi semua atribut dan method dari Account:
 *   id, username, password, balance, login(), logout(), updateProfile()
 *
 * Tambahan di Member:
 *   - membershipType : jenis keanggotaan (Regular / Premium)
 *   - role           : objek Role untuk hak akses (Admin / User)
 *   - topUpBalance() : menambah saldo
 *   - getDiscount()  : diskon sewa berdasarkan membership
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

    /**
     * Constructor kosong.
     */
    public Member() {
        super();
    }

    /**
     * Constructor dasar — dipakai saat register member baru.
     * Role default adalah "User", membership default "Regular".
     */
    public Member(String username, String password) {
        super(0, username, password, 0.0);
        this.membershipType = "Regular";
        this.role           = new Role(Role.USER);
    }

    /**
     * Constructor lengkap — dipakai MemberDAO saat membaca
     * data dari database.
     *
     * @param id             ID dari tabel members
     * @param username       username member
     * @param password       password hash SHA-256
     * @param balance        saldo member
     * @param membershipType "Regular" atau "Premium"
     * @param roleName       "Admin" atau "User"
     */
    public Member(int id, String username, String password,
                  double balance, String membershipType, String roleName) {
        super(id, username, password, balance);
        this.membershipType = membershipType;
        this.role           = new Role(roleName);
    }

    // =========================================================
    // Method (Public)
    // =========================================================

    /**
     * Menambah saldo member.
     * Dipanggil oleh TopUpServlet setelah menerima jumlah top-up.
     *
     * @param amount jumlah uang yang ditambahkan (harus positif)
     */
    public void topUpBalance(double amount) {
        if (amount <= 0) {
            System.out.println("[Member] Jumlah top-up harus lebih dari 0.");
            return;
        }
        setBalance(getBalance() + amount);
        System.out.println("[Member] Top-up berhasil. Saldo baru: " + getBalance());
    }

    /**
     * Mengembalikan persentase diskon berdasarkan membership.
     * Dipanggil RentServlet saat menghitung total biaya sewa.
     *
     * Regular  → 0.0  (tidak ada diskon)
     * Premium  → 0.15 (diskon 15%)
     *
     * @return nilai desimal diskon (0.0 sampai 1.0)
     */
    public double getDiscount() {
        if ("Premium".equalsIgnoreCase(membershipType)) {
            return 0.15;
        }
        return 0.0;
    }

    /**
     * Mengurangi saldo — dipakai RentServlet saat
     * transaksi sewa selesai dan total biaya dihitung.
     *
     * @param amount jumlah yang dipotong
     * @return true jika saldo cukup, false jika tidak
     */
    public boolean deductBalance(double amount) {
        if (getBalance() < amount) {
            System.out.println("[Member] Saldo tidak cukup.");
            return false;
        }
        setBalance(getBalance() - amount);
        System.out.println("[Member] Saldo dipotong " + amount + ". Sisa: " + getBalance());
        return true;
    }

    /**
     * Mengecek apakah member ini adalah admin.
     * Shortcut dari role.isAdmin().
     */
    public boolean isAdmin() {
        return role != null && role.isAdmin();
    }

    // =========================================================
    // Getter & Setter
    // =========================================================

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // =========================================================
    // toString — override dari Account
    // =========================================================

    @Override
    public String toString() {
        return "Member{id=" + getId()
                + ", username='" + getUsername() + "'"
                + ", balance="   + getBalance()
                + ", membership='" + membershipType + "'"
                + ", role='"     + (role != null ? role.getRoleName() : "null") + "'"
                + "}";
    }
}