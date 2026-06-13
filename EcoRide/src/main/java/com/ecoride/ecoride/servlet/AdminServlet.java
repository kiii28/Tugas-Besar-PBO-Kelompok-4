/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.ecoride.ecoride.servlet;

import com.ecoride.ecoride.dao.MemberDAO;
import com.ecoride.ecoride.dao.TransactionDAO;
import com.ecoride.ecoride.dao.VehicleDAO;
import com.ecoride.ecoride.model.ElectricBike;
import com.ecoride.ecoride.model.ElectricScooter;
import com.ecoride.ecoride.model.Member;
import com.ecoride.ecoride.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * AdminServlet
 * URL: /admin  (didefinisikan di web.xml)
 *
 * GET  → tampilkan halaman admin sesuai parameter ?page=
 *         - (kosong / dashboard) → admin/dashboard.jsp
 *         - member               → admin/manageMember.jsp
 *         - vehicle              → admin/manageVehicle.jsp
 *         - transaction          → admin/transactions.jsp
 *
 * POST → proses aksi admin:
 *         - upgradeMembership
 *         - deleteMember
 *         - addVehicle
 *         - deleteVehicle
 *         - chargeVehicle
 */
public class AdminServlet extends HttpServlet {

    private final MemberDAO      memberDAO      = new MemberDAO();
    private final VehicleDAO     vehicleDAO     = new VehicleDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    // =========================================================
    // GET — tampilkan halaman admin
    // =========================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Guard: hanya admin
        if (SessionUtil.redirectIfNotAdmin(request, response)) return;

        String page = request.getParameter("page");
        if (page == null) page = "dashboard";

        switch (page) {
            case "member":
                showMemberPage(request, response);
                break;
            case "vehicle":
                showVehiclePage(request, response);
                break;
            case "transaction":
                showTransactionPage(request, response);
                break;
            default:
                showDashboard(request, response);
                break;
        }
    }

    // =========================================================
    // POST — proses aksi admin
    // =========================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (SessionUtil.redirectIfNotAdmin(request, response)) return;

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        System.out.println("[AdminServlet] action=" + action);

        if ("upgradeMembership".equals(action)) {
            doUpgradeMembership(request, response);
        } else if ("deleteMember".equals(action)) {
            doDeleteMember(request, response);
        } else if ("addVehicle".equals(action)) {
            doAddVehicle(request, response);
        } else if ("deleteVehicle".equals(action)) {
            doDeleteVehicle(request, response);
        } else if ("chargeVehicle".equals(action)) {
            doChargeVehicle(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin");
        }
    }

    // =========================================================
    // HALAMAN DASHBOARD
    // =========================================================
    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Member> daftarMember = memberDAO.getAllMembers();

        // Hanya ambil user biasa (bukan admin) untuk ditampilkan
        long totalMember = daftarMember.stream()
                .filter(m -> !m.isAdmin()).count();

        long kendaraanTersedia = vehicleDAO.getAllAvailable().size();
        List<Object[]> daftarTransaksi = transactionDAO.getAll();
        double totalPendapatan = transactionDAO.getTotalPendapatan();

        request.setAttribute("totalMember",       totalMember);
        request.setAttribute("kendaraanTersedia", kendaraanTersedia);
        request.setAttribute("totalTransaksi",    daftarTransaksi.size());
        request.setAttribute("totalPendapatan",   totalPendapatan);
        request.setAttribute("daftarMember",      daftarMember);
        request.setAttribute("daftarTransaksi",   daftarTransaksi.size() > 5
                ? daftarTransaksi.subList(0, 5) : daftarTransaksi);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    // =========================================================
    // HALAMAN KELOLA MEMBER
    // =========================================================
    private void showMemberPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("daftarMember", memberDAO.getAllMembers());
        request.getRequestDispatcher("/admin/manageMember.jsp").forward(request, response);
    }

    // =========================================================
    // HALAMAN KELOLA KENDARAAN
    // =========================================================
    private void showVehiclePage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("daftarKendaraan", vehicleDAO.getAll());
        request.getRequestDispatcher("/admin/manageVehicle.jsp").forward(request, response);
    }

    // =========================================================
    // HALAMAN TRANSAKSI
    // =========================================================
    private void showTransactionPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("daftarTransaksi", transactionDAO.getAll());
        request.setAttribute("totalPendapatan", transactionDAO.getTotalPendapatan());
        request.getRequestDispatcher("/admin/transactions.jsp").forward(request, response);
    }

    // =========================================================
    // AKSI: Upgrade / Downgrade Membership
    // =========================================================
    private void doUpgradeMembership(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int memberId        = Integer.parseInt(request.getParameter("memberId"));
            String membership   = request.getParameter("membership");
            boolean ok = memberDAO.updateMembership(memberId, membership);
            if (ok) {
                request.setAttribute("successMsg", "Membership berhasil diubah ke " + membership + ".");
            } else {
                request.setAttribute("errorMsg", "Gagal mengubah membership.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMsg", "ID member tidak valid.");
        }
        showMemberPage(request, response);
    }

    // =========================================================
    // AKSI: Hapus Member
    // =========================================================
    private void doDeleteMember(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int memberId = Integer.parseInt(request.getParameter("memberId"));
            boolean ok   = memberDAO.delete(memberId);
            if (ok) {
                request.setAttribute("successMsg", "Member berhasil dihapus.");
            } else {
                request.setAttribute("errorMsg", "Gagal menghapus member.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMsg", "ID member tidak valid.");
        }
        showMemberPage(request, response);
    }

    // =========================================================
    // AKSI: Tambah Kendaraan
    // =========================================================
    private void doAddVehicle(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String vehicleId  = request.getParameter("vehicleId").trim().toUpperCase();
            String model      = request.getParameter("model").trim();
            String type       = request.getParameter("vehicleType");
            double battery    = Double.parseDouble(request.getParameter("batteryLevel"));
            double price      = Double.parseDouble(request.getParameter("pricePerMinute"));

            boolean ok;
            if ("BIKE".equals(type)) {
                boolean hasPedals = "true".equals(request.getParameter("hasPedals"));
                ElectricBike bike = new ElectricBike(vehicleId, model, battery, true, price, hasPedals);
                ok = vehicleDAO.addVehicle(bike);
            } else {
                int maxSpeed = Integer.parseInt(request.getParameter("maxSpeed"));
                ElectricScooter scooter = new ElectricScooter(vehicleId, model, battery, true, price, maxSpeed);
                ok = vehicleDAO.addVehicle(scooter);
            }

            if (ok) {
                request.setAttribute("successMsg", "Kendaraan " + vehicleId + " berhasil ditambahkan.");
            } else {
                request.setAttribute("errorMsg", "Gagal menambahkan kendaraan. ID mungkin sudah ada.");
            }
        } catch (Exception e) {
            request.setAttribute("errorMsg", "Error: " + e.getMessage());
            System.err.println("[AdminServlet] doAddVehicle() error: " + e.getMessage());
        }
        showVehiclePage(request, response);
    }

    // =========================================================
    // AKSI: Hapus Kendaraan
    // =========================================================
    private void doDeleteVehicle(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String vehicleId = request.getParameter("vehicleId");
        boolean ok = vehicleDAO.delete(vehicleId);
        if (ok) {
            request.setAttribute("successMsg", "Kendaraan " + vehicleId + " berhasil dihapus.");
        } else {
            request.setAttribute("errorMsg", "Gagal menghapus kendaraan.");
        }
        showVehiclePage(request, response);
    }

    // =========================================================
    // AKSI: Charge Baterai Kendaraan ke 100%
    // =========================================================
    private void doChargeVehicle(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String vehicleId = request.getParameter("vehicleId");
        boolean ok = vehicleDAO.updateBattery(vehicleId, 100.0);
        if (ok) {
            request.setAttribute("successMsg", "Kendaraan " + vehicleId + " berhasil di-charge ke 100%.");
        } else {
            request.setAttribute("errorMsg", "Gagal meng-charge kendaraan.");
        }
        showVehiclePage(request, response);
    }
}
