<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>EcoRide - Top-Up Saldo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .summary-box { background:#f8fafc; border:1px solid #e5e7eb; border-radius:8px; }
        .method-card {
            border:1px solid #dee2e6; border-radius:8px; padding:.85rem;
            cursor:pointer; height:100%; transition:border-color .15s, box-shadow .15s;
        }
        .method-card:hover { border-color:#0d6efd; box-shadow:0 0 0 .15rem rgba(13,110,253,.12); }
        .method-card input { margin-right:.45rem; }
    </style>
</head>
<body class="bg-light">

<nav class="navbar navbar-dark bg-success shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/dashboard">
            <i class="bi bi-ev-station"></i> EcoRide
        </a>
        <div class="d-flex align-items-center gap-2">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-light btn-sm">
                <i class="bi bi-arrow-left"></i> Kembali
            </a>
            <span class="text-white small d-none d-md-inline">
                <i class="bi bi-wallet2"></i>
                Saldo: <strong>Rp <fmt:formatNumber value="${member.balance}" type="number" maxFractionDigits="0"/></strong>
            </span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">
                <i class="bi bi-box-arrow-right"></i> Logout
            </a>
        </div>
    </div>
</nav>

<div class="container my-5" style="max-width: 860px;">
    <div class="row g-4">
        <div class="col-lg-7">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-primary text-white py-3">
                    <h4 class="mb-0"><i class="bi bi-plus-circle"></i> Top-Up Saldo</h4>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty errorMsg}">
                        <div class="alert alert-danger alert-dismissible fade show">
                            <i class="bi bi-exclamation-circle"></i> ${errorMsg}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/topup" method="POST" id="topupForm">
                        <label class="form-label fw-bold">Pilih Nominal</label>
                        <div class="row g-2 mb-3">
                            <div class="col-6 col-md-4"><button type="button" class="btn btn-outline-primary w-100" onclick="setNominal(20000)">Rp 20.000</button></div>
                            <div class="col-6 col-md-4"><button type="button" class="btn btn-outline-primary w-100" onclick="setNominal(50000)">Rp 50.000</button></div>
                            <div class="col-6 col-md-4"><button type="button" class="btn btn-outline-primary w-100" onclick="setNominal(100000)">Rp 100.000</button></div>
                            <div class="col-6 col-md-4"><button type="button" class="btn btn-outline-primary w-100" onclick="setNominal(200000)">Rp 200.000</button></div>
                            <div class="col-6 col-md-4"><button type="button" class="btn btn-outline-primary w-100" onclick="setNominal(500000)">Rp 500.000</button></div>
                            <div class="col-6 col-md-4"><button type="button" class="btn btn-outline-primary w-100" onclick="setNominal(1000000)">Rp 1.000.000</button></div>
                        </div>

                        <div class="mb-4">
                            <label for="amount" class="form-label fw-bold">Nominal Lain</label>
                            <div class="input-group">
                                <span class="input-group-text">Rp</span>
                                <input type="number" id="amount" name="amount"
                                       class="form-control" min="10000" max="1000000" step="1000"
                                       value="${selectedAmount}"
                                       placeholder="Minimal Rp 10.000" required>
                            </div>
                            <div class="form-text">Minimal Rp 10.000 dan maksimal Rp 1.000.000 per transaksi.</div>
                        </div>

                        <label class="form-label fw-bold">Metode Pembayaran</label>
                        <div class="row g-2 mb-4">
                            <div class="col-md-4">
                                <label class="method-card">
                                    <input type="radio" name="paymentMethod" value="BANK_TRANSFER"
                                           ${selectedPaymentMethod == 'BANK_TRANSFER' ? 'checked' : ''} required>
                                    <strong>Transfer Bank</strong>
                                    <div class="text-muted small">VA BCA/Mandiri/BRI</div>
                                </label>
                            </div>
                            <div class="col-md-4">
                                <label class="method-card">
                                    <input type="radio" name="paymentMethod" value="EWALLET"
                                           ${selectedPaymentMethod == 'EWALLET' ? 'checked' : ''}>
                                    <strong>E-Wallet</strong>
                                    <div class="text-muted small">GoPay, OVO, Dana</div>
                                </label>
                            </div>
                            <div class="col-md-4">
                                <label class="method-card">
                                    <input type="radio" name="paymentMethod" value="CASH_COUNTER"
                                           ${selectedPaymentMethod == 'CASH_COUNTER' ? 'checked' : ''}>
                                    <strong>Loket EcoRide</strong>
                                    <div class="text-muted small">Bayar di counter</div>
                                </label>
                            </div>
                        </div>

                        <div class="mb-4">
                            <label for="payerAccount" class="form-label fw-bold">Nomor Akun / Referensi Pembayaran</label>
                            <input type="text" id="payerAccount" name="payerAccount"
                                   class="form-control" value="${payerAccount}"
                                   placeholder="Contoh: 081234567890 atau VA-123456" required>
                            <div class="form-text">Dipakai sebagai bukti pembayaran.</div>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="bi bi-check-circle"></i> order
                            </button>
                            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left"></i> Kembali ke Dashboard
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-lg-5">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <h5 class="fw-bold mb-3"><i class="bi bi-receipt"></i> Ringkasan Pembayaran</h5>
                    <div class="summary-box p-3 mb-3">
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Saldo saat ini</span>
                            <strong>Rp <fmt:formatNumber value="${member.balance}" type="number" maxFractionDigits="0"/></strong>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Nominal top-up</span>
                            <strong id="summaryAmount">Rp 0</strong>
                        </div>
                        <hr>
                        <div class="d-flex justify-content-between">
                            <span class="text-muted">Estimasi saldo baru</span>
                            <strong class="text-success" id="summaryBalance">Rp <fmt:formatNumber value="${member.balance}" type="number" maxFractionDigits="0"/></strong>
                        </div>
                    </div>
                    <div class="alert alert-info mb-0">
                        <i class="bi bi-info-circle"></i>
                        Setelah konfirmasi, sistem membuat pengajuan top-up. Saldo akan masuk setelah admin menyetujui.
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
const currentBalance = Number('${member.balance}');
const amountInput = document.getElementById('amount');

function formatRupiah(value) {
    return new Intl.NumberFormat('id-ID', {
        style: 'currency',
        currency: 'IDR',
        maximumFractionDigits: 0
    }).format(value || 0);
}

function updateSummary() {
    const amount = Number(amountInput.value || 0);
    document.getElementById('summaryAmount').textContent = formatRupiah(amount);
    document.getElementById('summaryBalance').textContent = formatRupiah(currentBalance + amount);
}

function setNominal(val) {
    amountInput.value = val;
    updateSummary();
}

amountInput.addEventListener('input', updateSummary);
updateSummary();
</script>
</body>
</html>