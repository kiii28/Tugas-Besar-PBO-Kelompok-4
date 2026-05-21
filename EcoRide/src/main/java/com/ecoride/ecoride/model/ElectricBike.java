/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecoride.ecoride.model;

/**
 *
 * @author rifky
 */
public class ElectricBike extends Vehicle {
    private boolean hasPedals;

    public ElectricBike(String vehicleID, String model, double batteryLevel, boolean isAvailable, boolean hasPedals) {
        super(vehicleID, model, batteryLevel, isAvailable);
        this.hasPedals = hasPedals;
    }

    @Override
    public double calculateRent(int minutes) {
        return minutes * 0.5; // contoh harga
    }

    public boolean hasPedals() {
        return hasPedals;
    }
}
