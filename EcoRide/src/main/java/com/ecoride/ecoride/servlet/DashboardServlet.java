package com.ecoride.ecoride.servlet;

import com.ecoride.ecoride.dao.MemberDAO;
import com.ecoride.ecoride.dao.TransactionDAO;
import com.ecoride.ecoride.model.Member;
import com.ecoride.ecoride.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class DashboardServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (SessionUtil.redirectIfNotLoggedIn(request, response)) return;

        Member member = SessionUtil.getMember(request);
        if (member.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }

        Member updated = memberDAO.findById(member.getId());
        if (updated != null) {
            member = updated;
            SessionUtil.updateMember(request, member);
        }

        List<Object[]> riwayat = transactionDAO.getByMember(member.getId());
        request.setAttribute("member", member);
        request.setAttribute("totalTransaksi", transactionDAO.countByMember(member.getId()));
        request.setAttribute("daftarRiwayat", riwayat.size() > 5 ? riwayat.subList(0, 5) : riwayat);

        if ("success".equals(request.getParameter("topup"))) {
            HttpSession session = request.getSession(false);
            Object method = session != null ? session.getAttribute("topupSuccessMethod") : null;
            Object reference = session != null ? session.getAttribute("topupSuccessReference") : null;

            if (method != null && reference != null) {
                request.setAttribute("successMsg",
                        "Pengajuan top-up berhasil dikirim. Metode: " + method
                        + ", Referensi: " + reference + ". Saldo akan bertambah setelah disetujui admin.");
            } else {
                request.setAttribute("successMsg", "Pengajuan top-up berhasil dikirim. Menunggu persetujuan admin.");
            }

            if (session != null) {
                session.removeAttribute("topupSuccessAmount");
                session.removeAttribute("topupSuccessMethod");
                session.removeAttribute("topupSuccessReference");
            }
        }

        request.getRequestDispatcher("/member/dashboard.jsp").forward(request, response);
    }
}
