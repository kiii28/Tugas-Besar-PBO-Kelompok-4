/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecoride.ecoride.model;

/**
 *
 * @author rifky
 */
import java.time.LocalDateTime;



import java.time.temporal.ChronoUnit;

/**
 * RentalTransaction — Class
 *
 * Merepresentasikan satu transaksi sewa kendaraan.
 * Satu Member bisa punya banyak RentalTransaction (relasi 1 ke 0..*).
 *
 * Alur transaksi:
 *   1. Member pilih kendaraan → createTransaction() dipanggil RentServlet
 *   2. Kendaraan dipakai → status "active", endTime masih null
 *   3. Member selesai → completeTransaction() dipanggil RentServlet
 *      totalCost dihitung otomatis dari durasi * tarif * diskon member
 */
public class RentalTransaction {

    // =========================================================
    // Atribut (Private)
    // =========================================================

    private int            id;
    private String         transactionID;   // contoh: TRX-20250101-001
    private Member         member;          // siapa yang menyewa
    private Vehicle        vehicle;         // kendaraan yang disewa
    private LocalDateTime  startTime;
    private LocalDateTime  endTime;         // null jika masih aktif
    private double         totalCost;
    private String         status;          // "active", "completed", "cancelled"

    // =========================================================
    // Constructor
    // =========================================================

    public RentalTransaction() {}

    /**
     * Constructor untuk membuat transaksi baru saat mulai sewa.
     * endTime dan totalCost belum diisi (diisi saat selesai).
     */
    public RentalTransaction(String transactionID, Member member, Vehicle vehicle) {
        this.transactionID = transactionID;
        this.member        = member;
        this.vehicle       = vehicle;
        this.startTime     = LocalDateTime.now();
        this.endTime       = null;
        this.totalCost     = 0.0;
        this.status        = "active";
    }

    /**
     * Constructor lengkap — dipakai TransactionDAO saat baca dari DB.
     */
    public RentalTransaction(int id, String transactionID, Member member,
                             Vehicle vehicle, LocalDateTime startTime,
                             LocalDateTime endTime, double totalCost, String status) {
        this.id            = id;
        this.transactionID = transactionID;
        this.member        = member;
        this.vehicle       = vehicle;
        this.startTime     = startTime;
        this.endTime       = endTime;
        this.totalCost     = totalCost;
        this.status        = status;
    }

    // =========================================================
    // Method (Public)
    // =========================================================

    /**
     * Membuat dan mencatat transaksi baru.
     * Dipanggil RentServlet saat member mulai menyewa.
     * Status kendaraan diubah jadi tidak tersedia.
     */
    public void createTransaction() {
        this.startTime = LocalDateTime.now();
        this.status    = "active";
        if (vehicle != null) {
            vehicle.setAvailable(false);
        }
        System.out.println("[Transaction] Transaksi " + transactionID + " dibuat. Mulai: " + startTime);
    }

    /**
     * Menyelesaikan transaksi.
     * Dipanggil RentServlet saat member selesai menyewa.
     *
     * Otomatis menghitung:
     *   1. Durasi sewa dalam menit
     *   2. Total biaya via vehicle.calculateRent()
     *   3. Potongan diskon dari member.getDiscount()
     *   4. Memotong saldo member
     *   5. Mengembalikan kendaraan jadi tersedia
     *
     * @return total biaya setelah diskon
     */
    public double completeTransaction() {
        this.endTime = LocalDateTime.now();
        this.status  = "completed";

        // hitung durasi dalam menit (minimal 1 menit)
        long minutes = ChronoUnit.MINUTES.between(startTime, endTime);
        if (minutes < 1) minutes = 1;

        // hitung biaya dari kendaraan
        double cost = vehicle.calculateRent((int) minutes);

        // terapkan diskon dari membership member
        double discount = member.getDiscount();
        cost = cost - (cost * discount);

        this.totalCost = cost;

        // potong saldo member
        member.deductBalance(cost);

        // kembalikan kendaraan jadi tersedia
        vehicle.setAvailable(true);

        System.out.println("[Transaction] Selesai. Durasi: " + minutes
                + " menit. Total: Rp" + totalCost);
        return totalCost;
    }

    /**
     * Membatalkan transaksi.
     * Kendaraan dikembalikan jadi tersedia tanpa biaya.
     */
    public void cancelTransaction() {
        this.endTime   = LocalDateTime.now();
        this.status    = "cancelled";
        this.totalCost = 0.0;
        if (vehicle != null) {
            vehicle.setAvailable(true);
        }
        System.out.println("[Transaction] Transaksi " + transactionID + " dibatalkan.");
    }

    /**
     * Mengecek apakah transaksi masih aktif.
     */
    public boolean isActive() {
        return "active".equals(this.status);
    }

    /**
     * Menghitung durasi sewa dalam menit (untuk transaksi aktif).
     * Berguna ditampilkan di halaman rent.jsp saat sewa sedang berjalan.
     */
    public long getDurationMinutes() {
        if (startTime == null) return 0;
        LocalDateTime end = (endTime != null) ? endTime : LocalDateTime.now();
        return ChronoUnit.MINUTES.between(startTime, end);
    }

    // =========================================================
    // Getter & Setter
    // =========================================================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTransactionID() { return transactionID; }
    public void setTransactionID(String transactionID) { this.transactionID = transactionID; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // =========================================================
    // toString
    // =========================================================

    @Override
    public String toString() {
        return "RentalTransaction{id='" + transactionID
                + "', member='"  + (member  != null ? member.getUsername()   : "null")
                + "', vehicle='" + (vehicle != null ? vehicle.getVehicleID() : "null")
                + "', status='"  + status
                + "', cost="     + totalCost + "}";
    }
}
