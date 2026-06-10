<%-- 
    Document   : manageMember
    Created on : 21 Mei 2026, 17.16.09
    Author     : rifky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>EcoRide - Kelola Member</title>
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
        .main-card { border: none; border-radius: 12px; }
        .badge-admin   { background-color: #dc3545; }
        .badge-regular { background-color: #0d6efd; }
        .badge-premium { background-color: #6f42c1; }
        .search-box { max-width: 280px; }
        .action-btn { min-width: 70px; }
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
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard.jsp">
                        📊 Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/manageMember.jsp">
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
                    <h4 class="mb-0 fw-bold">👥 Kelola Member</h4>
                    <small class="text-muted">Daftar seluruh member terdaftar di EcoRide</small>
                </div>
            </div>

            <!-- Notifikasi sukses (misal setelah hapus/edit member) -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible fade show shadow-sm mb-3" role="alert">
                    ✅ <c:out value="${sessionScope.successMessage}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>

            <!-- Notifikasi error -->
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show shadow-sm mb-3" role="alert">
                    ❌ <c:out value="${sessionScope.errorMessage}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="errorMessage" scope="session"/>
            </c:if>

            <!-- Card utama tabel member -->
            <div class="card main-card shadow-sm">

                <!-- Toolbar: search + filter -->
                <div class="card-header bg-white py-3">
                    <div class="d-flex flex-wrap gap-2 align-items-center">
                        <!-- Search -->
                        <div class="input-group search-box">
                            <span class="input-group-text bg-light border-end-0">🔍</span>
                            <input type="text" id="searchInput" class="form-control border-start-0"
                                   placeholder="Cari username..." onkeyup="filterTable()">
                        </div>

                        <!-- Filter Role -->
                        <select id="filterRole" class="form-select" style="max-width:160px;" onchange="filterTable()">
                            <option value="">Semua Role</option>
                            <option value="Admin">Admin</option>
                            <option value="User">User</option>
                        </select>

                        <!-- Filter Membership -->
                        <select id="filterMembership" class="form-select" style="max-width:180px;" onchange="filterTable()">
                            <option value="">Semua Membership</option>
                            <option value="Regular">Regular</option>
                            <option value="Premium">Premium</option>
                        </select>

                        <span class="ms-auto text-muted small" id="rowCount"></span>
                    </div>
                </div>

                <!-- Tabel -->
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0" id="memberTable">
                            <thead class="table-dark">
                                <tr>
                                    <th class="ps-3">ID</th>
                                    <th>Username</th>
                                    <th>Role</th>
                                    <th>Membership</th>
                                    <th>Saldo (Rp)</th>
                                    <th>Diskon</th>
                                    <th class="text-center pe-3">Aksi</th>
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
                                                        <c:when test="${m.admin}">
                                                            <span class="badge badge-admin">Admin</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">User</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${m.membershipType == 'Premium'}">
                                                            <span class="badge badge-premium">⭐ Premium</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-regular">Regular</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <span class="fw-semibold text-success">
                                                        Rp ${m.balance}
                                                    </span>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${m.membershipType == 'Premium'}">
                                                            <span class="text-purple fw-bold">15%</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">0%</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="text-center pe-3">
                                                    <!-- Tombol Upgrade/Downgrade Membership -->
                                                    <form action="${pageContext.request.contextPath}/admin/members" method="POST"
                                                          class="d-inline"
                                                          onsubmit="return confirm('Ubah membership member ini?')">
                                                        <input type="hidden" name="action" value="toggleMembership">
                                                        <input type="hidden" name="memberId" value="${m.id}">
                                                        <c:choose>
                                                            <c:when test="${m.membershipType == 'Regular'}">
                                                                <input type="hidden" name="newMembership" value="Premium">
                                                                <button type="submit" class="btn btn-sm btn-outline-purple action-btn"
                                                                        title="Upgrade ke Premium">⭐ Upgrade</button>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <input type="hidden" name="newMembership" value="Regular">
                                                                <button type="submit" class="btn btn-sm btn-outline-secondary action-btn"
                                                                        title="Downgrade ke Regular">↓ Downgrade</button>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </form>

                                                    <!-- Tombol Hapus Member (hanya untuk non-admin) -->
                                                    <c:if test="${!m.admin}">
                                                        <form action="${pageContext.request.contextPath}/admin/members" method="POST"
                                                              class="d-inline"
                                                              onsubmit="return confirm('Yakin hapus member ${m.username}? Aksi ini tidak bisa dibatalkan!')">
                                                            <input type="hidden" name="action" value="deleteMember">
                                                            <input type="hidden" name="memberId" value="${m.id}">
                                                            <button type="submit" class="btn btn-sm btn-outline-danger action-btn"
                                                                    title="Hapus member">🗑 Hapus</button>
                                                        </form>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Placeholder saat data belum tersedia dari DAO -->
                                        <tr id="emptyRow">
                                            <td colspan="7" class="text-center py-5 text-muted">
                                                <div class="fs-1">👥</div>
                                                <div class="mt-2 fw-semibold">Belum ada data member</div>
                                                <small>Data akan tampil otomatis setelah MemberDAO.getAllMembers() diimplementasi.</small>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Footer info -->
                <div class="card-footer bg-white text-muted">
                    <small>
                        ℹ️ Tabel ini diisi oleh <code>AdminServlet</code> yang memanggil
                        <code>MemberDAO.getAllMembers()</code> dan menyimpannya ke request attribute
                        <code>daftarMember</code>.
                        Aksi <em>Hapus</em> dan <em>Upgrade/Downgrade</em> akan memposting ke
                        <code>/admin/members</code>.
                    </small>
                </div>

            </div><!-- end card -->
        </div><!-- end main content -->
    </div><!-- end row -->
</div><!-- end container -->

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Filter tabel secara client-side (username, role, membership)
    function filterTable() {
        const search     = document.getElementById('searchInput').value.toLowerCase();
        const roleFilter = document.getElementById('filterRole').value.toLowerCase();
        const memFilter  = document.getElementById('filterMembership').value.toLowerCase();

        const rows = document.querySelectorAll('#memberTable tbody tr:not(#emptyRow)');
        let visible = 0;

        rows.forEach(row => {
            const username   = (row.cells[1]?.textContent || '').toLowerCase();
            const role       = (row.cells[2]?.textContent || '').toLowerCase();
            const membership = (row.cells[3]?.textContent || '').toLowerCase();

            const matchSearch = username.includes(search);
            const matchRole   = roleFilter === '' || role.includes(roleFilter);
            const matchMem    = memFilter  === '' || membership.includes(memFilter);

            if (matchSearch && matchRole && matchMem) {
                row.style.display = '';
                visible++;
            } else {
                row.style.display = 'none';
            }
        });

        const countEl = document.getElementById('rowCount');
        if (countEl) countEl.textContent = visible + ' member ditampilkan';
    }

    // Hitung baris saat halaman dimuat
    window.addEventListener('DOMContentLoaded', filterTable);
</script>
</body>
</html>
