<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.ecoride.ecoride.model.Member" %>
<%
    // Jika sudah login, langsung redirect ke halaman sesuai role
    Member sessionMember = (Member) session.getAttribute("loggedInMember");
    if (sessionMember != null) {
        if (sessionMember.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/admin");
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
        return;
    }
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EcoRide ” Sewa Kendaraan Listrik</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        /* ====================================================
           INDEX PAGE STYLES
           ==================================================== */
        .hero {
            min-height: 100vh;
            background: linear-gradient(135deg, #0f6e56 0%, #1a9e75 50%, #22c55e 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
            padding: 2rem;
            position: relative;
            overflow: hidden;
        }
        .hero::before {
            content: '';
            position: absolute;
            width: 600px; height: 600px;
            border-radius: 50%;
            background: rgba(255,255,255,0.05);
            top: -200px; right: -200px;
        }
        .hero::after {
            content: '';
            position: absolute;
            width: 400px; height: 400px;
            border-radius: 50%;
            background: rgba(255,255,255,0.05);
            bottom: -150px; left: -100px;
        }
        .hero-content {
            position: relative;
            z-index: 1;
            max-width: 600px;
        }
        .hero-logo {
            font-size: 3rem;
            margin-bottom: 0.5rem;
        }
        .hero-title {
            font-size: 3rem;
            font-weight: 700;
            color: white;
            margin-bottom: 1rem;
            line-height: 1.1;
        }
        .hero-title span {
            color: #a7f3d0;
        }
        .hero-subtitle {
            font-size: 1.1rem;
            color: rgba(255,255,255,0.85);
            margin-bottom: 2.5rem;
            line-height: 1.6;
        }
        .hero-buttons {
            display: flex;
            gap: 1rem;
            justify-content: center;
            flex-wrap: wrap;
        }
        .btn-hero-primary {
            background: white;
            color: #0f6e56;
            padding: 0.85rem 2rem;
            border-radius: 50px;
            font-weight: 600;
            font-size: 1rem;
            text-decoration: none;
            transition: transform .15s, box-shadow .15s;
            box-shadow: 0 4px 15px rgba(0,0,0,0.2);
        }
        .btn-hero-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(0,0,0,0.25);
        }
        .btn-hero-secondary {
            background: transparent;
            color: white;
            padding: 0.85rem 2rem;
            border-radius: 50px;
            font-weight: 600;
            font-size: 1rem;
            text-decoration: none;
            border: 2px solid rgba(255,255,255,0.7);
            transition: background .15s, border-color .15s;
        }
        .btn-hero-secondary:hover {
            background: rgba(255,255,255,0.1);
            border-color: white;
        }

        /* Features section */
        .features {
            padding: 5rem 2rem;
            background: #f9fafb;
        }
        .features-container {
            max-width: 900px;
            margin: 0 auto;
            text-align: center;
        }
        .features-title {
            font-size: 2rem;
            font-weight: 700;
            color: #111827;
            margin-bottom: 0.5rem;
        }
        .features-sub {
            color: #6b7280;
            margin-bottom: 3rem;
        }
        .features-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 1.5rem;
        }
        .feature-card {
            background: white;
            border-radius: 16px;
            padding: 2rem 1.5rem;
            border: 1px solid #e5e7eb;
            transition: transform .15s, box-shadow .15s;
        }
        .feature-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.08);
        }
        .feature-icon {
            font-size: 2.5rem;
            margin-bottom: 1rem;
        }
        .feature-name {
            font-weight: 600;
            color: #111827;
            margin-bottom: 0.5rem;
        }
        .feature-desc {
            font-size: 0.9rem;
            color: #6b7280;
            line-height: 1.5;
        }

        /* Footer */
        .footer {
            background: #111827;
            color: #9ca3af;
            text-align: center;
            padding: 1.5rem;
            font-size: 0.85rem;
        }
        .footer span { color: #1a9e75; }
    </style>
</head>
<body>

    <!-- ======================================================
         HERO SECTION
         ====================================================== -->
    <section class="hero">
        <div class="hero-content">
            <div class="hero-logo">⚡⚡</div>
            <h1 class="hero-title">
                Selamat Datang di<br><span>EcoRide</span>
            </h1>
            <p class="hero-subtitle">
                Platform penyewaan kendaraan listrik ramah lingkungan
                untuk mobilitas kampus dan perkotaan yang lebih bersih.
            </p>
            <div class="hero-buttons">
                <a href="<%= request.getContextPath() %>/login" class="btn-hero-primary">
                    Masuk
                </a>
                <a href="<%= request.getContextPath() %>/register" class="btn-hero-secondary">
                    Daftar Sekarang
                </a>
            </div>
        </div>
    </section>

    <!-- ======================================================
         FEATURES SECTION
         ====================================================== -->
    <section class="features">
        <div class="features-container">
            <h2 class="features-title">Mengapa EcoRide?</h2>
            <p class="features-sub">Solusi transportasi modern yang mudah, cepat, dan ramah lingkungan</p>
            <div class="features-grid">
                <div class="feature-card">
                    <div class="feature-icon"></div>
                    <div class="feature-name">Sepeda Listrik</div>
                    <p class="feature-desc">Sepeda listrik nyaman dengan pilihan model berkualitas untuk perjalanan singkat.</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon"></div>
                    <div class="feature-name">Skuter Listrik</div>
                    <p class="feature-desc">Skuter listrik cepat dan efisien untuk mobilitas lebih jauh di sekitar kampus.</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon"></div>
                    <div class="feature-name">Baterai Terpantau</div>
                    <p class="feature-desc">Status baterai selalu terpantau. Hanya kendaraan siap pakai yang tersedia.</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon"></div>
                    <div class="feature-name">Saldo Digital</div>
                    <p class="feature-desc">Top-up saldo kapan saja. Bayar otomatis saat selesai menyewa.</p>
                </div>
            </div>
        </div>
    </section>

    <!-- ======================================================
         FOOTER
         ====================================================== -->
    <footer class="footer">
        <p>&copy; 2025 <span>EcoRide</span> ” Kelompok 4 IF-48-02, Universitas Telkom</p>
    </footer>

</body>
</html>

