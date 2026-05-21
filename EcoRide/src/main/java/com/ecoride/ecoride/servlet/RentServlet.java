/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ecoride.ecoride.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Import model yang dibutuhkan
import com.ecoride.ecoride.model.ElectricBike;
import com.ecoride.ecoride.model.ElectricScooter;
import com.ecoride.ecoride.model.Vehicle;

/**
 *
 * @author Fikri
 */
@WebServlet("/member/rent") // URL mapping untuk menangkap aksi sewa
public class RentServlet extends HttpServlet {

    // 1. MENAMPILKAN FORM KONFIRMASI SEWA
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Menangkap parameter ID kendaraan yang diklik dari halaman katalog
        String vehicleId = request.getParameter("id");
        
        if (vehicleId == null || vehicleId.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/member/vehicles");
            return;
        }

        // MEMBUAT MOCK DATA KENDARAAN YANG DIPILIH
        Vehicle selectedVehicle;
        
        if (vehicleId.startsWith("EB")) {
            ElectricBike bike = new ElectricBike();
            bike.setVehicleID(vehicleId);
            bike.setModel("EcoBike Deluxe X (Selected)");
            bike.setBatteryLevel(92.5);
            bike.setAvailable(true);
            bike.setHasPedals(true);
            selectedVehicle = bike;
        } else {
            ElectricScooter scooter = new ElectricScooter();
            scooter.setVehicleID(vehicleId);
            scooter.setModel("SpeedScooter Evo (Selected)");
            scooter.setBatteryLevel(40.0);
            scooter.setAvailable(true);
            scooter.setMaxSpeed(25);
            selectedVehicle = scooter;
        }

        // Kirim data kendaraan yang dipilih ke halaman rent.jsp
        request.setAttribute("chosenVehicle", selectedVehicle);
        
        // Simulasi harga sewa statis (misal: Rp 15.000 / jam)
        request.setAttribute("rentalPrice", 15000);
        
        request.getRequestDispatcher("/member/rent.jsp").forward(request, response);
    }

    // 2. MEMPROSES SUBMIT FORM (SIMULASI BERHASIL BAYAR)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Menangkap data inputan durasi dan ID dari form rent.jsp
        String vehicleId = request.getParameter("vehicleID");
        String durationStr = request.getParameter("duration");
        
        // Di sini nanti tempat Danish/Syahrial memasukkan query INSERT ke database transaksi.
        // Karena kita pakai Mock Data, kita langsung asumsikan proses sewa BERHASIL.
        
        // Set pesan sukses menggunakan session agar terbaca di halaman berikutnya
        request.getSession().setAttribute("successMessage", "Sewa kendaraan " + vehicleId + " selama " + durationStr + " jam BERHASIL!");
        
        // Alihkan halaman ke Servlet Riwayat Transaksi (TransactionServlet) setelah berhasil
        response.sendRedirect(request.getContextPath() + "/member/history");
    }
}