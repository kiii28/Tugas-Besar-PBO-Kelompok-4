/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecoride.ecoride.model;

/**
 *
 * @author rifky
 */
public class ElectricScooter extends Vehicle {
    private int maxSpeed;

    public ElectricScooter(String vehicleID, String model, double batteryLevel, boolean isAvailable, int maxSpeed) {
        super(vehicleID, model, batteryLevel, isAvailable);
        this.maxSpeed = maxSpeed;
    }

    @Override
    public double calculateRent(int minutes) {
        return minutes * 0.8; // lebih mahal dari bike
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }
}
