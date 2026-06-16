<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>EcoRide - Konfirmasi Sewa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-dark bg-success shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/vehicles">
            <i class="bi bi-ev-station"></i> EcoRide
        </a>
        <div class="d-flex align-items-center gap-2">
            <span class="text-white small">
                Saldo: <strong>Rp <fmt:formatNumber value="${member.balance}" type="number" maxFractionDigits="0"/></strong>
            </span>
            <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-outline-light btn-sm">
                <i class="bi bi-arrow-left"></i> Kembali
            </a>
        </div>
    </div>
</nav>

<div class="container mt-5" style="max-width: 620px;">
    <div class="card shadow">
        <div class="card-header bg-success text-white text-center py-3">
            <h4 class="mb-0"><i class="bi bi-bicycle"></i> Formulir Konfirmasi Sewa</h4>
        </div>
        <div class="card-body p-4">

            <!-- Error -->
            <c:if test="${not empty errorMsg}">
                <div class="alert alert-danger">
                    <i class="bi bi-exclamation-circle"></i> ${errorMsg}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/rent" method="POST">

                <!-- vehicleId dikirim sebagai hidden field -->
                <input type="hidden" name="vehicleId" value="${chosenVehicle.vehicleId}" />

                <div class="mb-3">
                    <label class="form-label fw-bold">ID Kendaraan</label>
                    <input type="text" class="form-control bg-light"
                           value="${chosenVehicle.vehicleId}" disabled />
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Model Kendaraan</label>
                    <input type="text" class="form-control bg-light"
                           value="${chosenVehicle.model}" disabled />
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Tipe</label>
                    <input type="text" class="form-control bg-light"
                           value="${chosenVehicle.vehicleType}" disabled />
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Tarif Sewa</label>
                    <div class="input-group">
                        <span class="input-group-text">Rp</span>
                        <fmt:formatNumber var="formattedPrice" value="${chosenVehicle.pricePerMinute}" maxFractionDigits="0"/>
                        <input type="text" class="form-control bg-light"
                               value="${formattedPrice} / menit"
                               disabled />
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Saldo Kamu</label>
                    <div class="input-group">
                        <span class="input-group-text">Rp</span>
                        <fmt:formatNumber var="formattedBalance" value="${member.balance}" type="number" maxFractionDigits="0"/>
                        <input type="text" class="form-control bg-light"
                               value="${formattedBalance}"
                               disabled />
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Diskon Membership</label>
                    <fmt:formatNumber var="formattedDiscount" value="${member.discount * 100}" maxFractionDigits="0"/>
                    <input type="text" class="form-control bg-light"
                           value="${formattedDiscount}% (${member.membershipType})"
                           disabled />
                </div>

                <div class="mb-4">
                    <label for="duration" class="form-label fw-bold">
                        <i class="bi bi-clock"></i> Durasi Sewa (Jam)
                    </label>
                    <select class="form-select" id="duration" name="duration" required>
                        <option value="1">1 Jam</option>
                        <option value="2">2 Jam</option>
                        <option value="3">3 Jam</option>
                        <option value="5">5 Jam</option>
                        <option value="12">12 Jam</option>
                    </select>
                </div>

                <hr>

                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-success btn-lg">
                        <i class="bi bi-check-circle"></i> Konfirmasi Sewa
                    </button>
                    <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-outline-secondary">
                        <i class="bi bi-arrow-left"></i> Kembali ke Kendaraan
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>