<%-- 
    Document   : manageMember
    Created on : 21 Mei 2026, 17.16.09
    Author     : rifky
--%>

<%-- 
    Document   : manageMember
    Created on : 21 Mei 2026, 17.16.09
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
    <title>EcoRide - Kelola Member</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { background:#f0f2f5; }
        .sidebar .nav-link { color:#adb5bd;border-radius:8px;margin-bottom:4px;transition:all .2s; }
        .sidebar .nav-link:hover { background:rgba(255,255,255,.15);color:#fff; }
    </style>
</head>
<body>
<div class="container-fluid p-0">
<div class="row g-0">

    <!-- SIDEBAR -->
    <div class="col-auto sidebar p-3"
         style="width:240px;min-height:100vh;background:linear-gradient(180deg,#1a1a2e 0%,#16213e 100%);">
        <div class="text-center mb-4 pt-2">
            <div style="font-size:1.4rem;font-weight:700;color:#fff;letter-spacing:1px;">🌿 EcoRide</div>
            <small class="text-secondary">Admin Panel</small>
        </div>
        <hr class="border-secondary">
        <ul class="nav flex-column px-1">
            <li><a class="nav-link" href="${pageContext.request.contextPath}/admin"
                   style="color:#adb5bd;border-radius:8px;margin-bottom:4px;">📊 Dashboard</a></li>
            <li><a class="nav-link" href="${pageContext.request.contextPath}/admin?page=member"
                   style="background:rgba(255,255,255,.15);color:#fff;border-radius:8px;margin-bottom:4px;">👥 Kelola Member</a></li>
            <li><a class="nav-link" href="${pageContext.request.contextPath}/admin?page=vehicle"
                   style="color:#adb5bd;border-radius:8px;margin-bottom:4px;">🛴 Kelola Kendaraan</a></li>
            <li><a class="nav-link" href="${pageContext.request.contextPath}/admin?page=transaction"
                   style="color:#adb5bd;border-radius:8px;margin-bottom:4px;">🧾 Transaksi</a></li>
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

    <!-- MAIN CONTENT -->
    <div class="col p-4">

        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h4 class="mb-0 fw-bold">👥 Kelola Member</h4>
                <small class="text-muted">Manajemen akun pengguna EcoRide</small>
            </div>
        </div>

        <!-- Alert -->
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

        <!-- Tabel Member -->
        <div class="card shadow-sm border-0 rounded-3">
            <div class="card-header bg-white py-3">
                <h6 class="mb-0 fw-bold">Daftar Semua Member</h6>
            </div>
            <div class="card-body p-0">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-dark">
                        <tr>
                            <th class="ps-3">ID</th>
                            <th>Username</th>
                            <th>Membership</th>
                            <th>Role</th>
                            <th>Saldo (Rp)</th>
                            <th class="text-center pe-3">Aksi</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty daftarMember}">
                                <c:forEach var="m" items="${daftarMember}">
                                    <tr>
                                        <td class="ps-3 text-muted"><small>${m.id}</small></td>
                                        <td class="fw-semibold">
                                            <i class="bi bi-person-circle text-muted me-1"></i>${m.username}
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${m.membershipType == 'Premium'}">
                                                    <span class="badge" style="background:#6f42c1">⭐ Premium</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-primary">Regular</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${m.admin}">
                                                    <span class="badge bg-danger">Admin</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">User</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <fmt:formatNumber value="${m.balance}" type="number" maxFractionDigits="0"/>
                                        </td>
                                        <td class="text-center pe-3">
                                            <!-- Upgrade Membership -->
                                            <c:if test="${!m.admin}">
                                                <form action="${pageContext.request.contextPath}/admin"
                                                      method="POST" class="d-inline">
                                                    <input type="hidden" name="action" value="upgradeMembership"/>
                                                    <input type="hidden" name="memberId" value="${m.id}"/>
                                                    <input type="hidden" name="membership"
                                                           value="${m.membershipType == 'Regular' ? 'Premium' : 'Regular'}"/>
                                                    <button type="submit" class="btn btn-sm
                                                        ${m.membershipType == 'Regular' ? 'btn-outline-purple' : 'btn-outline-secondary'}"
                                                        style="border-color:${m.membershipType == 'Regular' ? '#6f42c1' : '#6c757d'};
                                                               color:${m.membershipType == 'Regular' ? '#6f42c1' : '#6c757d'};"
                                                        title="${m.membershipType == 'Regular' ? 'Upgrade ke Premium' : 'Downgrade ke Regular'}">
                                                        ${m.membershipType == 'Regular' ? '⬆ Premium' : '⬇ Regular'}
                                                    </button>
                                                </form>
                                                <!-- Hapus Member -->
                                                <form action="${pageContext.request.contextPath}/admin"
                                                      method="POST" class="d-inline"
                                                      onsubmit="return confirm('Hapus member ${m.username}?')">
                                                    <input type="hidden" name="action" value="deleteMember"/>
                                                    <input type="hidden" name="memberId" value="${m.id}"/>
                                                    <button type="submit" class="btn btn-sm btn-outline-danger ms-1">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </form>
                                            </c:if>
                                            <c:if test="${m.admin}">
                                                <span class="text-muted small">—</span>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" class="text-center text-muted py-5">
                                        <i class="bi bi-inbox" style="font-size:2rem"></i>
                                        <p class="mt-2">Belum ada data member.</p>
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
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
