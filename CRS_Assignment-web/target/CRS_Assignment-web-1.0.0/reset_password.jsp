<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Reset Password</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css" />
</head>
<body>
<div class="card">
  <h2>Reset Password</h2>
  <c:if test="${not empty message}">
    <div class="alert alert-success">${message}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>
  <form method="post" action="${pageContext.request.contextPath}/reset_password">
    <label>Reset Token</label><br/>
    <input name="token" required /><br/><br/>
    <label>New Password</label><br/>
    <input name="new_password" type="password" required /><br/><br/>
    <button type="submit" class="btn btn-primary">Reset</button>
  </form>
  <p><a href="${pageContext.request.contextPath}/login.jsp">Back to Login</a></p>
</div>
</body>
</html>
