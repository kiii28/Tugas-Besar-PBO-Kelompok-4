package com.ecoride.ecoride.servlet;

import com.ecoride.ecoride.dao.MemberDAO;
import com.ecoride.ecoride.dao.TransactionDAO;
import com.ecoride.ecoride.dao.VehicleDAO;
import com.ecoride.ecoride.model.Member;
import com.ecoride.ecoride.model.Vehicle;
import com.ecoride.ecoride.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RentServlet extends HttpServlet {

    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final MemberDAO memberDAO = new MemberDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (SessionUtil.redirectIfNotLoggedIn(request, response)) return;

        String vehicleId = request.getParameter("id");
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/vehicles");
            return;
        }

        Vehicle selectedVehicle = vehicleDAO.findByVehicleId(vehicleId.trim());
        if (selectedVehicle == null || !selectedVehicle.isAvailable()) {
            request.setAttribute("errorMsg", "Kendaraan tidak ditemukan atau sedang tidak tersedia.");
            request.setAttribute("daftarKendaraan", vehicleDAO.getAllAvailable());
            request.setAttribute("member", SessionUtil.getMember(request));
            request.getRequestDispatcher("/member/vehicle.jsp").forward(request, response);
            return;
        }

        request.setAttribute("chosenVehicle", selectedVehicle);
        request.setAttribute("member", SessionUtil.getMember(request));
        request.setAttribute("rentalPrice", selectedVehicle.getRentPerMinute() * 60);
        request.getRequestDispatcher("/member/rent.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (SessionUtil.redirectIfNotLoggedIn(request, response)) return;

        String vehicleId = request.getParameter("vehicleId");
        String durationStr = request.getParameter("duration");
        Member member = SessionUtil.getMember(request);
        Vehicle vehicle = vehicleDAO.findByVehicleId(vehicleId);

        int durationHours;
        try {
            durationHours = Integer.parseInt(durationStr);
        } catch (NumberFormatException e) {
            showRentError(request, response, vehicle, member, "Durasi sewa tidak valid.");
            return;
        }

        if (vehicle == null || !vehicle.isAvailable()) {
            showRentError(request, response, vehicle, member, "Kendaraan tidak ditemukan atau sedang tidak tersedia.");
            return;
        }

        int durationMinutes = durationHours * 60;
        double totalCost = vehicle.calculateRent(durationMinutes);
        totalCost = totalCost - (totalCost * member.getDiscount());

        if (!memberDAO.deductBalance(member.getId(), totalCost)) {
            showRentError(request, response, vehicle, member, "Saldo tidak cukup untuk melakukan sewa.");
            return;
        }

        boolean saved = transactionDAO.createCompleted(member.getId(), vehicleId, durationHours, totalCost);
        if (!saved) {
            memberDAO.topUp(member.getId(), totalCost);
            showRentError(request, response, vehicle, member, "Transaksi gagal disimpan. Saldo dikembalikan.");
            return;
        }

        Member updated = memberDAO.findById(member.getId());
        if (updated != null) SessionUtil.updateMember(request, updated);

        request.getSession().setAttribute("successMessage",
                "Sewa kendaraan " + vehicleId + " selama " + durationHours + " jam berhasil.");
        response.sendRedirect(request.getContextPath() + "/transactions");
    }

    private void showRentError(HttpServletRequest request, HttpServletResponse response,
                               Vehicle vehicle, Member member, String message)
            throws ServletException, IOException {
        request.setAttribute("errorMsg", message);
        request.setAttribute("chosenVehicle", vehicle);
        request.setAttribute("member", member);
        request.getRequestDispatcher("/member/rent.jsp").forward(request, response);
    }
}
