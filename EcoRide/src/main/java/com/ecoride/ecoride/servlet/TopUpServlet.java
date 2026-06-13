/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ecoride.ecoride.servlet;

import com.ecoride.ecoride.dao.MemberDAO;
import com.ecoride.ecoride.model.Member;
import com.ecoride.ecoride.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TopUpServlet
 * URL: /topup
 *
 * GET  → tampilkan topup.jsp
 * POST → proses top-up saldo, redirect ke dashboard
 */

public class TopUpServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (SessionUtil.redirectIfNotLoggedIn(request, response)) return;

        request.setAttribute("member", SessionUtil.getMember(request));
        request.getRequestDispatcher("/member/topup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (SessionUtil.redirectIfNotLoggedIn(request, response)) return;

        request.setCharacterEncoding("UTF-8");
        Member member = SessionUtil.getMember(request);

        String amountStr = request.getParameter("amount");
        double amount;

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Jumlah top-up tidak valid.");
            request.setAttribute("member", member);
            request.getRequestDispatcher("/member/topup.jsp").forward(request, response);
            return;
        }

        // validasi jumlah
        if (amount < 10000) {
            request.setAttribute("error", "Minimal top-up adalah Rp10.000.");
            request.setAttribute("member", member);
            request.getRequestDispatcher("/member/topup.jsp").forward(request, response);
            return;
        }
        if (amount > 1000000) {
            request.setAttribute("error", "Maksimal top-up adalah Rp1.000.000 sekali transaksi.");
            request.setAttribute("member", member);
            request.getRequestDispatcher("/member/topup.jsp").forward(request, response);
            return;
        }

        boolean success = memberDAO.topUp(member.getId(), amount);

        if (success) {
            // refresh data member di session agar saldo tampil terbaru
            Member updated = memberDAO.findById(member.getId());
            if (updated != null) SessionUtil.updateMember(request, updated);

            response.sendRedirect(request.getContextPath() + "/vehicles?topup=success");
        } else {
            request.setAttribute("error", "Top-up gagal. Coba lagi.");
            request.setAttribute("member", member);
            request.getRequestDispatcher("/member/topup.jsp").forward(request, response);
        }
    }
}
