/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecoride.ecoride.model;

/**
 * ElectricScooter — Subclass dari Vehicle
 *
 * Merepresentasikan skuter/motor listrik.
 * Tambahan atribut: maxSpeed (kecepatan maksimal dalam km/h).
 *
 * Override calculateRent():
 *   Tarif = rentPerMinute * minutes
 *   Surcharge 5% jika maxSpeed di atas 25 km/h (skuter premium).
 */
public class ElectricScooter extends Vehicle {

    // =========================================================
    // Atribut tambahan (Private)
    // =========================================================

    private int maxSpeed; // kecepatan maksimal dalam km/h

    // =========================================================
    // Constructor
    // =========================================================

    public ElectricScooter() {
        super();
    }

    /**
     * Constructor lengkap — dipakai VehicleDAO.
     *
     * @param vehicleID     kode unik kendaraan (contoh: SCOOT-001)
     * @param model         nama model kendaraan
     * @param batteryLevel  level baterai saat ini
     * @param isAvailable   apakah sedang tersedia untuk disewa
     * @param rentPerMinute harga sewa per menit
     * @param maxSpeed      kecepatan maksimal dalam km/h
     */
    public ElectricScooter(String vehicleID, String model, double batteryLevel,
                           boolean isAvailable, double rentPerMinute, int maxSpeed) {
        super(vehicleID, model, batteryLevel, isAvailable, rentPerMinute);
        this.maxSpeed = maxSpeed;
    }

    // =========================================================
    // Override calculateRent()
    // =========================================================

    /**
     * Hitung biaya sewa skuter listrik.
     * Surcharge 5% jika maxSpeed lebih dari 25 km/h.
     *
     * Contoh:
     *   Sewa 30 menit, tarif Rp600/menit, maxSpeed 25 → Rp18.000
     *   Sewa 30 menit, tarif Rp600/menit, maxSpeed 30 → Rp18.000 + 5% = Rp18.900
     *
     * @param minutes durasi sewa dalam menit
     * @return total biaya dalam Rupiah
     */
    @Override
    public double calculateRent(int minutes) {
        double total = getRentPerMinute() * minutes;
        if (maxSpeed > 25) {
            total = total * 1.05; // surcharge 5% untuk skuter kecepatan tinggi
        }
        return total;
    }

    // =========================================================
    // Getter & Setter
    // =========================================================

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    // =========================================================
    // toString
    // =========================================================

    @Override
    public String toString() {
        return "ElectricScooter{vehicleID='" + getVehicleID()
                + "', model='"    + getModel()
                + "', battery="   + getBatteryLevel()
                + "%, available=" + isAvailable()
                + ", maxSpeed="   + maxSpeed + "km/h}";
    }
}
