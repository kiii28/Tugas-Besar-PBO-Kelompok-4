/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ecoride.ecoride.model;

/**
 *
 * @author rifky
 */

/**
 * Chargable — Interface
 *
 * Kontrak yang wajib dipenuhi oleh semua kendaraan listrik.
 * Vehicle.java mengimplementasikan interface ini, sehingga
 * ElectricBike dan ElectricScooter otomatis juga wajib
 * punya kedua method ini.
 */
public interface Chargable {

    /**
     * Mengisi ulang baterai kendaraan hingga 100%.
     * Dipanggil admin lewat AdminServlet.
     */
    void recharge();

    /**
     * Mengecek level baterai saat ini.
     *
     * @return level baterai dalam persen (0.0 - 100.0)
     */
    double checkBatteryStatus();
}
