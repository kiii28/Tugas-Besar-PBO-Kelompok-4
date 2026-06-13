/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecoride.ecoride.util;

/**
 *
 * @author rifky
 */
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PasswordUtil — Utility Class
 *
 * Meng-hash password pakai SHA-256 dan memverifikasi
 * password plain terhadap hash yang tersimpan di database.
 *
 * Cara pakai:
 *   String hash = PasswordUtil.hash("admin123");
 *   boolean ok  = PasswordUtil.verify("admin123", hash);
 */
public class PasswordUtil {

    private PasswordUtil() {
        throw new UnsupportedOperationException("Utility class.");
    }

    // =========================================================
    // Hash password plain menjadi SHA-256 hex string
    // =========================================================
    public static String hash(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            System.err.println("[PasswordUtil] hash() — password tidak boleh null/kosong.");
           
     
    
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(plainPassword.getBytes("UTF-8"));

            // Konversi byte array ke hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("[PasswordUtil] SHA-256 tidak tersedia: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            System.err.println("[PasswordUtil] UTF-8 tidak tersedia: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // =========================================================
    // Verifikasi password plain vs hash di database
    // =========================================================
    public static boolean verify(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            System.err.println("[PasswordUtil] verify() — input null, return false.");
            
            return false;
            
            
        }
        String hashedInput = hash(plainPassword);
        if (hashedInput == null) return false;

        boolean match = storedHash.equals(hashedInput);
        System.out.println("[PasswordUtil] verify() — match: " + match);
        
        return match;
    }

    
}
