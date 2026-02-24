<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"/><title>Forgot Password</title></head>
<body style="font-family:Arial, sans-serif; padding:20px;">
<h2>Forgot Password</h2>
<c:if test="${not empty message}">
  <div style="padding:10px;background:#e7ffe7;border:1px solid #8bc48b;margin-bottom:10px;">${message}</div>
</c:if>
<c:if test="${not empty error}">
  <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">${error}</div>
</c:if>
<form method="post" action="${pageContext.request.contextPath}/forgot_password">
  <label>Email</label><br/>
  <input name="email" type="email" required style="width:320px;"/><br/><br/>
  <button type="submit">Send Reset Token</button>
</form>
<p><a href="${pageContext.request.contextPath}/reset_password.jsp">I already have a token</a></p>
<p><a href="${pageContext.request.contextPath}/login.jsp">Back to Login</a></p>
</body>
</html>
