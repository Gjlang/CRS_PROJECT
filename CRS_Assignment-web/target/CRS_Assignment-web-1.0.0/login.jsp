<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>CRS Login</title>
</head>
<body style="font-family:Arial, sans-serif; padding:20px;">
<h2>CRS Login</h2>
<c:if test="${not empty error}">
  <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">${error}</div>
</c:if>
<form method="post" action="${pageContext.request.contextPath}/login">
  <label>Email</label><br/>
  <input name="email" type="email" required style="width:320px;"/><br/><br/>
  <label>Password</label><br/>
  <input name="password" type="password" required style="width:320px;"/><br/><br/>
  <button type="submit">Login</button>
</form>
<p><a href="${pageContext.request.contextPath}/forgot_password.jsp">Forgot Password?</a></p>
</body>
</html>
