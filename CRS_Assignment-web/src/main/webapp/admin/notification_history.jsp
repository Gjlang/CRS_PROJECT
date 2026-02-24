<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin - Notification History</title>
</head>
<body style="font-family:Arial, sans-serif; padding:20px;">

<div style="padding:10px;background:#f4f4f4;margin-bottom:12px;">
  <a href="${pageContext.request.contextPath}/dashboard.jsp">Dashboard</a>
  | <a href="${pageContext.request.contextPath}/logout">Logout</a>
  <c:if test="${sessionScope.role == 'COURSE_ADMIN'}">
    | <a href="${pageContext.request.contextPath}/admin/users">User Management</a>
    | <a href="${pageContext.request.contextPath}/admin/notification_history">Notification History</a>
  </c:if>
  <c:if test="${sessionScope.role == 'ACADEMIC_OFFICER'}">
    | <a href="${pageContext.request.contextPath}/academic/eligibility">Eligibility</a>
    | <a href="${pageContext.request.contextPath}/academic/enrolment">Enrolment</a>
    | <a href="${pageContext.request.contextPath}/academic/recovery_plan">Recovery Plan</a>
    | <a href="${pageContext.request.contextPath}/academic/report">Report</a>
  </c:if>
</div>

<h2>Admin - Notification History</h2>

<c:if test="${not empty message}">
  <div style="padding:10px;background:#e7ffe7;border:1px solid #8bc48b;margin-bottom:10px;">${message}</div>
</c:if>
<c:if test="${not empty error}">
  <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">${error}</div>
</c:if>


<h3>Notification History (latest 50)</h3>
<table border="1" cellpadding="6" cellspacing="0">
  <tr>
    <th>ID</th><th>To</th><th>Type</th><th>Status</th><th>Subject</th><th>Created</th><th>Error</th>
  </tr>
  <c:forEach items="${history}" var="n">
    <tr>
      <td>${n.notificationId}</td>
      <td>${n.recipientEmail}</td>
      <td>${n.type}</td>
      <td>${n.status}</td>
      <td>${n.subject}</td>
      <td>${n.createdAt}</td>
      <td>${n.errorMessage}</td>
    </tr>
  </c:forEach>
</table>

</body>
</html>
