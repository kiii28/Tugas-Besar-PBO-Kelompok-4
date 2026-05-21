package com.ecoride.ecoride.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Note: Jika kelompok Anda belum membuat kelas model khusus Transaksi,
 * kita gunakan trik menyimpan data dalam format String/Array Map terstruktur 
 * agar tidak memicu eror import merah di proyek kelompok.
 */
@WebServlet("/member/history")
public class TransactionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. MEMBUAT MOCK DATA RIWAYAT TRANSAKSI (SIMULASI DATA DARI DATABASE)
        List<String[]> riwayatTransaksiTiruan = new ArrayList<>();
        
        // Format array data: { ID Transaksi, ID Kendaraan, Durasi, Total Biaya, Tanggal, Status }
        riwayatTransaksiTiruan.add(new String[]{
            "TRX-2026-001", "EB-401", "2 Jam", "Rp 30.000", "20 Mei 2026", "Selesai"
        });
        riwayatTransaksiTiruan.add(new String[]{
            "TRX-2026-002", "ES-202", "1 Jam", "Rp 15.000", "21 Mei 2026", "Selesai"
        });

        // 2. MENANGKAP PESAN SUKSES DARI PROSES SEWA (JIKA ADA)
        // Ini untuk menangkap trigger pesan sukses setelah menekan tombol di form rent.jsp tadi
        String successMsg = (String) request.getSession().getAttribute("successMessage");
        if (successMsg != null) {
            request.setAttribute("notifikasiSukses", successMsg);
            // Hapus session setelah dibaca agar notifikasi tidak muncul terus-menerus saat di-refresh
            request.getSession().removeAttribute("successMessage");
        }

        // Kirim list riwayat ke halaman history.jsp
        request.setAttribute("daftarRiwayat", riwayatTransaksiTiruan);
        
        request.getRequestDispatcher("/member/history.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}