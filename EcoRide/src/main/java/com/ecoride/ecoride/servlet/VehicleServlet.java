/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ecoride.ecoride.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet; // PENTING: Ditambahkan agar web mengenali url servlet ini
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Import model kendaraan (Menyesuaikan dengan tugas Syahrial di folder model)
import com.ecoride.ecoride.model.ElectricBike;
import com.ecoride.ecoride.model.ElectricScooter;
import com.ecoride.ecoride.model.Vehicle;

/**
 *
 * @author Fikri
 */
@WebServlet("/member/vehicles") // Mengarahkan URL browser ke halaman katalog kendaraan Anda
public class VehicleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. MEMBUAT MOCK DATA (DATA TIRUAN KENDARAAN)
        List<Vehicle> listKendaraanTiruan = new ArrayList<>();
        
        // Membuat data Sepeda Listrik tiruan 1
        ElectricBike bike1 = new ElectricBike();
        bike1.setVehicleID("EB-401");
        bike1.setModel("EcoBike Deluxe X");
        bike1.setBatteryLevel(92.5);
        bike1.setAvailable(true);
        bike1.setHasPedals(true);
        
        // Membuat data Skuter Listrik tiruan 2
        ElectricScooter scooter1 = new ElectricScooter();
        scooter1.setVehicleID("ES-202");
        scooter1.setModel("SpeedScooter Evo");
        scooter1.setBatteryLevel(40.0);
        scooter1.setAvailable(true);
        scooter1.setMaxSpeed(25);
        
        // Masukkan objek tiruan ke dalam list
        listKendaraanTiruan.add(bike1);
        listKendaraanTiruan.add(scooter1);
        
        // 2. MENGIRIM DATA KE JSP
        // Menyimpan list ke dalam request attribute bernama "daftarKendaraan"
        request.setAttribute("daftarKendaraan", listKendaraanTiruan);
        
        // Melempar request agar dibaca oleh file vehicle.jsp di folder member Anda
        request.getRequestDispatcher("/member/vehicle.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Sementara kosong dulu, karena melihat daftar kendaraan menggunakan method GET (doGet)
    }
}
