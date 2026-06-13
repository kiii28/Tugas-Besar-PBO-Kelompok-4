<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>EcoRide - Daftar Kendaraan</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">

<!-- Navbar -->
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
            <a href="${pageContext.request.contextPath}/transactions" class="btn btn-outline-light btn-sm">
                <i class="bi bi-clock-history"></i> Riwayat
            </a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">
                <i class="bi bi-box-arrow-right"></i> Logout
            </a>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4><i class="bi bi-bicycle"></i> Katalog Kendaraan Listrik</h4>
        <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-outline-success btn-sm">
            <i class="bi bi-arrow-clockwise"></i> Refresh
        </a>
    </div>

    <!-- Pesan error jika ada -->
    <c:if test="${not empty errorMsg}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="bi bi-exclamation-circle"></i> ${errorMsg}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Tidak ada kendaraan -->
    <c:if test="${empty daftarKendaraan}">
        <div class="text-center text-muted py-5">
            <i class="bi bi-battery-dead" style="font-size:3rem"></i>
            <p class="mt-2">Tidak ada kendaraan yang tersedia saat ini.</p>
        </div>
    </c:if>

    <!-- Grid kendaraan -->
    <div class="row g-3">
        <c:forEach var="v" items="${daftarKendaraan}">
            <div class="col-md-4">
                <div class="card shadow-sm h-100">
                    <div class="card-header text-center fw-bold
                        <c:choose>
                            <c:when test="${v.vehicleType == 'BIKE'}">bg-success text-white</c:when>
                            <c:otherwise>bg-primary text-white</c:otherwise>
                        </c:choose>">
                        <c:choose>
                            <c:when test="${v.vehicleType == 'BIKE'}">
                                🚲 Electric Bike
                            </c:when>
                            <c:otherwise>
                                🛴 Electric Scooter
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${v.model}</h5>
                        <p class="text-muted small mb-1">
                            <i class="bi bi-tag"></i> ID: <code>${v.vehicleId}</code>
                        </p>

                        <!-- Battery bar -->
                        <div class="mb-2">
                            <div class="d-flex justify-content-between small">
                                <span><i class="bi bi-battery-half"></i> Baterai</span>
                                <span class="fw-bold">${v.batteryLevel}%</span>
                            </div>
                            <div class="progress" style="height:8px">
                                <div class="progress-bar
                                    <c:choose>
                                        <c:when test="${v.batteryLevel >= 60}">bg-success</c:when>
                                        <c:when test="${v.batteryLevel >= 30}">bg-warning</c:when>
                                        <c:otherwise>bg-danger</c:otherwise>
                                    </c:choose>"
                                    style="width:${v.batteryLevel}%"></div>
                            </div>
                        </div>

                        <!-- Tarif -->
                        <p class="mb-1 small">
                            <i class="bi bi-cash"></i> Tarif:
                            <strong>Rp <fmt:formatNumber value="${v.pricePerMinute}" maxFractionDigits="0"/>/menit</strong>
                        </p>

                        <!-- Fitur spesifik -->
                        <c:choose>
                            <c:when test="${v.vehicleType == 'BIKE'}">
                                <p class="mb-0 small text-muted">
                                    <i class="bi bi-check-circle text-success"></i>
                                    Pedal: ${v.hasPedals ? 'Ada' : 'Tidak Ada'}
                                </p>
                            </c:when>
                            <c:otherwise>
                                <p class="mb-0 small text-muted">
                                    <i class="bi bi-speedometer2 text-primary"></i>
                                    Kec. Maks: ${v.maxSpeed} km/jam
                                </p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="card-footer text-center">
                        <c:choose>
                            <c:when test="${v.available}">
                                <a href="${pageContext.request.contextPath}/rent?id=${v.vehicleId}"
                                   class="btn btn-success w-100">
                                    <i class="bi bi-play-circle"></i> Sewa Sekarang
                                </a>
                            </c:when>
                            <c:otherwise>
                                <button class="btn btn-secondary w-100" disabled>
                                    <i class="bi bi-x-circle"></i> Sedang Disewa
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
