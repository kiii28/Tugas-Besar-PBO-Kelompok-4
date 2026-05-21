/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecoride.ecoride.model;

/**
 *
 * @author rifky
 */
public abstract class Vehicle implements Chargable {
    private String vehicleID;
    private String model;
    protected double batteryLevel;
    protected boolean isAvailable;

    public Vehicle(String vehicleID, String model, double batteryLevel, boolean isAvailable) {
        this.vehicleID = vehicleID;
        this.model = model;
        this.batteryLevel = batteryLevel;
        this.isAvailable = isAvailable;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public String getModel() {
        return model;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean status) {
        this.isAvailable = status;
    }

    @Override
    public void recharge() {
        batteryLevel = 100;
        System.out.println("Vehicle recharged to 100%");
    }

    @Override
    public double checkBatteryStatus() {
        return batteryLevel;
    }

    public abstract double calculateRent(int minutes);
}