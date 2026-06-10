<%-- 
    Document   : dashboard
    Created on : 21 Mei 2026, 17.15.30
    Author     : rifky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>EcoRide - Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f0f2f5; }
        .sidebar {
            min-height: 100vh;
            background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
        }
        .sidebar .nav-link {
            color: #adb5bd;
            border-radius: 8px;
            margin-bottom: 4px;
            transition: all 0.2s;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background-color: rgba(255,255,255,0.15);
            color: #fff;
        }
        .sidebar .brand-title {
            font-size: 1.4rem;
            font-weight: 700;
            color: #fff;
            letter-spacing: 1px;
        }
        .stat-card {
            border: none;
            border-radius: 12px;
            transition: transform 0.2s;
        }
        .stat-card:hover { transform: translateY(-4px); }
        .stat-icon {
            width: 56px; height: 56px;
            border-radius: 12px;
            display: flex; align-items: center; justify-content: center;
            font-size: 1.5rem;
        }
        .table-card { border: none; border-radius: 12px; }
        .badge-admin   { background-color: #dc3545; }
        .badge-regular { background-color: #0d6efd; }
        .badge-premium { background-color: #6f42c1; }
    </style>
</head>
<body>

<%
    // Proteksi halaman: hanya admin yang boleh akses
    com.ecoride.ecoride.model.Member adminUser =
        (com.ecoride.ecoride.model.Member) session.getAttribute("loggedInUser");
    if (adminUser == null || !adminUser.isAdmin()) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>

<div class="container-fluid p-0">
    <div class="row g-0">

        <!-- ===== SIDEBAR ===== -->
        <div class="col-auto sidebar p-3" style="width: 240px;">
            <div class="text-center mb-4 pt-2">
                <div class="brand-title">🌿 EcoRide</div>
                <small class="text-secondary">Admin Panel</small>
            </div>
            <hr class="border-secondary">
            <ul class="nav flex-column px-1">
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/dashboard.jsp">
                        📊 Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/manageMember.jsp">
                        👥 Kelola Member
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/manageVehicle.jsp">
                        🛴 Kelola Kendaraan
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/transactions.jsp">
                        🧾 Transaksi
                    </a>
                </li>
            </ul>
            <hr class="border-secondary mt-4">
            <div class="px-1">
                <small class="text-secondary d-block mb-2">Login sebagai:</small>
                <span class="text-white fw-bold"><%= adminUser.getUsername() %></span>
                <span class="badge bg-danger ms-1">Admin</span>
                <div class="mt-3">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline-light w-100">
                        🚪 Logout
                    </a>
                </div>
            </div>
        </div>

        <!-- ===== MAIN CONTENT ===== -->
        <div class="col p-4">

            <!-- Header -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h4 class="mb-0 fw-bold">Dashboard Admin</h4>
                    <small class="text-muted">Selamat datang kembali, <%= adminUser.getUsername() %>!</small>
                </div>
                <span class="text-muted"><small id="currentDate"></small></span>
            </div>

            <!-- Stat Cards -->
            <div class="row g-3 mb-4">

                <!-- Total Member -->
                <div class="col-sm-6 col-xl-3">
                    <div class="card stat-card shadow-sm h-100">
                        <div class="card-body d-flex align-items-center gap-3">
                            <div class="stat-icon bg-primary bg-opacity-10">
                                <span>👥</span>
                            </div>
                            <div>
                                <div class="text-muted small">Total Member</div>
                                <div class="fs-3 fw-bold text-primary">
                                    <c:choose>
                                        <c:when test="${not empty totalMember}">${totalMember}</c:when>
                                        <c:otherwise>—</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Kendaraan Tersedia -->
                <div class="col-sm-6 col-xl-3">
                    <div class="card stat-card shadow-sm h-100">
                        <div class="card-body d-flex align-items-center gap-3">
                            <div class="stat-icon bg-success bg-opacity-10">
                                <span>🛴</span>
                            </div>
                            <div>
                                <div class="text-muted small">Kendaraan Tersedia</div>
                                <div class="fs-3 fw-bold text-success">
                                    <c:choose>
                                        <c:when test="${not empty kendaraanTersedia}">${kendaraanTersedia}</c:when>
                                        <c:otherwise>—</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Total Transaksi -->
                <div class="col-sm-6 col-xl-3">
                    <div class="card stat-card shadow-sm h-100">
                        <div class="card-body d-flex align-items-center gap-3">
                            <div class="stat-icon bg-warning bg-opacity-10">
                                <span>🧾</span>
                            </div>
                            <div>
                                <div class="text-muted small">Total Transaksi</div>
                                <div class="fs-3 fw-bold text-warning">
                                    <c:choose>
                                        <c:when test="${not empty totalTransaksi}">${totalTransaksi}</c:when>
                                        <c:otherwise>—</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Total Pendapatan -->
                <div class="col-sm-6 col-xl-3">
                    <div class="card stat-card shadow-sm h-100">
                        <div class="card-body d-flex align-items-center gap-3">
                            <div class="stat-icon bg-danger bg-opacity-10">
                                <span>💰</span>
                            </div>
                            <div>
                                <div class="text-muted small">Pendapatan (Rp)</div>
                                <div class="fs-3 fw-bold text-danger">
                                    <c:choose>
                                        <c:when test="${not empty totalPendapatan}">${totalPendapatan}</c:when>
                                        <c:otherwise>—</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

            <!-- Tabel Member Terbaru -->
            <div class="card table-card shadow-sm mb-4">
                <div class="card-header bg-white d-flex justify-content-between align-items-center py-3">
                    <h6 class="mb-0 fw-bold">👥 Member Terbaru</h6>
                    <a href="${pageContext.request.contextPath}/admin/manageMember.jsp" class="btn btn-sm btn-outline-primary">
                        Lihat Semua
                    </a>
                </div>
                <div class="card-body p-0">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th class="ps-3">ID</th>
                                <th>Username</th>
                                <th>Membership</th>
                                <th>Role</th>
                                <th class="pe-3">Saldo (Rp)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty daftarMember}">
                                    <c:forEach var="m" items="${daftarMember}">
                                        <tr>
                                            <td class="ps-3 text-muted"><small>${m.id}</small></td>
                                            <td class="fw-semibold">${m.username}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${m.membershipType == 'Premium'}">
                                                        <span class="badge badge-premium">Premium</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-regular">Regular</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${m.admin}">
                                                        <span class="badge badge-admin">Admin</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary">User</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="pe-3">${m.balance}</td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="5" class="text-center text-muted py-4">
                                            Belum ada data member. Data akan muncul setelah MemberDAO diimplementasi.
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Tabel Transaksi Terakhir -->
            <div class="card table-card shadow-sm">
                <div class="card-header bg-white d-flex justify-content-between align-items-center py-3">
                    <h6 class="mb-0 fw-bold">🧾 Transaksi Terakhir</h6>
                    <a href="${pageContext.request.contextPath}/admin/transactions.jsp" class="btn btn-sm btn-outline-warning">
                        Lihat Semua
                    </a>
                </div>
                <div class="card-body p-0">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th class="ps-3">ID Transaksi</th>
                                <th>Member</th>
                                <th>Kendaraan</th>
                                <th>Durasi</th>
                                <th>Total Biaya</th>
                                <th class="pe-3">Tanggal</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty daftarTransaksi}">
                                    <c:forEach var="t" items="${daftarTransaksi}">
                                        <tr>
                                            <td class="ps-3"><strong>${t[0]}</strong></td>
                                            <td>${t[1]}</td>
                                            <td>${t[2]}</td>
                                            <td>${t[3]} jam</td>
                                            <td class="text-success fw-semibold">Rp ${t[4]}</td>
                                            <td class="pe-3 text-muted"><small>${t[5]}</small></td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6" class="text-center text-muted py-4">
                                            Belum ada data transaksi. Data akan muncul setelah TransactionDAO diimplementasi.
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>

        </div><!-- end main content -->
    </div><!-- end row -->
</div><!-- end container -->

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Tampilkan tanggal hari ini
    const today = new Date();
    document.getElementById('currentDate').textContent =
        today.toLocaleDateString('id-ID', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
</script>
</body>
</html>
