/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecoride.ecoride.model;

/**
 *
 * @author rifky
 */


/**
 * Account — Abstract Class
 *
 * Kelas induk untuk semua jenis akun di sistem EcoRide.
 * Tidak bisa dibuat objeknya langsung (new Account() akan error).
 * Harus melalui subclass, yaitu Member.
 *
 * Semua atribut private — hanya bisa diakses lewat getter/setter.
 */
public abstract class Account {

    // =========================================================
    // Atribut (Private)
    // =========================================================

    private int    id;
    private String username;
    private String password;   // disimpan sebagai hash SHA-256
    private double balance;

    // =========================================================
    // Constructor
    // =========================================================

    /**
     * Constructor kosong — dipakai saat membuat objek
     * lalu mengisi atribut satu per satu lewat setter.
     */
    public Account() {}

    /**
     * Constructor lengkap — dipakai MemberDAO saat membaca
     * data dari database dan langsung membungkusnya jadi objek.
     */
    public Account(int id, String username, String password, double balance) {
        this.id       = id;
        this.username = username;
        this.password = password;
        this.balance  = balance;
    }

    // =========================================================
    // Method (Public)
    // =========================================================

    /**
     * Login — validasi dilakukan di LoginServlet + MemberDAO,
     * method ini menandai aksi login di sisi objek.
     */
    public void login() {
        System.out.println("[Account] " + username + " berhasil login.");
    }

    /**
     * Logout — session akan dihapus di LoginServlet/SessionUtil,
     * method ini menandai aksi logout di sisi objek.
     */
    public void logout() {
        System.out.println("[Account] " + username + " berhasil logout.");
    }

    /**
     * Update profil — implementasi detail ada di subclass
     * atau dipanggil servlet setelah user mengisi form.
     */
    public void updateProfile(String newUsername) {
        this.username = newUsername;
        System.out.println("[Account] Profil diperbarui: " + newUsername);
    }

    // =========================================================
    // Getter & Setter
    // =========================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // =========================================================
    // toString — berguna saat debugging
    // =========================================================

    @Override
    public String toString() {
        return "Account{id=" + id + ", username='" + username + "', balance=" + balance + "}";
    }
}