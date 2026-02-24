<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Academic - Recovery Plan</title>
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

<h2>Academic - Recovery Plan</h2>

<c:if test="${not empty message}">
  <div style="padding:10px;background:#e7ffe7;border:1px solid #8bc48b;margin-bottom:10px;">${message}</div>
</c:if>
<c:if test="${not empty error}">
  <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">${error}</div>
</c:if>


<form method="post" action="${pageContext.request.contextPath}/academic/recovery_plan" style="border:1px solid #ddd;padding:12px;margin-bottom:20px;">
  <input type="hidden" name="action" value="load"/>
  <label>Student ID</label><br/>
  <input name="student_id" required style="width:220px;"/><br/><br/>
  <label>Course Code</label><br/>
  <input name="course_code" required style="width:220px;"/><br/><br/>
  <label>Attempt No (1-3)</label><br/>
  <input name="attempt_no" type="number" min="1" max="3" value="2" required style="width:80px;"/><br/><br/>
  <button type="submit">Load / Create Plan</button>
</form>

<c:if test="${not empty plan}">
  <h3>Plan Details</h3>
  <p>Plan ID: <b>${plan.planId}</b> | Student: <b>${plan.studentId}</b> | Course: <b>${plan.courseCode}</b> | Attempt: <b>${plan.attemptNo}</b></p>

  <form method="post" action="${pageContext.request.contextPath}/academic/recovery_plan" style="border:1px solid #ddd;padding:12px;margin:10px 0;">
    <input type="hidden" name="action" value="update_recommendation"/>
    <input type="hidden" name="plan_id" value="${plan.planId}"/>
    <label>Recommendation</label><br/>
    <textarea name="recommendation" rows="6" style="width:700px;">${plan.recommendation}</textarea><br/><br/>
    <button type="submit">Update Recommendation</button>
  </form>

  <h3>Milestones</h3>
  <form method="post" action="${pageContext.request.contextPath}/academic/milestones" style="border:1px solid #ddd;padding:12px;margin-bottom:10px;">
    <input type="hidden" name="action" value="add"/>
    <input type="hidden" name="plan_id" value="${plan.planId}"/>
    <label>Study Week</label>
    <input name="study_week" type="number" min="1" required style="width:80px;"/>
    <label style="margin-left:10px;">Task</label>
    <input name="task" required style="width:360px;"/>
    <label style="margin-left:10px;">Due Date</label>
    <input name="due_date" type="date"/>
    <button type="submit" style="margin-left:10px;">Add</button>
  </form>

  <table border="1" cellpadding="6" cellspacing="0">
    <tr><th>ID</th><th>Week</th><th>Task</th><th>Due</th><th>Status</th><th>Grade</th><th>Actions</th></tr>
    <c:forEach items="${milestones}" var="m">
      <tr>
        <td>${m.milestoneId}</td>
        <td>${m.studyWeek}</td>
        <td>${m.task}</td>
        <td>${m.dueDate}</td>
        <td>${m.status}</td>
        <td>${m.grade}</td>
        <td>
          <form method="post" action="${pageContext.request.contextPath}/academic/milestones" style="display:inline;">
            <input type="hidden" name="action" value="done"/>
            <input type="hidden" name="plan_id" value="${plan.planId}"/>
            <input type="hidden" name="milestone_id" value="${m.milestoneId}"/>
            <input name="grade" placeholder="Grade" style="width:80px;"/>
            <button type="submit">Mark Done</button>
          </form>
          <form method="post" action="${pageContext.request.contextPath}/academic/milestones" style="display:inline;">
            <input type="hidden" name="action" value="delete"/>
            <input type="hidden" name="plan_id" value="${plan.planId}"/>
            <input type="hidden" name="milestone_id" value="${m.milestoneId}"/>
            <button type="submit">Delete</button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </table>
</c:if>

</body>
</html>
