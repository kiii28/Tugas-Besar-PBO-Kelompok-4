<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>EcoRide - Riwayat Sewa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <div class="container mt-5">
        
        <c:if test="${not empty notifikasiSukses}">
            <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
                <strong>✨ Transaksi Berhasil!</strong> <c:out value="${notifikasiSukses}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Riwayat Penyewaan Kendaraan Anda</h2>
            <a href="${pageContext.request.contextPath}/member/vehicles" class="btn btn-primary">Sewa Kendaraan Lagi</a>
        </div>

        <div class="card shadow-sm">
            <div class="card-body">
                <table class="table table-striped table-hover align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>ID Transaksi</th>
                            <th>ID Kendaraan</th>
                            <th>Durasi Sewa</th>
                            <th>Total Biaya</th>
                            <th>Tanggal Sewa</th>
                            <th>Status Pembayaran</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="t" items="${daftarRiwayat}">
                            <tr>
                                <td><strong><c:out value="${t[0]}"/></strong></td>
                                <td><c:out value="${t[1]}"/></td>
                                <td><c:out value="${t[2]}"/></td>
                                <td><c:out value="${t[3]}"/></td>
                                <td><c:out value="${t[4]}"/></td>
                                <td>
                                    <span class="badge bg-success"><c:out value="${t[5]}"/></span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>