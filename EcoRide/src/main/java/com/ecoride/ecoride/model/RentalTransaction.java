/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecoride.ecoride.model;

/**
 *
 * @author rifky
 */
import java.time.LocalDateTime;
import java.time.Duration;

public class RentalTransaction {
    private String transactionID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalCost;

    private Vehicle vehicle;

    public RentalTransaction(String transactionID, Vehicle vehicle) {
        this.transactionID = transactionID;
        this.vehicle = vehicle;
    }

    public void createTransaction() {
        startTime = LocalDateTime.now();
        vehicle.setAvailable(false);
        System.out.println("Transaction started at " + startTime);
    }

    public void completeTransaction() {
        endTime = LocalDateTime.now();
        long minutes = Duration.between(startTime, endTime).toMinutes();
        totalCost = vehicle.calculateRent((int) minutes);

        vehicle.setAvailable(true);

        System.out.println("Transaction completed at " + endTime);
        System.out.println("Total minutes: " + minutes);
        System.out.println("Total cost: " + totalCost);
    }

    public double getTotalCost() {
        return totalCost;
    }
}