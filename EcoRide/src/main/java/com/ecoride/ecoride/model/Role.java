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
public class Role {

    // =========================================================
    // Konstanta — agar tidak salah ketik string role
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
     * Mengambil nama role.
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Mengubah nama role.
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * Mengecek apakah string yang diberikan adalah role yang valid.
     * Hanya "Admin" dan "User" yang diterima.
     *
     * Contoh pakai:
     *   Role.validRole("Admin") → true
     *   Role.validRole("SuperUser") → false
     */
    public static boolean validRole(String role) {
        return ADMIN.equals(role) || USER.equals(role);
    }

    /**
     * Mengecek apakah role ini adalah Admin.
     */
    public boolean isAdmin() {
        return ADMIN.equals(this.roleName);
    }

    // =========================================================
    // toString
    // =========================================================

    @Override
    public String toString() {
        return "Role{roleName='" + roleName + "'}";
    }
}