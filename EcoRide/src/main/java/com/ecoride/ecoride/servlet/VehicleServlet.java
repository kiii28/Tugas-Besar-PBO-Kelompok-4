/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ecoride.ecoride.servlet;
 
import com.ecoride.ecoride.dao.VehicleDAO;
import com.ecoride.ecoride.model.Vehicle;
import com.ecoride.ecoride.util.SessionUtil;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Fikri
 */

/**
 * VehicleServlet
 * URL: /vehicles
 *
 * GET → ambil semua kendaraan tersedia, tampilkan di vehicles.jsp
 */

public class VehicleServlet extends HttpServlet {

    private final VehicleDAO vehicleDAO = new VehicleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // cek login dulu
        if (SessionUtil.redirectIfNotLoggedIn(request, response)) return;

        List<Vehicle> vehicles = vehicleDAO.getAllAvailable();

        request.setAttribute("vehicles", vehicles);
        request.setAttribute("daftarKendaraan", vehicles);
        request.setAttribute("member", SessionUtil.getMember(request));
        request.getRequestDispatcher("/member/vehicle.jsp").forward(request, response);
    }
}
