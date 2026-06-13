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
 * ElectricBike — Subclass dari Vehicle
 *
 * Merepresentasikan sepeda listrik.
 * Tambahan atribut: hasPedals (apakah punya pedal kayuh).
 *
 * Override calculateRent():
 *   Tarif = rentPerMinute * minutes
 *   Diskon 10% jika durasi lebih dari 60 menit.
 */
public class ElectricBike extends Vehicle {

    // =========================================================
    // Atribut tambahan (Private)
    // =========================================================

    private boolean hasPedals;

    // =========================================================
    // Constructor
    // =========================================================

    public ElectricBike() {
        super();
    }

    /**
     * Constructor lengkap — dipakai VehicleDAO.
     *
     * @param vehicleID     kode unik kendaraan (contoh: BIKE-001)
     * @param model         nama model kendaraan
     * @param batteryLevel  level baterai saat ini
     * @param isAvailable   apakah sedang tersedia untuk disewa
     * @param rentPerMinute harga sewa per menit
     * @param hasPedals     apakah sepeda punya pedal kayuh
     */
    public ElectricBike(String vehicleID, String model, double batteryLevel,
                        boolean isAvailable, double rentPerMinute, boolean hasPedals) {
        super(vehicleID, model, batteryLevel, isAvailable, rentPerMinute);
        this.hasPedals = hasPedals;
    }

    // =========================================================
    // Override calculateRent()
    // =========================================================

    /**
     * Hitung biaya sewa sepeda listrik.
     * Diskon 10% otomatis jika sewa lebih dari 60 menit.
     *
     * Contoh:
     *   Sewa 30 menit, tarif Rp300/menit → Rp9.000
     *   Sewa 90 menit, tarif Rp300/menit → Rp27.000 - 10% = Rp24.300
     *
     * @param minutes durasi sewa dalam menit
     * @return total biaya dalam Rupiah
     */
    @Override
    public double calculateRent(int minutes) {
        double total = getRentPerMinute() * minutes;
        if (minutes > 60) {
            total = total * 0.9; // diskon 10% untuk sewa lebih dari 1 jam
        }
        return total;
    }

    // =========================================================
    // Getter & Setter
    // =========================================================

    public boolean isHasPedals() {
        return hasPedals;
    }

    public void setHasPedals(boolean hasPedals) {
        this.hasPedals = hasPedals;
    }

    // =========================================================
    // toString
    // =========================================================

    @Override
    public String toString() {
        return "ElectricBike{vehicleID='" + getVehicleID()
                + "', model='"      + getModel()
                + "', battery="     + getBatteryLevel()
                + "%, available="   + isAvailable()
                + ", hasPedals="    + hasPedals + "}";
    }
}
