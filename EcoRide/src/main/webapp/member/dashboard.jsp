<%-- 
    Document   : dashboard
    Created on : 21 Mei 2026, 17.12.19
    Author     : rifky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>EcoRide - Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .stat-card { border-left: 4px solid; border-radius: 8px; }
        .stat-green { border-color: #198754; }
        .stat-blue  { border-color: #0d6efd; }
        .stat-gold  { border-color: #ffc107; }
        .navbar-brand { font-weight: 700; font-size: 1.4rem; }
    </style>
</head>
<body class="bg-light">

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm">
    <div class="container">
        <a class="navbar-brand" href="#"><i class="bi bi-ev-station"></i> EcoRide</a>
        <div class="navbar-nav ms-auto align-items-center">
            <span class="nav-link text-white">
                <i class="bi bi-person-circle"></i>
                <strong>${member.username}</strong>
                <span class="badge bg-warning text-dark ms-1">${member.membershipType}</span>
            </span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm ms-2">
                <i class="bi bi-box-arrow-right"></i> Logout
            </a>
        </div>
    </div>
</nav>

<div class="container mt-4">

    <!-- Pesan sukses / error -->
    <c:if test="${not empty successMsg}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm">
            <i class="bi bi-check-circle"></i> ${successMsg}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty errorMsg}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm">
            <i class="bi bi-exclamation-circle"></i> ${errorMsg}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <h4 class="mb-4">Selamat datang, <strong>${member.username}</strong>! 👋</h4>

    <!-- Stat Cards -->
    <div class="row g-3 mb-4">
        <div class="col-md-4">
            <div class="card stat-card stat-green shadow-sm h-100">
                <div class="card-body d-flex align-items-center gap-3">
                    <i class="bi bi-wallet2 text-success" style="font-size:2.2rem"></i>
                    <div>
                        <div class="text-muted small">Saldo Kamu</div>
                        <div class="fw-bold fs-5">
                            Rp <fmt:formatNumber value="${member.balance}" type="number" maxFractionDigits="0"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card stat-card stat-blue shadow-sm h-100">
                <div class="card-body d-flex align-items-center gap-3">
                    <i class="bi bi-star text-primary" style="font-size:2.2rem"></i>
                    <div>
                        <div class="text-muted small">Membership</div>
                        <div class="fw-bold fs-5">${member.membershipType}</div>
                        <div class="text-muted small">
                            Diskon:
                            <fmt:formatNumber value="${member.discount * 100}" maxFractionDigits="0"/>%
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card stat-card stat-gold shadow-sm h-100">
                <div class="card-body d-flex align-items-center gap-3">
                    <i class="bi bi-receipt text-warning" style="font-size:2.2rem"></i>
                    <div>
                        <div class="text-muted small">Total Transaksi</div>
                        <div class="fw-bold fs-5">${totalTransaksi} kali sewa</div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Menu Cepat -->
    <div class="row g-3 mb-4">
        <div class="col-md-3">
            <a href="${pageContext.request.contextPath}/vehicles"
               class="card text-center text-decoration-none shadow-sm h-100 border-0 hover-card">
                <div class="card-body py-4">
                    <i class="bi bi-bicycle text-success" style="font-size:2rem"></i>
                    <div class="fw-bold mt-2">Sewa Kendaraan</div>
                    <div class="text-muted small">Lihat kendaraan tersedia</div>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a href="${pageContext.request.contextPath}/topup"
               class="card text-center text-decoration-none shadow-sm h-100 border-0">
                <div class="card-body py-4">
                    <i class="bi bi-plus-circle text-primary" style="font-size:2rem"></i>
                    <div class="fw-bold mt-2">Top-Up Saldo</div>
                    <div class="text-muted small">Tambah saldo akun</div>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a href="${pageContext.request.contextPath}/transactions"
               class="card text-center text-decoration-none shadow-sm h-100 border-0">
                <div class="card-body py-4">
                    <i class="bi bi-clock-history text-warning" style="font-size:2rem"></i>
                    <div class="fw-bold mt-2">Riwayat Sewa</div>
                    <div class="text-muted small">Lihat transaksi kamu</div>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a href="${pageContext.request.contextPath}/logout"
               class="card text-center text-decoration-none shadow-sm h-100 border-0">
                <div class="card-body py-4">
                    <i class="bi bi-box-arrow-right text-danger" style="font-size:2rem"></i>
                    <div class="fw-bold mt-2">Logout</div>
                    <div class="text-muted small">Keluar dari akun</div>
                </div>
            </a>
        </div>
    </div>

    <!-- Riwayat Terbaru -->
    <div class="card shadow-sm">
        <div class="card-header bg-white fw-bold">
            <i class="bi bi-clock-history"></i> Transaksi Terbaru
        </div>
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${empty daftarRiwayat}">
                    <div class="text-center text-muted py-4">
                        <i class="bi bi-inbox" style="font-size:2rem"></i>
                        <p class="mt-2">Belum ada riwayat transaksi.</p>
                        <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-success btn-sm">
                            Sewa Sekarang
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table table-striped table-hover align-middle mb-0">
                        <thead class="table-dark">
                            <tr>
                                <th>ID Transaksi</th>
                                <th>Kendaraan</th>
                                <th>Total Biaya</th>
                                <th>Tanggal</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="t" items="${daftarRiwayat}">
                                <tr>
                                    <td><code>${t[0]}</code></td>
                                    <td>${t[1]}</td>
                                    <td>Rp <fmt:formatNumber value="${t[3]}" type="number" maxFractionDigits="0"/></td>
                                    <td>${t[4]}</td>
                                    <td><span class="badge bg-success">${t[5]}</span></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
