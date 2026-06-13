/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecoride.ecoride.util;

/**
 *
 * @author rifky
 */
import com.ecoride.ecoride.model.Member;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * SessionUtil — Utility Class
 *
 * Helper untuk manajemen session.
 * Semua servlet memanggil class ini sebagai "gerbang" —
 * cek login dulu sebelum lanjut proses apapun.
 */
public class SessionUtil {

    private static final String SESSION_KEY = "loggedInMember";

    private SessionUtil() {}

    // =========================================================
    // Simpan & Ambil Member dari Session
    // =========================================================

    /** Simpan objek Member ke session setelah login berhasil. */
    public static void setMember(HttpServletRequest request, Member member) {
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_KEY, member);
        System.out.println("[SessionUtil] setMember() — " + member.toString());
    }

    /** Ambil objek Member dari session. Null jika belum login. */
    public static Member getMember(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (Member) session.getAttribute(SESSION_KEY);
    }

    /** Hapus session (logout). */
    public static void removeSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("[SessionUtil] removeSession() — session dihapus.");
            session.invalidate();
        }
    }

    /** Update Member di session setelah ada perubahan data (top-up, dll). */
    public static void updateMember(HttpServletRequest request, Member member) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(SESSION_KEY, member);
            System.out.println("[SessionUtil] updateMember() — " + member.toString());
        }
    }

    // =========================================================
    // Cek Status Login
    // =========================================================

    /** true jika ada member yang sedang login. */
    public static boolean isLoggedIn(HttpServletRequest request) {
        boolean loggedIn = getMember(request) != null;
        System.out.println("[SessionUtil] isLoggedIn() — " + loggedIn);
        return loggedIn;
    }

    /** true jika member yang login adalah Admin. */
    public static boolean isAdmin(HttpServletRequest request) {
        Member member = getMember(request);
        if (member == null) {
            System.out.println("[SessionUtil] isAdmin() — member null, return false.");
            return false;
        }
        boolean admin = member.isAdmin();
        System.out.println("[SessionUtil] isAdmin() — " + member.getUsername() + " isAdmin=" + admin);
        return admin;
    }

    // =========================================================
    // Guard / Redirect Helper
    // =========================================================

    /**
     * Redirect ke /login jika belum login.
     *
     * Cara pakai di Servlet:
     *   if (SessionUtil.redirectIfNotLoggedIn(request, response)) return;
     *
     * @return true jika redirect dilakukan (hentikan servlet),
     *         false jika sudah login (lanjutkan)
     */
    public static boolean redirectIfNotLoggedIn(HttpServletRequest request,
                                                 HttpServletResponse response)
            throws IOException {
        if (!isLoggedIn(request)) {
            System.out.println("[SessionUtil] redirectIfNotLoggedIn() — redirect ke /login");
            response.sendRedirect(request.getContextPath() + "/login");
            return true;
        }
        return false;
    }

    /**
     * Redirect ke /vehicles jika bukan admin.
     * HANYA dipanggil dari AdminServlet.
     *
     * PENTING: /vehicles adalah halaman member biasa.
     * Tidak ada pengecekan admin di sana, jadi tidak akan loop.
     *
     * Cara pakai di AdminServlet:
     *   if (SessionUtil.redirectIfNotAdmin(request, response)) return;
     *
     * @return true jika redirect dilakukan, false jika memang admin
     */
    public static boolean redirectIfNotAdmin(HttpServletRequest request,
                                              HttpServletResponse response)
            throws IOException {
        // Cek login dulu
        if (!isLoggedIn(request)) {
            System.out.println("[SessionUtil] redirectIfNotAdmin() — belum login, ke /login");
            response.sendRedirect(request.getContextPath() + "/login");
            return true;
        }
        // Cek role admin
        if (!isAdmin(request)) {
            System.out.println("[SessionUtil] redirectIfNotAdmin() — bukan admin, ke /vehicles");
            response.sendRedirect(request.getContextPath() + "/vehicles");
            return true;
        }
        return false;
    }
}
