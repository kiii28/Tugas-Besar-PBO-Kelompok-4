<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>EcoRide - Daftar Kendaraan</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <div class="container mt-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Katalog Kendaraan Listrik EcoRide</h2>
            <a href="${pageContext.request.contextPath}/member/dashboard.jsp" class="btn btn-secondary">Kembali ke Dashboard</a>
        </div>

        <div class="card shadow-sm">
            <div class="card-body">
                <table class="table table-striped table-hover align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>ID Kendaraan</th>
                            <th>Model / Tipe</th>
                            <th>Kapasitas Baterai</th>
                            <th>Status</th>
                            <th>Fitur Spesifik</th>
                            <th>Aksi</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="v" items="${daftarKendaraan}">
                            <tr>
                                <td><strong><c:out value="${v.vehicleID}"/></strong></td>
                                <td><c:out value="${v.model}"/></td>
                                <td>
                                    <span class="fw-bold text-success">
                                        <c:out value="${v.batteryLevel}"/> %
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${v.isAvailable}">
                                            <span class="badge bg-success">Tersedia</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger">Sedang Disewa</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${v.vehicleID.startsWith('EB')}">
                                            🚲 Memiliki Pedal
                                        </c:when>
                                        <c:otherwise>
                                            🛴 Max Speed: 25 km/jam
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:if test="${v.isAvailable}">
                                        <a href="${pageContext.request.contextPath}/member/rent?id=${v.vehicleID}" class="btn btn-sm btn-primary">Sewa Sekarang</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</body>
</html>