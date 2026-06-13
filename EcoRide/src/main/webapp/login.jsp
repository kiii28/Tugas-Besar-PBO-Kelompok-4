<%-- 
    Document   : login
    Created on : 21 Mei 2026, 16.26.24
    Author     : rifky
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Masuk — EcoRide</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        /* ====================================================
           AUTH PAGE STYLES (dipakai login & register)
           ==================================================== */
        body {
            min-height: 100vh;
            background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 2rem 1rem;
        }
        .auth-wrapper {
            width: 100%;
            max-width: 420px;
        }
        .auth-brand {
            text-align: center;
            margin-bottom: 2rem;
        }
        .auth-brand a {
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            font-size: 1.5rem;
            font-weight: 700;
            color: #0f6e56;
        }
        .auth-card {
            background: white;
            border-radius: 20px;
            padding: 2.5rem 2rem;
            box-shadow: 0 4px 30px rgba(0,0,0,0.08);
            border: 1px solid #e5e7eb;
        }
        .auth-title {
            font-size: 1.5rem;
            font-weight: 700;
            color: #111827;
            margin-bottom: 0.4rem;
        }
        .auth-subtitle {
            color: #6b7280;
            font-size: 0.9rem;
            margin-bottom: 1.8rem;
        }

        /* Alert */
        .alert {
            padding: 0.8rem 1rem;
            border-radius: 10px;
            font-size: 0.9rem;
            margin-bottom: 1.2rem;
        }
        .alert-error {
            background: #fef2f2;
            color: #991b1b;
            border: 1px solid #fecaca;
        }
        .alert-success {
            background: #f0fdf4;
            color: #166534;
            border: 1px solid #bbf7d0;
        }

        /* Form */
        .form-group {
            margin-bottom: 1.2rem;
        }
        .form-label {
            display: block;
            font-size: 0.875rem;
            font-weight: 500;
            color: #374151;
            margin-bottom: 0.4rem;
        }
        .form-input {
            width: 100%;
            padding: 0.75rem 1rem;
            border: 1.5px solid #e5e7eb;
            border-radius: 10px;
            font-size: 0.95rem;
            color: #111827;
            background: #f9fafb;
            outline: none;
            transition: border-color .15s, background .15s;
            box-sizing: border-box;
        }
        .form-input:focus {
            border-color: #0f6e56;
            background: white;
        }

        /* Button */
        .btn-submit {
            width: 100%;
            padding: 0.85rem;
            background: #0f6e56;
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: background .15s, transform .1s;
            margin-top: 0.5rem;
        }
        .btn-submit:hover {
            background: #0a5a45;
            transform: translateY(-1px);
        }
        .btn-submit:active {
            transform: translateY(0);
        }

        /* Footer link */
        .auth-footer {
            text-align: center;
            margin-top: 1.5rem;
            font-size: 0.9rem;
            color: #6b7280;
        }
        .auth-footer a {
            color: #0f6e56;
            font-weight: 500;
            text-decoration: none;
        }
        .auth-footer a:hover {
            text-decoration: underline;
        }

        /* Divider */
        .divider {
            display: flex;
            align-items: center;
            gap: 0.75rem;
            margin: 1.5rem 0;
            color: #9ca3af;
            font-size: 0.85rem;
        }
        .divider::before, .divider::after {
            content: '';
            flex: 1;
            height: 1px;
            background: #e5e7eb;
        }
    </style>
</head>
<body>

<div class="auth-wrapper">

    <!-- Brand -->
    <div class="auth-brand">
        <a href="<%= request.getContextPath() %>/">
            ⚡ EcoRide
        </a>
    </div>

    <!-- Card -->
    <div class="auth-card">
        <h1 class="auth-title">Masuk</h1>
        <p class="auth-subtitle">Selamat datang kembali! Masukkan akun kamu.</p>

        <!-- Pesan error dari LoginServlet -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">
                ⚠️ <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <!-- Pesan sukses setelah register -->
        <% if ("true".equals(request.getParameter("registered"))) { %>
            <div class="alert alert-success">
                ✅ Registrasi berhasil! Silakan masuk dengan akun baru kamu.
            </div>
        <% } %>

        <!-- Form Login -->
        <form method="post" action="<%= request.getContextPath() %>/login">

            <div class="form-group">
                <label class="form-label" for="username">Username</label>
                <input
                    class="form-input"
                    type="text"
                    id="username"
                    name="username"
                    placeholder="Masukkan username"
                    value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>"
                    required
                    autofocus
                >
            </div>

            <div class="form-group">
                <label class="form-label" for="password">Password</label>
                <input
                    class="form-input"
                    type="password"
                    id="password"
                    name="password"
                    placeholder="Masukkan password"
                    required
                >
            </div>

            <button type="submit" class="btn-submit">Masuk</button>
        </form>

        <div class="auth-footer">
            Belum punya akun?
            <a href="<%= request.getContextPath() %>/register">Daftar sekarang</a>
        </div>
    </div>

    <!-- Back to home -->
    <div style="text-align:center; margin-top:1.2rem;">
        <a href="<%= request.getContextPath() %>/"
           style="color:#6b7280; font-size:0.85rem; text-decoration:none;">
            ← Kembali ke beranda
        </a>
    </div>

</div>

</body>
</html>
