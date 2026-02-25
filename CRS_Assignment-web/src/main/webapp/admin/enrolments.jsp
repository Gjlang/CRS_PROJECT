<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Admin - Enrolment Approvals</title>
</head>
<body style="font-family:Arial, sans-serif; padding:20px;">

<div style="padding:10px;background:#f4f4f4;margin-bottom:12px;">
  <a href="${pageContext.request.contextPath}/dashboard.jsp">Dashboard</a>
  | <a href="${pageContext.request.contextPath}/logout">Logout</a>
  | <a href="${pageContext.request.contextPath}/admin/users">User Management</a>
  | <a href="${pageContext.request.contextPath}/admin/enrolments">Enrolment Approvals</a>
  | <a href="${pageContext.request.contextPath}/admin/notification_history">Notification History</a>
</div>

<h2>Admin - Enrolment Approvals</h2>

<c:if test="${not empty message}">
  <div style="padding:10px;background:#e7ffe7;border:1px solid #8bc48b;margin-bottom:10px;">${message}</div>
</c:if>
<c:if test="${not empty error}">
  <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">${error}</div>
</c:if>

<h3>Pending Enrolments</h3>
<table border="1" cellpadding="6" cellspacing="0" style="width:100%;border-collapse:collapse;">
  <tr style="background:#f0f0f0;">
    <th>ID</th>
    <th>Student</th>
    <th>Course</th>
    <th>Attempt</th>
    <th>Eligibility</th>
    <th>Submitted By (User ID)</th>
    <th>Created At</th>
    <th>Approve</th>
    <th>Reject</th>
  </tr>
  <c:forEach items="${pending}" var="e">
    <tr>
      <td>${e.enrolmentId}</td>
      <td>${e.studentId}</td>
      <td>${e.courseCode}</td>
      <td>${e.attemptNo}</td>
      <td>${e.eligibilityStatus}</td>
      <td>${e.createdByUserId}</td>
      <td>${e.createdAt}</td>

      <%-- Approve button --%>
      <td>
        <form method="post" action="${pageContext.request.contextPath}/admin/enrolments"
              onsubmit="return confirm('Approve enrolment #${e.enrolmentId}?');">
          <input type="hidden" name="action" value="approve"/>
          <input type="hidden" name="enrolment_id" value="${e.enrolmentId}"/>
          <button type="submit"
                  style="background:#4caf50;color:white;border:none;padding:5px 12px;cursor:pointer;border-radius:3px;">
            Approve
          </button>
        </form>
      </td>

      <%-- Reject form with reason --%>
      <td>
        <form method="post" action="${pageContext.request.contextPath}/admin/enrolments"
              onsubmit="return confirm('Reject enrolment #${e.enrolmentId}?');">
          <input type="hidden" name="action" value="reject"/>
          <input type="hidden" name="enrolment_id" value="${e.enrolmentId}"/>
          <input type="text" name="reason" placeholder="Reason (required)" required
                 style="width:180px;margin-right:4px;"/>
          <button type="submit"
                  style="background:#e53935;color:white;border:none;padding:5px 12px;cursor:pointer;border-radius:3px;">
            Reject
          </button>
        </form>
      </td>
    </tr>
  </c:forEach>
  <c:if test="${empty pending}">
    <tr>
      <td colspan="9" style="text-align:center;color:#888;padding:12px;">No pending enrolments.</td>
    </tr>
  </c:if>
</table>

</body>
</html>
