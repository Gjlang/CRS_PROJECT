<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"/><title>Reset Password</title></head>
<body style="font-family:Arial, sans-serif; padding:20px;">
<h2>Reset Password</h2>
<c:if test="${not empty message}">
  <div style="padding:10px;background:#e7ffe7;border:1px solid #8bc48b;margin-bottom:10px;">${message}</div>
</c:if>
<c:if test="${not empty error}">
  <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">${error}</div>
</c:if>
<form method="post" action="${pageContext.request.contextPath}/reset_password">
  <label>Reset Token</label><br/>
  <input name="token" required style="width:320px;"/><br/><br/>
  <label>New Password</label><br/>
  <input name="new_password" type="password" required style="width:320px;"/><br/><br/>
  <button type="submit">Reset</button>
</form>
<p><a href="${pageContext.request.contextPath}/login.jsp">Back to Login</a></p>
</body>
</html>
