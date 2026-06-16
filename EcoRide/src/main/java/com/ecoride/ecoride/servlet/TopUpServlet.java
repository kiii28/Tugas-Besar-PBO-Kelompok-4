/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ecoride.ecoride.servlet;

import com.ecoride.ecoride.dao.TopUpDAO;
import com.ecoride.ecoride.model.Member;
import com.ecoride.ecoride.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TopUpServlet
 * URL: /topup
 *
 * GET  â†’ tampilkan topup.jsp
 * POST â†’ proses top-up saldo, redirect ke dashboard
 */

public class TopUpServlet extends HttpServlet {

    private final TopUpDAO topUpDAO = new TopUpDAO();

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
        String paymentMethod = request.getParameter("paymentMethod");
        String payerAccount = request.getParameter("payerAccount");
        double amount;

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException | NullPointerException e) {
            forwardTopUpError(request, response, member, "Jumlah top-up tidak valid.", paymentMethod, payerAccount);
            return;
        }

        if (amount < 10000) {
            forwardTopUpError(request, response, member, "Minimal top-up adalah Rp10.000.", paymentMethod, payerAccount);
            return;
        }
        if (amount > 1000000) {
            forwardTopUpError(request, response, member, "Maksimal top-up adalah Rp1.000.000 sekali transaksi.", paymentMethod, payerAccount);
            return;
        }
        if (!isValidPaymentMethod(paymentMethod)) {
            forwardTopUpError(request, response, member, "Pilih metode pembayaran terlebih dahulu.", paymentMethod, payerAccount);
            return;
        }
        if (payerAccount == null || payerAccount.trim().length() < 4) {
            forwardTopUpError(request, response, member, "Masukkan nomor akun/referensi pembayaran minimal 4 karakter.", paymentMethod, payerAccount);
            return;
        }

        String referenceCode = generateReference(member.getId());
        boolean success = topUpDAO.createRequest(
                member.getId(),
                amount,
                paymentMethod,
                payerAccount.trim(),
                referenceCode);

        if (success) {
            HttpSession session = request.getSession();
            session.setAttribute("topupSuccessAmount", amount);
            session.setAttribute("topupSuccessMethod", getPaymentLabel(paymentMethod));
            session.setAttribute("topupSuccessReference", referenceCode);

            response.sendRedirect(request.getContextPath() + "/dashboard?topup=success");
        } else {
            forwardTopUpError(request, response, member, "Pengajuan top-up gagal dibuat. Coba lagi.", paymentMethod, payerAccount);
        }
    }

    private void forwardTopUpError(HttpServletRequest request, HttpServletResponse response,
                                   Member member, String message, String paymentMethod,
                                   String payerAccount)
            throws ServletException, IOException {
        request.setAttribute("errorMsg", message);
        request.setAttribute("member", member);
        request.setAttribute("selectedAmount", request.getParameter("amount"));
        request.setAttribute("selectedPaymentMethod", paymentMethod);
        request.setAttribute("payerAccount", payerAccount);
        request.getRequestDispatcher("/member/topup.jsp").forward(request, response);
    }

    private boolean isValidPaymentMethod(String paymentMethod) {
        return "BANK_TRANSFER".equals(paymentMethod)
                || "EWALLET".equals(paymentMethod)
                || "CASH_COUNTER".equals(paymentMethod);
    }

    private String getPaymentLabel(String paymentMethod) {
        if ("BANK_TRANSFER".equals(paymentMethod)) return "Transfer Bank";
        if ("EWALLET".equals(paymentMethod)) return "E-Wallet";
        if ("CASH_COUNTER".equals(paymentMethod)) return "Loket EcoRide";
        return "Pembayaran";
    }

    private String generateReference(int memberId) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "ECO-" + memberId + "-" + timestamp;
    }
}

