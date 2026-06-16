/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ecoride.ecoride.servlet;

import com.ecoride.ecoride.dao.DBConnection;
import com.ecoride.ecoride.dao.MemberDAO;
import com.ecoride.ecoride.model.Member;
import com.ecoride.ecoride.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * LoginServlet
 * URL: /login  (didefinisikan di web.xml)
 *
 * GET  â†’ tampilkan login.jsp
 * POST â†’ proses form login
 *         - Berhasil Admin  â†’ redirect /admin
 *         - Berhasil User   â†’ redirect /vehicles
 *         - Gagal           â†’ kembali ke login.jsp dengan pesan error
 */
public class LoginServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();

    // =========================================================
    // GET â€” tampilkan halaman login
    // =========================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Jika sudah login, langsung ke halaman yang sesuai
        if (SessionUtil.isLoggedIn(request)) {
            System.out.println("[LoginServlet] doGet() â€” sudah login, redirect...");
            if (SessionUtil.isAdmin(request)) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/member/dashboard.jsp");
            }
            return;
        }

        // Tampilkan login.jsp
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // =========================================================
    // POST â€” proses login
    // =========================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // DEBUG â€” hapus setelah aplikasi stabil
        System.out.println("[LoginServlet] doPost() â€” username='" + username + "'");
        System.out.println("[LoginServlet] doPost() â€” DBConnection test: "
                + DBConnection.testConnection());

        // ---- Validasi input tidak boleh kosong ----
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username dan password tidak boleh kosong.");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // ---- Cek ke database ----
        Member member = memberDAO.login(username.trim(), password);

        if (member != null) {
            // Login berhasil â€” simpan ke session
            SessionUtil.setMember(request, member);
            System.out.println("[LoginServlet] Login berhasil: " + member.toString());

            // Arahkan sesuai role
            if (member.isAdmin()) {
                System.out.println("[LoginServlet] Role Admin â†’ redirect /admin");
                response.sendRedirect(request.getContextPath() + "/admin/dashboard.jsp");
            } else {
                System.out.println("[LoginServlet] Role User â†’ redirect /member");
                response.sendRedirect(request.getContextPath() + "/member/dashboard.jsp");
            }

        } else {
            // Login gagal
            System.err.println("[LoginServlet] Login GAGAL untuk username: " + username);
            request.setAttribute("error", "Username atau password salah.");
            request.setAttribute("username", username); // isi ulang field username
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
        
    }
}

