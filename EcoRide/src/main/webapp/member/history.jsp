<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>EcoRide - Riwayat Sewa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-dark bg-success shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/vehicles">
            <i class="bi bi-ev-station"></i> EcoRide
        </a>
        <div class="d-flex align-items-center gap-3">
            <span class="text-white small">
                <i class="bi bi-wallet2"></i>
                Saldo: <strong>Rp <fmt:formatNumber value="${member.balance}" type="number" maxFractionDigits="0"/></strong>
            </span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">
                <i class="bi bi-box-arrow-right"></i> Logout
            </a>
        </div>
    </div>
</nav>

<div class="container mt-4">

    <!-- Notifikasi sukses setelah transaksi -->
    <c:if test="${not empty notifikasiSukses}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm">
            <i class="bi bi-check-circle"></i> <strong>Transaksi Berhasil!</strong>
            ${notifikasiSukses}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4><i class="bi bi-clock-history"></i> Riwayat Penyewaan</h4>
        <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-success btn-sm">
            <i class="bi bi-plus-circle"></i> Sewa Lagi
        </a>
    </div>

    <div class="card shadow-sm">
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${empty daftarRiwayat}">
                    <div class="text-center text-muted py-5">
                        <i class="bi bi-inbox" style="font-size:3rem"></i>
                        <p class="mt-2">Belum ada riwayat transaksi.</p>
                        <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-success btn-sm">
                            Sewa Sekarang
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-striped table-hover align-middle mb-0">
                            <thead class="table-dark">
                                <tr>
                                    <th>ID Transaksi</th>
                                    <th>ID Kendaraan</th>
                                    <th>Durasi</th>
                                    <th>Total Biaya</th>
                                    <th>Waktu Sewa</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="t" items="${daftarRiwayat}">
                                    <tr>
                                        <td><code><c:out value="${t[0]}"/></code></td>
                                        <td><c:out value="${t[1]}"/></td>
                                        <td><c:out value="${t[2]}"/> jam</td>
                                        <td>
                                            <strong class="text-success">
                                                Rp <fmt:formatNumber value="${t[3]}" type="number" maxFractionDigits="0"/>
                                            </strong>
                                        </td>
                                        <td><c:out value="${t[4]}"/></td>
                                        <td>
                                            <span class="badge
                                                <c:choose>
                                                    <c:when test="${t[5] == 'COMPLETED'}">bg-success</c:when>
                                                    <c:when test="${t[5] == 'ACTIVE'}">bg-warning text-dark</c:when>
                                                    <c:otherwise>bg-secondary</c:otherwise>
                                                </c:choose>">
                                                <c:out value="${t[5]}"/>
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
