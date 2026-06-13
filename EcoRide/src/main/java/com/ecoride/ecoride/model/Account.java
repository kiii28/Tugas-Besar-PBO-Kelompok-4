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
 * Base class untuk semua jenis akun di EcoRide.
 * Menyimpan atribut dasar: id, username, password, balance.
 *
 * Tidak bisa di-instantiate langsung — harus lewat subclass Member.
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
    public Account() {}

    public Account(int id, String username, String password, double balance) {
        this.id       = id;
        this.username = username;
        this.password = password;
        this.balance  = balance;
    }

    // =========================================================
    // Abstract Methods — wajib diimplementasi subclass
    // =========================================================
    public abstract void login();
    public abstract void logout();
    public abstract void updateProfile();

    // =========================================================
    // Getter & Setter
    // =========================================================
    public int getId()                { return id; }
    public void setId(int id)         { this.id = id; }

    public String getUsername()             { return username; }
    public void setUsername(String username){ this.username = username; }

    public String getPassword()             { return password; }
    public void setPassword(String password){ this.password = password; }

    public double getBalance()              { return balance; }
    public void setBalance(double balance)  { this.balance = balance; }
}
