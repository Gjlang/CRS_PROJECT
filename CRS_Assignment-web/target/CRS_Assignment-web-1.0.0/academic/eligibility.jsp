<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Academic - Eligibility Check</title>
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

<h2>Academic - Eligibility Check</h2>

<c:if test="${not empty message}">
  <div style="padding:10px;background:#e7ffe7;border:1px solid #8bc48b;margin-bottom:10px;">${message}</div>
</c:if>
<c:if test="${not empty error}">
  <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">${error}</div>
</c:if>


<form method="post" action="${pageContext.request.contextPath}/academic/eligibility" style="border:1px solid #ddd;padding:12px;margin-bottom:20px;">
  <label>Student ID</label><br/>
  <input name="student_id" required style="width:260px;"/><br/><br/>
  <button type="submit">Check Eligibility</button>
</form>

<c:if test="${not empty result}">
  <h3>Eligibility Result</h3>
  <p>Student: <b>${result.studentId}</b></p>
  <p>CGPA: <b>${result.cgpa}</b></p>
  <p>Failed Course Count: <b>${result.failedCourseCount}</b></p>
  <p>Eligible: <b>${result.eligible}</b></p>
  <ul>
    <c:forEach items="${result.reasons}" var="r"><li>${r}</li></c:forEach>
  </ul>
</c:if>

</body>
</html>
