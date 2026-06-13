<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ecoride.ecoride.model.Vehicle" %>
<%@ page import="com.ecoride.ecoride.model.ElectricBike" %>
<%@ page import="com.ecoride.ecoride.model.ElectricScooter" %>
<%@ page import="com.ecoride.ecoride.model.Member" %>
<%
    Member adminMember = (Member) request.getAttribute("member");
    List<Vehicle> vehicles = (List<Vehicle>) request.getAttribute("vehicles");
    String success = request.getParameter("success");
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kelola Kendaraan — EcoRide Admin</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        body { background: #f3f4f6; }

        /* Sidebar + layout */
        .admin-layout { display: flex; min-height: calc(100vh - 60px); }
        .sidebar {
            width: 220px; flex-shrink: 0;
            background: white;
            border-right: 1px solid #e5e7eb;
            padding: 1.5rem 0;
        }
        .sidebar-label {
            font-size: 10px; font-weight: 600; color: #9ca3af;
            text-transform: uppercase; letter-spacing: .08em;
            padding: 0 1.25rem; margin-bottom: 0.5rem; margin-top: 1rem;
        }
        .sidebar-link {
            display: flex; align-items: center; gap: 0.6rem;
            padding: 0.6rem 1.25rem;
            font-size: 0.9rem; color: #374151; text-decoration: none;
            transition: background .1s, color .1s;
            border-left: 3px solid transparent;
        }
        .sidebar-link:hover { background: #f9fafb; color: #111827; }
        .sidebar-link.active {
            background: #f0fdf4; color: #0f6e56;
            border-left-color: #0f6e56; font-weight: 500;
        }
        .main-content { flex: 1; padding: 2rem; overflow-x: hidden; }

        /* Page header */
        .page-header {
            display: flex; align-items: center;
            justify-content: space-between;
            margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;
        }

        /* Vehicle table */
        .table-card {
            background: white; border-radius: 14px;
            border: 1px solid #e5e7eb;
            overflow: hidden;
            margin-bottom: 2rem;
        }
        .table-card-header {
            padding: 1rem 1.5rem;
            border-bottom: 1px solid #e5e7eb;
            display: flex; align-items: center; justify-content: space-between;
        }
        .table-card-title { font-weight: 600; font-size: 0.95rem; color: #111827; }
        .table-count {
            font-size: 0.8rem; color: #6b7280;
            background: #f3f4f6; padding: 2px 8px; border-radius: 10px;
        }
        table { width: 100%; border-collapse: collapse; font-size: 0.88rem; }
        thead { background: #f9fafb; }
        thead th {
            padding: 0.7rem 1rem; text-align: left;
            font-size: 0.75rem; font-weight: 500; color: #6b7280;
            text-transform: uppercase; letter-spacing: .04em;
            border-bottom: 1px solid #e5e7eb;
        }
        tbody tr { border-bottom: 1px solid #f3f4f6; transition: background .1s; }
        tbody tr:last-child { border-bottom: none; }
        tbody tr:hover { background: #fafafa; }
        tbody td { padding: 0.85rem 1rem; color: #374151; vertical-align: middle; }

        /* Battery bar inline */
        .bat-wrap { display: flex; align-items: center; gap: 8px; }
        .bat-bar { width: 60px; height: 6px; background: #e5e7eb; border-radius: 4px; overflow: hidden; }
        .bat-fill { height: 100%; border-radius: 4px; }
        .bat-high { background: #22c55e; }
        .bat-mid  { background: #f59e0b; }
        .bat-low  { background: #ef4444; }
        .bat-pct  { font-size: 0.82rem; color: #6b7280; }

        /* Type badge */
        .type-bike    { background: #eff6ff; color: #1d4ed8; }
        .type-scooter { background: #fdf4ff; color: #7e22ce; }

        /* Action buttons */
        .action-row { display: flex; gap: 6px; }
        .btn-action {
            padding: 5px 12px; border-radius: 7px; font-size: 0.8rem;
            font-weight: 500; cursor: pointer; border: none;
            text-decoration: none; display: inline-flex;
            align-items: center; gap: 4px; transition: opacity .12s;
        }
        .btn-action:hover { opacity: 0.8; }
        .btn-recharge { background: #eff6ff; color: #1d4ed8; }
        .btn-delete   { background: #fef2f2; color: #991b1b; }

        /* ====================================================
           Modal — Tambah Kendaraan
           ==================================================== */
        .modal-overlay {
            display: none; position: fixed; inset: 0;
            background: rgba(0,0,0,0.45); z-index: 200;
            align-items: center; justify-content: center;
            padding: 1rem;
        }
        .modal-overlay.show { display: flex; }
        .modal {
            background: white; border-radius: 18px;
            padding: 2rem; width: 100%; max-width: 480px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.15);
            max-height: 90vh; overflow-y: auto;
        }
        .modal-title {
            font-size: 1.1rem; font-weight: 700; color: #111827;
            margin-bottom: 1.5rem;
            display: flex; align-items: center; justify-content: space-between;
        }
        .modal-close {
            background: #f3f4f6; border: none; width: 28px; height: 28px;
            border-radius: 50%; cursor: pointer; font-size: 1rem;
            display: flex; align-items: center; justify-content: center;
            color: #6b7280;
        }
        .modal-close:hover { background: #e5e7eb; }
        .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
        .modal label {
            display: block; font-size: 0.85rem; font-weight: 500;
            color: #374151; margin-bottom: 0.35rem;
        }
        .modal input, .modal select {
            width: 100%; padding: 0.65rem 0.9rem;
            border: 1.5px solid #e5e7eb; border-radius: 9px;
            font-size: 0.9rem; color: #111827; background: #f9fafb;
            outline: none; transition: border-color .15s;
            box-sizing: border-box;
        }
        .modal input:focus, .modal select:focus {
            border-color: #0f6e56; background: white;
        }
        .modal-footer {
            display: flex; gap: 0.75rem; justify-content: flex-end;
            margin-top: 1.5rem;
        }
        .btn-cancel {
            padding: 0.65rem 1.25rem; background: #f3f4f6; color: #374151;
            border: 1px solid #e5e7eb; border-radius: 9px;
            font-size: 0.9rem; font-weight: 500; cursor: pointer;
        }
        .btn-save {
            padding: 0.65rem 1.5rem; background: #0f6e56; color: white;
            border: none; border-radius: 9px;
            font-size: 0.9rem; font-weight: 600; cursor: pointer;
            transition: background .12s;
        }
        .btn-save:hover { background: #0a5a45; }

        /* Extra fields toggle */
        .extra-bike    { display: none; }
        .extra-scooter { display: none; }

        /* Delete confirm modal */
        .modal-icon { font-size: 3rem; text-align: center; margin-bottom: 1rem; }
        .modal-confirm-text {
            text-align: center; color: #374151;
            font-size: 0.95rem; margin-bottom: 1.5rem; line-height: 1.6;
        }

        @media (max-width: 640px) {
            .sidebar { display: none; }
            .form-row { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>

<!-- ============================================================
     NAVBAR
     ============================================================ -->
<nav class="navbar">
    <a href="<%= request.getContextPath() %>/admin" class="navbar-brand">⚡ EcoRide</a>
    <ul class="navbar-nav">
        <li><span style="font-size:0.85rem;color:#6b7280;">
            Admin: <strong><%= adminMember != null ? adminMember.getUsername() : "" %></strong>
        </span></li>
        <li>
            <a href="<%= request.getContextPath() %>/login?logout=true"
               class="btn-logout"
               onclick="event.preventDefault();
                        document.getElementById('logoutForm').submit();">
               Keluar
            </a>
        </li>
    </ul>
</nav>
<form id="logoutForm" method="post"
      action="<%= request.getContextPath() %>/login?action=logout"
      style="display:none;"></form>

<!-- ============================================================
     LAYOUT
     ============================================================ -->
<div class="admin-layout">

    <!-- Sidebar -->
    <aside class="sidebar">
        <div class="sidebar-label">Menu Admin</div>
        <a href="<%= request.getContextPath() %>/admin?page=dashboard"
           class="sidebar-link">📊 Dashboard</a>
        <a href="<%= request.getContextPath() %>/admin?page=vehicles"
           class="sidebar-link active">🚗 Kendaraan</a>
        <a href="<%= request.getContextPath() %>/admin?page=members"
           class="sidebar-link">👥 Member</a>
        <a href="<%= request.getContextPath() %>/admin?page=transactions"
           class="sidebar-link">📋 Transaksi</a>
    </aside>

    <!-- Main -->
    <main class="main-content">

        <!-- Page header -->
        <div class="page-header">
            <div>
                <h1 class="page-title">Kelola Kendaraan</h1>
                <p class="page-sub">Tambah, hapus, dan isi ulang baterai kendaraan</p>
            </div>
            <button class="btn btn-primary" onclick="openAddModal()">
                + Tambah Kendaraan
            </button>
        </div>

        <!-- Alert sukses -->
        <% if ("added".equals(success)) { %>
            <div class="alert alert-success">✅ Kendaraan berhasil ditambahkan.</div>
        <% } else if ("deleted".equals(success)) { %>
            <div class="alert alert-error">🗑️ Kendaraan berhasil dihapus.</div>
        <% } else if ("recharged".equals(success)) { %>
            <div class="alert alert-info">🔋 Baterai berhasil diisi ulang ke 100%.</div>
        <% } %>

        <!-- Tabel kendaraan -->
        <div class="table-card">
            <div class="table-card-header">
                <span class="table-card-title">Daftar Kendaraan</span>
                <span class="table-count">
                    <%= vehicles != null ? vehicles.size() : 0 %> unit
                </span>
            </div>

            <% if (vehicles == null || vehicles.isEmpty()) { %>
                <div class="empty-state">
                    <div class="empty-state-icon">🚗</div>
                    <p class="empty-state-text">Belum ada kendaraan. Tambahkan yang pertama!</p>
                </div>
            <% } else { %>
            <table>
                <thead>
                    <tr>
                        <th>Kode</th>
                        <th>Model</th>
                        <th>Tipe</th>
                        <th>Baterai</th>
                        <th>Status</th>
                        <th>Tarif/Menit</th>
                        <th>Info</th>
                        <th>Aksi</th>
                    </tr>
                </thead>
                <tbody>
                <% for (Vehicle v : vehicles) {
                    double bat = v.getBatteryLevel();
                    String batClass = bat >= 60 ? "bat-high" : bat >= 30 ? "bat-mid" : "bat-low";
                    String batWidth = String.valueOf((int) bat);
                    boolean isBike = v instanceof ElectricBike;
                    String typeLabel = isBike ? "Sepeda" : "Skuter";
                    String typeClass = isBike ? "type-bike" : "type-scooter";
                %>
                <tr>
                    <!-- Kode -->
                    <td><strong><%= v.getVehicleID() %></strong></td>

                    <!-- Model -->
                    <td><%= v.getModel() %></td>

                    <!-- Tipe -->
                    <td>
                        <span class="badge <%= typeClass %>">
                            <%= isBike ? "🚲" : "🛵" %> <%= typeLabel %>
                        </span>
                    </td>

                    <!-- Baterai -->
                    <td>
                        <div class="bat-wrap">
                            <div class="bat-bar">
                                <div class="bat-fill <%= batClass %>"
                                     style="width:<%= batWidth %>%"></div>
                            </div>
                            <span class="bat-pct"><%= (int)bat %>%</span>
                        </div>
                    </td>

                    <!-- Status -->
                    <td>
                        <% if (v.isAvailable()) { %>
                            <span class="badge badge-green">✓ Tersedia</span>
                        <% } else { %>
                            <span class="badge badge-red">✗ Disewa</span>
                        <% } %>
                    </td>

                    <!-- Tarif -->
                    <td>Rp<%= String.format("%,.0f", v.getRentPerMinute()) %></td>

                    <!-- Info tambahan -->
                    <td>
                        <% if (isBike) {
                            ElectricBike bike = (ElectricBike) v; %>
                            <span style="font-size:0.82rem;color:#6b7280;">
                                Pedal: <%= bike.isHasPedals() ? "Ya" : "Tidak" %>
                            </span>
                        <% } else {
                            ElectricScooter sc = (ElectricScooter) v; %>
                            <span style="font-size:0.82rem;color:#6b7280;">
                                Max: <%= sc.getMaxSpeed() %> km/h
                            </span>
                        <% } %>
                    </td>

                    <!-- Aksi -->
                    <td>
                        <div class="action-row">
                            <!-- Recharge -->
                            <form method="post"
                                  action="<%= request.getContextPath() %>/admin"
                                  style="display:inline;">
                                <input type="hidden" name="action" value="recharge">
                                <input type="hidden" name="vehicleId"
                                       value="<%= v.getVehicleID() %>">
                                <button type="submit" class="btn-action btn-recharge"
                                        title="Isi baterai ke 100%">
                                    🔋 Charge
                                </button>
                            </form>

                            <!-- Delete -->
                            <button class="btn-action btn-delete"
                                    onclick="openDeleteModal('<%= v.getVehicleID() %>',
                                                             '<%= v.getModel() %>')"
                                    title="Hapus kendaraan">
                                🗑️ Hapus
                            </button>
                        </div>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <% } %>
        </div><!-- end table-card -->

    </main>
</div><!-- end admin-layout -->


<!-- ============================================================
     MODAL — Tambah Kendaraan
     ============================================================ -->
<div class="modal-overlay" id="addModal">
    <div class="modal">
        <div class="modal-title">
            Tambah Kendaraan Baru
            <button class="modal-close" onclick="closeAddModal()">✕</button>
        </div>

        <form method="post" action="<%= request.getContextPath() %>/admin"
              id="addVehicleForm">
            <input type="hidden" name="action" value="addVehicle">

            <!-- Tipe kendaraan -->
            <div class="form-group" style="margin-bottom:1rem;">
                <label>Tipe Kendaraan</label>
                <select name="vehicleType" id="vehicleType"
                        onchange="toggleTypeFields(this.value)" required>
                    <option value="">-- Pilih tipe --</option>
                    <option value="bike">🚲 Sepeda Listrik</option>
                    <option value="scooter">🛵 Skuter Listrik</option>
                </select>
            </div>

            <div class="form-row">
                <!-- Kode kendaraan -->
                <div class="form-group" style="margin-bottom:1rem;">
                    <label>Kode Kendaraan</label>
                    <input type="text" name="vehicleCode"
                           placeholder="Contoh: BIKE-004"
                           required>
                </div>

                <!-- Tarif per menit -->
                <div class="form-group" style="margin-bottom:1rem;">
                    <label>Tarif per Menit (Rp)</label>
                    <input type="number" name="rentPerMinute"
                           placeholder="Contoh: 300" min="100" required>
                </div>
            </div>

            <!-- Nama model -->
            <div class="form-group" style="margin-bottom:1rem;">
                <label>Nama Model</label>
                <input type="text" name="model"
                       placeholder="Contoh: Polygon Zinq E3" required>
            </div>

            <!-- Field khusus Sepeda -->
            <div class="extra-bike" id="extraBike">
                <div class="form-group" style="margin-bottom:1rem;">
                    <label>Punya Pedal?</label>
                    <select name="hasPedals">
                        <option value="true">Ya</option>
                        <option value="false">Tidak</option>
                    </select>
                </div>
            </div>

            <!-- Field khusus Skuter -->
            <div class="extra-scooter" id="extraScooter">
                <div class="form-group" style="margin-bottom:1rem;">
                    <label>Kecepatan Maksimal (km/h)</label>
                    <input type="number" name="maxSpeed"
                           placeholder="Contoh: 25" min="1" max="60">
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-cancel"
                        onclick="closeAddModal()">Batal</button>
                <button type="submit" class="btn-save">Simpan</button>
            </div>
        </form>
    </div>
</div>


<!-- ============================================================
     MODAL — Konfirmasi Hapus
     ============================================================ -->
<div class="modal-overlay" id="deleteModal">
    <div class="modal" style="max-width:380px;">
        <div class="modal-icon">⚠️</div>
        <p class="modal-confirm-text">
            Yakin ingin menghapus kendaraan<br>
            <strong id="deleteVehicleName"></strong>?<br>
            <span style="font-size:0.85rem;color:#9ca3af;">
                Tindakan ini tidak bisa dibatalkan.
            </span>
        </p>
        <form method="post" action="<%= request.getContextPath() %>/admin"
              id="deleteForm">
            <input type="hidden" name="action" value="deleteVehicle">
            <input type="hidden" name="vehicleId" id="deleteVehicleId">
            <div class="modal-footer" style="justify-content:center;">
                <button type="button" class="btn-cancel"
                        onclick="closeDeleteModal()">Batal</button>
                <button type="submit"
                        style="padding:0.65rem 1.5rem;background:#dc2626;
                               color:white;border:none;border-radius:9px;
                               font-size:0.9rem;font-weight:600;cursor:pointer;">
                    Hapus
                </button>
            </div>
        </form>
    </div>
</div>


<script>
    // ============================================================
    // Modal: Tambah Kendaraan
    // ============================================================
    function openAddModal() {
        document.getElementById('addModal').classList.add('show');
    }
    function closeAddModal() {
        document.getElementById('addModal').classList.remove('show');
        document.getElementById('addVehicleForm').reset();
        toggleTypeFields('');
    }

    // Tampilkan field sesuai tipe yang dipilih
    function toggleTypeFields(type) {
        document.getElementById('extraBike').style.display    = (type === 'bike')    ? 'block' : 'none';
        document.getElementById('extraScooter').style.display = (type === 'scooter') ? 'block' : 'none';

        // set required dinamis
        const maxSpeed = document.querySelector('[name="maxSpeed"]');
        if (maxSpeed) maxSpeed.required = (type === 'scooter');
    }

    // ============================================================
    // Modal: Hapus Kendaraan
    // ============================================================
    function openDeleteModal(vehicleId, vehicleModel) {
        document.getElementById('deleteVehicleId').value   = vehicleId;
        document.getElementById('deleteVehicleName').textContent = vehicleModel + ' (' + vehicleId + ')';
        document.getElementById('deleteModal').classList.add('show');
    }
    function closeDeleteModal() {
        document.getElementById('deleteModal').classList.remove('show');
    }

    // Tutup modal kalau klik overlay
    document.querySelectorAll('.modal-overlay').forEach(overlay => {
        overlay.addEventListener('click', function(e) {
            if (e.target === this) this.classList.remove('show');
        });
    });

    // Auto-hide alert setelah 4 detik
    setTimeout(() => {
        document.querySelectorAll('.alert').forEach(el => {
            el.style.transition = 'opacity .5s';
            el.style.opacity = '0';
            setTimeout(() => el.remove(), 500);
        });
    }, 4000);
</script>

</body>
</html>
