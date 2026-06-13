<%-- 
    Document   : register
    Created on : 21 Mei 2026, 16.26.49
    Author     : rifky
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Daftar — EcoRide</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
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
        .form-hint {
            font-size: 0.8rem;
            color: #9ca3af;
            margin-top: 0.3rem;
        }
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
        .auth-footer a:hover { text-decoration: underline; }

        /* Password strength indicator */
        .strength-bar {
            height: 4px;
            border-radius: 4px;
            background: #e5e7eb;
            margin-top: 0.5rem;
            overflow: hidden;
        }
        .strength-fill {
            height: 100%;
            border-radius: 4px;
            width: 0%;
            transition: width .3s, background .3s;
        }
        .strength-text {
            font-size: 0.75rem;
            margin-top: 0.2rem;
            color: #9ca3af;
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
        <h1 class="auth-title">Buat Akun</h1>
        <p class="auth-subtitle">Bergabung dengan EcoRide dan mulai menyewa!</p>

        <!-- Pesan error dari RegisterServlet -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">
                ⚠️ <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <!-- Form Register -->
        <form method="post" action="<%= request.getContextPath() %>/register"
              id="registerForm">

            <div class="form-group">
                <label class="form-label" for="username">Username</label>
                <input
                    class="form-input"
                    type="text"
                    id="username"
                    name="username"
                    placeholder="Buat username kamu"
                    value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>"
                    required
                    autofocus
                >
                <p class="form-hint">Hanya huruf, angka, dan underscore.</p>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">Password</label>
                <input
                    class="form-input"
                    type="password"
                    id="password"
                    name="password"
                    placeholder="Buat password"
                    required
                    oninput="checkStrength(this.value)"
                >
                <!-- Password strength bar -->
                <div class="strength-bar">
                    <div class="strength-fill" id="strengthFill"></div>
                </div>
                <p class="strength-text" id="strengthText">Minimal 6 karakter</p>
            </div>

            <div class="form-group">
                <label class="form-label" for="confirmPassword">Konfirmasi Password</label>
                <input
                    class="form-input"
                    type="password"
                    id="confirmPassword"
                    name="confirmPassword"
                    placeholder="Ulangi password"
                    required
                    oninput="checkMatch()"
                >
                <p class="form-hint" id="matchText"></p>
            </div>

            <button type="submit" class="btn-submit" id="submitBtn">
                Daftar Sekarang
            </button>
        </form>

        <div class="auth-footer">
            Sudah punya akun?
            <a href="<%= request.getContextPath() %>/login">Masuk di sini</a>
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

<script>
    // Password strength checker
    function checkStrength(val) {
        const fill = document.getElementById('strengthFill');
        const text = document.getElementById('strengthText');
        let strength = 0;
        if (val.length >= 6)  strength++;
        if (val.length >= 10) strength++;
        if (/[A-Z]/.test(val)) strength++;
        if (/[0-9]/.test(val)) strength++;
        if (/[^A-Za-z0-9]/.test(val)) strength++;

        const levels = [
            { w: '0%',   c: '#e5e7eb', t: 'Minimal 6 karakter' },
            { w: '25%',  c: '#ef4444', t: 'Lemah' },
            { w: '50%',  c: '#f97316', t: 'Cukup' },
            { w: '75%',  c: '#eab308', t: 'Baik' },
            { w: '100%', c: '#22c55e', t: 'Kuat 💪' },
        ];
        const level = val.length === 0 ? 0 : Math.min(strength, 4);
        fill.style.width      = levels[level].w;
        fill.style.background = levels[level].c;
        text.textContent      = levels[level].t;
        text.style.color      = levels[level].c;
        checkMatch();
    }

    // Password match checker
    function checkMatch() {
        const pw  = document.getElementById('password').value;
        const cpw = document.getElementById('confirmPassword').value;
        const txt = document.getElementById('matchText');
        const btn = document.getElementById('submitBtn');

        if (cpw.length === 0) {
            txt.textContent = '';
            return;
        }
        if (pw === cpw) {
            txt.textContent  = '✅ Password cocok';
            txt.style.color  = '#16a34a';
            btn.disabled     = false;
        } else {
            txt.textContent  = '❌ Password tidak cocok';
            txt.style.color  = '#dc2626';
            btn.disabled     = true;
        }
    }
</script>

</body>
</html>
