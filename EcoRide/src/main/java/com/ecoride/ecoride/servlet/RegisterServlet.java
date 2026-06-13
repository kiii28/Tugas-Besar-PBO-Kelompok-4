/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ecoride.ecoride.servlet;

import com.ecoride.ecoride.dao.MemberDAO;
import com.ecoride.ecoride.model.Member;
import com.ecoride.ecoride.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * RegisterServlet
 * URL: /register
 *
 * GET  → tampilkan register.jsp
 * POST → proses form registrasi, redirect ke login jika sukses
 */

public class RegisterServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username        = request.getParameter("username");
        String password        = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // validasi input
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "Username tidak boleh kosong.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        if (password == null || password.length() < 6) {
            request.setAttribute("error", "Password minimal 6 karakter.");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Konfirmasi password tidak cocok.");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // cek username sudah ada
        if (memberDAO.isUsernameTaken(username.trim())) {
            request.setAttribute("error", "Username sudah dipakai, coba yang lain.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // buat objek Member baru dengan password yang sudah di-hash
        Member newMember = new Member(username.trim(), PasswordUtil.hash(password));

        boolean success = memberDAO.register(newMember);

        if (success) {
            // redirect ke login dengan pesan sukses
            response.sendRedirect(request.getContextPath() + "/login?registered=true");
        } else {
            request.setAttribute("error", "Registrasi gagal, coba lagi.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
