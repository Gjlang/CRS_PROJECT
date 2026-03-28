<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Forgot Password</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css" />
</head>
<body>
<div class="card">
  <h2>Forgot Password</h2>

  <c:if test="${not empty message}">
    <div class="alert alert-success">${message}</div>
  </c:if>

  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/forgot_password" autocomplete="off">
    <label for="email">Email</label><br/>
    <input
      id="email"
      name="email"
      type="email"
      autocomplete="off"
      autocapitalize="none"
      spellcheck="false"
      required
    /><br/><br/>

    <button type="submit" class="btn btn-primary">Send Reset Token</button>
  </form>

  <p><a href="${pageContext.request.contextPath}/reset_password">I already have a token</a></p>
  <p><a href="${pageContext.request.contextPath}/login.jsp">Back to Login</a></p>
</div>
</body>
</html>