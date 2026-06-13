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
 * Role — Helper Class
 *
 * Menyimpan jenis role akun: "Admin" atau "User".
 * Dipakai oleh Member untuk menentukan hak akses.
 * SessionUtil.isAdmin() mengecek role ini sebelum
 * membuka halaman admin.
 */


/**
 * Role — Helper Class
 *
 * Merepresentasikan hak akses member.
 * Digunakan oleh Member untuk menentukan apakah
 * member tersebut adalah Admin atau User biasa.
 *
 * Nilai role_name di database: "Admin" atau "User"
 */
public class Role {

    // =========================================================
    // Konstanta nama role — harus sama persis dengan DB
    // =========================================================
    public static final String ADMIN = "Admin";
    public static final String USER  = "User";

    // =========================================================
    // Atribut (Private)
    // =========================================================
    private String roleName;

    // =========================================================
    // Constructor
    // =========================================================
    public Role() {}

    public Role(String roleName) {
        this.roleName = roleName;
    }

    // =========================================================
    // Method (Public)
    // =========================================================

    /**
     * Cek apakah role ini adalah Admin.
     * Pakai equalsIgnoreCase supaya aman dari perbedaan
     * huruf besar/kecil di database.
     */
    public boolean isAdmin() {
        return ADMIN.equalsIgnoreCase(roleName);
    }

    /**
     * Cek apakah role ini adalah User biasa.
     */
    public boolean isUser() {
        return USER.equalsIgnoreCase(roleName);
    }

    // =========================================================
    // Getter & Setter
    // =========================================================
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{roleName='" + roleName + "'}";
    }
}
