package com.ecoride.ecoride.servlet;

import com.ecoride.ecoride.dao.TransactionDAO;
import com.ecoride.ecoride.model.Member;
import com.ecoride.ecoride.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TransactionServlet extends HttpServlet {

    private final TransactionDAO transactionDAO = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (SessionUtil.redirectIfNotLoggedIn(request, response)) return;

        Member member = SessionUtil.getMember(request);
        String successMsg = (String) request.getSession().getAttribute("successMessage");
        if (successMsg != null) {
            request.setAttribute("notifikasiSukses", successMsg);
            request.getSession().removeAttribute("successMessage");
        }

        List<Object[]> daftarRiwayat = transactionDAO.getByMember(member.getId());
        request.setAttribute("member", member);
        request.setAttribute("daftarRiwayat", daftarRiwayat);
        request.getRequestDispatcher("/member/history.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
