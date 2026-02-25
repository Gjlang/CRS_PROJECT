<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Academic - Enrolment</title>
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

<h2>Academic - Enrolment</h2>

<c:if test="${not empty message}">
  <div style="padding:10px;background:#e7ffe7;border:1px solid #8bc48b;margin-bottom:10px;">${message}</div>
</c:if>
<c:if test="${not empty error}">
  <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">${error}</div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/academic/enrolment" style="border:1px solid #ddd;padding:12px;margin-bottom:20px;">
  <label>Student ID</label><br/>
  <input name="student_id" required style="width:220px;"/><br/><br/>
  <label>Course Code</label><br/>
  <input name="course_code" required style="width:220px;"/><br/><br/>
  <button type="submit">Create Enrolment (Pending)</button>
</form>

<%-- 6.1: Title changed, loop var changed to ${requests}, new columns added --%>
<h3>My Enrolment Requests</h3>
<table border="1" cellpadding="6" cellspacing="0">
  <tr>
    <th>ID</th>
    <th>Student</th>
    <th>Course</th>
    <th>Attempt</th>
    <th>Eligibility</th>
    <th>Status</th>
    <th>Decided At</th>
    <th>Reject Reason</th>
  </tr>
  <c:forEach items="${requests}" var="e">
    <tr>
      <td>${e.enrolmentId}</td>
      <td>${e.studentId}</td>
      <td>${e.courseCode}</td>
      <td>${e.attemptNo}</td>
      <td>${e.eligibilityStatus}</td>
      <td>${e.enrolmentStatus}</td>
      <td>
        <c:choose>
          <c:when test="${not empty e.decidedAt}">${e.decidedAt}</c:when>
          <c:otherwise>_</c:otherwise>
        </c:choose>
      </td>
      <td>
        <c:choose>
          <c:when test="${not empty e.rejectReason}">${e.rejectReason}</c:when>
          <c:otherwise>_</c:otherwise>
        </c:choose>
      </td>
    </tr>
  </c:forEach>
  <c:if test="${empty requests}">
    <tr>
      <td colspan="8" style="text-align:center;color:#888;">No enrolment requests found.</td>
    </tr>
  </c:if>
</table>

</body>
</html>
