<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>EcoRide - Konfirmasi Sewa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <div class="container mt-5" style="max-width: 600px;">
        <div class="card shadow">
            <div class="card-header bg-primary text-white text-center">
                <h3>Formulir Konfirmasi Sewa</h3>
            </div>
            <div class="card-body p-4">
                
                <form action="${pageContext.request.contextPath}/member/rent" method="POST">
                    
                    <input type="hidden" name="vehicleID" value="<c:out value='${chosenVehicle.vehicleID}'/>" />

                    <div class="mb-3">
                        <label class="form-label fw-bold">ID Kendaraan:</label>
                        <input type="text" class="form-control bg-white" value="<c:out value='${chosenVehicle.vehicleID}'/>" disabled />
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold">Model / Tipe Kendaraan:</label>
                        <input type="text" class="form-control bg-white" value="<c:out value='${chosenVehicle.model}'/>" disabled />
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold">Tarif Sewa:</label>
                        <div class="input-group">
                            <span class="input-group-text">Rp</span>
                            <input type="text" class="form-control bg-white" value="<c:out value='${rentalPrice}'/> / jam" disabled />
                        </div>
                    </div>

                    <div class="mb-4">
                        <label for="duration" class="form-label fw-bold">Durasi Sewa (Jam):</label>
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
                        <button type="submit" class="btn btn-success btn-lg">Konfirmasi Pembayaran Sewa</button>
                        <a href="${pageContext.request.contextPath}/member/vehicles" class="btn btn-secondary">Batal</a>
                    </div>

                </form>

            </div>
        </div>
    </div>

</body>
</html>