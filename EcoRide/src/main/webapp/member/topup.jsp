<%-- 
    Document   : topup
    Created on : 21 Mei 2026, 17.15.12
    Author     : rifky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>EcoRide - Top-Up Saldo</title>
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

<div class="container mt-5" style="max-width: 540px;">
    <div class="card shadow">
        <div class="card-header bg-primary text-white text-center py-3">
            <h4 class="mb-0"><i class="bi bi-plus-circle"></i> Top-Up Saldo</h4>
        </div>
        <div class="card-body p-4">

            <!-- Pesan sukses -->
            <c:if test="${not empty successMsg}">
                <div class="alert alert-success alert-dismissible fade show">
                    <i class="bi bi-check-circle"></i> ${successMsg}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Pesan error -->
            <c:if test="${not empty errorMsg}">
                <div class="alert alert-danger alert-dismissible fade show">
                    <i class="bi bi-exclamation-circle"></i> ${errorMsg}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Info saldo -->
            <div class="alert alert-info mb-4">
                <i class="bi bi-info-circle"></i>
                Saldo saat ini:
                <strong>Rp <fmt:formatNumber value="${member.balance}" type="number" maxFractionDigits="0"/></strong>
            </div>

            <!-- Pilihan nominal cepat -->
            <p class="fw-bold mb-2">Pilih Nominal:</p>
            <div class="d-flex gap-2 flex-wrap mb-3">
                <button class="btn btn-outline-primary" onclick="setNominal(20000)">Rp 20.000</button>
                <button class="btn btn-outline-primary" onclick="setNominal(50000)">Rp 50.000</button>
                <button class="btn btn-outline-primary" onclick="setNominal(100000)">Rp 100.000</button>
                <button class="btn btn-outline-primary" onclick="setNominal(200000)">Rp 200.000</button>
                <button class="btn btn-outline-primary" onclick="setNominal(500000)">Rp 500.000</button>
            </div>

            <form action="${pageContext.request.contextPath}/topup" method="POST">
                <div class="mb-4">
                    <label for="amount" class="form-label fw-bold">Atau masukkan jumlah sendiri (Rp):</label>
                    <div class="input-group">
                        <span class="input-group-text">Rp</span>
                        <input type="number" id="amount" name="amount"
                               class="form-control" min="10000" step="1000"
                               placeholder="Minimal Rp 10.000" required />
                    </div>
                    <div class="form-text">Minimal top-up Rp 10.000</div>
                </div>

                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary btn-lg">
                        <i class="bi bi-check-circle"></i> Konfirmasi Top-Up
                    </button>
                    <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-outline-secondary">
                        <i class="bi bi-arrow-left"></i> Kembali
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
function setNominal(val) {
    document.getElementById('amount').value = val;
}
</script>
</body>
</html>
