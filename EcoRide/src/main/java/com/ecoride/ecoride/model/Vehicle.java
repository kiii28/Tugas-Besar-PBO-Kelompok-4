/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecoride.ecoride.model;

/**
 * Vehicle — Abstract Class
 *
 * Kelas induk untuk semua kendaraan listrik di EcoRide.
 * Mengimplementasikan interface Chargable, artinya
 * Vehicle wajib punya recharge() dan checkBatteryStatus().
 *
 * calculateRent() adalah abstract method — setiap subclass
 * (ElectricBike, ElectricScooter) hitung tarif sewanya sendiri.
 */
public abstract class Vehicle implements Chargable {

    // =========================================================
    // Atribut (Private)
    // =========================================================

    private String  vehicleID;
    private String  model;
    private double  batteryLevel;   // 0.0 - 100.0 (persen)
    private boolean isAvailable;
    private double  rentPerMinute;  // harga sewa per menit (Rupiah)

    // =========================================================
    // Constructor
    // =========================================================

    public Vehicle() {}

    /**
     * Constructor lengkap — dipakai VehicleDAO saat baca dari DB.
     */
    public Vehicle(String vehicleID, String model, double batteryLevel,
                   boolean isAvailable, double rentPerMinute) {
        this.vehicleID      = vehicleID;
        this.model          = model;
        this.batteryLevel   = batteryLevel;
        this.isAvailable    = isAvailable;
        this.rentPerMinute  = rentPerMinute;
    }

    // =========================================================
    // Abstract Method — wajib di-override subclass
    // =========================================================

    /**
     * Menghitung total biaya sewa.
     * ElectricBike dan ElectricScooter punya tarif berbeda.
     *
     * @param minutes durasi sewa dalam menit
     * @return total biaya dalam Rupiah
     */
    public abstract double calculateRent(int minutes);

    // =========================================================
    // Implementasi dari interface Chargable
    // =========================================================

    @Override
    public void recharge() {
        this.batteryLevel = 100.0;
        System.out.println("[Vehicle] " + vehicleID + " baterai penuh (100%).");
    }

    @Override
    public double checkBatteryStatus() {
        return this.batteryLevel;
    }

    // =========================================================
    // Getter & Setter
    // =========================================================

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public double getRentPerMinute() {
        return rentPerMinute;
    }

    public void setRentPerMinute(double rentPerMinute) {
        this.rentPerMinute = rentPerMinute;
    }

    // =========================================================
    // toString
    // =========================================================

    @Override
    public String toString() {
        return "Vehicle{vehicleID='" + vehicleID
                + "', model='"       + model
                + "', battery="      + batteryLevel
                + "%, available="    + isAvailable + "}";
    }
}
