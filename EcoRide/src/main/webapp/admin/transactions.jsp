<%-- 
    Document   : transactions
    Created on : 21 Mei 2026, 17.16.21
    Author     : rifky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    com.ecoride.ecoride.model.Member adminUser =
        (com.ecoride.ecoride.model.Member) session.getAttribute("loggedInMember");
    if (adminUser == null || !adminUser.isAdmin()) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>EcoRide - Semua Transaksi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style> body { background:#f0f2f5; } </style>
</head>
<body>
<div class="container-fluid p-0">
<div class="row g-0">

    <!-- SIDEBAR -->
    <div class="col-auto p-3"
         style="width:240px;min-height:100vh;background:linear-gradient(180deg,#1a1a2e 0%,#16213e 100%);">
        <div class="text-center mb-4 pt-2">
            <div style="font-size:1.4rem;font-weight:700;color:#fff;">🌿 EcoRide</div>
            <small class="text-secondary">Admin Panel</small>
        </div>
        <hr class="border-secondary">
        <ul class="nav flex-column px-1">
            <li><a class="nav-link" href="${pageContext.request.contextPath}/admin"
                   style="color:#adb5bd;border-radius:8px;margin-bottom:4px;">📊 Dashboard</a></li>
            <li><a class="nav-link" href="${pageContext.request.contextPath}/admin?page=member"
                   style="color:#adb5bd;border-radius:8px;margin-bottom:4px;">👥 Kelola Member</a></li>
            <li><a class="nav-link" href="${pageContext.request.contextPath}/admin?page=vehicle"
                   style="color:#adb5bd;border-radius:8px;margin-bottom:4px;">🛴 Kelola Kendaraan</a></li>
            <li><a class="nav-link" href="${pageContext.request.contextPath}/admin?page=transaction"
                   style="background:rgba(255,255,255,.15);color:#fff;border-radius:8px;margin-bottom:4px;">🧾 Transaksi</a></li>
        </ul>
        <hr class="border-secondary mt-4">
        <div class="px-1">
            <small class="text-secondary d-block mb-2">Login sebagai:</small>
            <span class="text-white fw-bold"><%= adminUser.getUsername() %></span>
            <span class="badge bg-danger ms-1">Admin</span>
            <div class="mt-3">
                <a href="${pageContext.request.contextPath}/logout"
                   class="btn btn-sm btn-outline-light w-100">🚪 Logout</a>
            </div>
        </div>
    </div>

    <!-- MAIN -->
    <div class="col p-4">

        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h4 class="mb-0 fw-bold">🧾 Semua Transaksi</h4>
                <small class="text-muted">Riwayat seluruh penyewaan kendaraan</small>
            </div>
            <!-- Total Pendapatan -->
            <div class="card border-0 shadow-sm px-4 py-2 text-end">
                <div class="text-muted small">Total Pendapatan</div>
                <div class="fw-bold text-success fs-5">
                    Rp <fmt:formatNumber value="${totalPendapatan}" type="number" maxFractionDigits="0"/>
                </div>
            </div>
        </div>

        <div class="card shadow-sm border-0 rounded-3">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-dark">
                            <tr>
                                <th class="ps-3">ID Transaksi</th>
                                <th>Member</th>
                                <th>Kendaraan</th>
                                <th>Durasi</th>
                                <th>Total Biaya</th>
                                <th>Waktu Sewa</th>
                                <th class="pe-3">Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty daftarTransaksi}">
                                    <c:forEach var="t" items="${daftarTransaksi}">
                                        <tr>
                                            <td class="ps-3"><code>${t[0]}</code></td>
                                            <td>
                                                <i class="bi bi-person-circle text-muted me-1"></i>${t[1]}
                                            </td>
                                            <td>${t[2]}</td>
                                            <td>${t[3]} jam</td>
                                            <td class="fw-semibold text-success">
                                                Rp <fmt:formatNumber value="${t[4]}"
                                                    type="number" maxFractionDigits="0"/>
                                            </td>
                                            <td class="text-muted small">${t[5]}</td>
                                            <td class="pe-3">
                                                <c:choose>
                                                    <c:when test="${t[6] == 'COMPLETED'}">
                                                        <span class="badge bg-success">✅ Selesai</span>
                                                    </c:when>
                                                    <c:when test="${t[6] == 'ACTIVE'}">
                                                        <span class="badge bg-warning text-dark">⏳ Aktif</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary">${t[6]}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="7" class="text-center text-muted py-5">
                                            <i class="bi bi-inbox" style="font-size:2rem"></i>
                                            <p class="mt-2">Belum ada data transaksi.</p>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </div>
</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
