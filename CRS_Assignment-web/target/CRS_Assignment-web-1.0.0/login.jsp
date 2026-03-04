<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>CRS Login</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css" />
</head>
<body>

<div class="auth-shell">

  <!-- Top bar -->
  <div class="auth-topbar">
    <div class="auth-logo">
      <span>CRS</span>
    </div>

    <div class="auth-toplinks">
      <a href="#" onclick="return false;">Documentation</a>
      <a href="#" onclick="return false;">Support</a>
    </div>
  </div>

  <!-- Center card -->
  <div class="auth-center">
    <div class="auth-card">

      <div class="auth-title">Welcome back</div>
      <div class="auth-subtitle">Course Recovery System Management Portal</div>

      <div class="auth-banner">
        <div class="auth-banner-icon">🛡️</div>
        <div>
          <div class="auth-banner-title">Identity Verification</div>
          <div class="auth-banner-desc">Secure enterprise single sign-on access</div>
        </div>
      </div>

      <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/login">

        <div class="field">
          <label>Username / Email</label>
          <input name="email" type="email" placeholder="e.g. j.smith@crs.edu" required />
        </div>

        <div class="field">
          <label>Password</label>
          <input name="password" type="password" placeholder="••••••••" required />
        </div>

        <div class="auth-row">
          <div class="auth-meta">
            <label style="display:flex; gap:8px; align-items:center;">
              <input type="checkbox" name="remember" value="1" />
              Remember me
            </label>
          </div>

          <a class="auth-forgot" href="${pageContext.request.contextPath}/forgot_password">
            Forgot password?
          </a>
        </div>

        <button type="submit" class="btn btn-primary auth-btn">
          Sign In to CRS JAWAA →
        </button>

      </form>

      <div class="auth-footnote">ENTERPRISE ACCESS ONLY</div>

    </div>
  </div>

  <!-- Bottom / footer -->
  <div class="auth-bottom">
    System maintenance scheduled for Sundays 02:00 – 04:00 GMT.
    <div style="margin-top:6px;">CRS Assignment — EPDA</div>
  </div>

</div>

</body>
</html>