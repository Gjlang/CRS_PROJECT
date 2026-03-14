<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
  request.setAttribute("pageTitle", "Eligibility / Enrolment");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Eligibility / Enrolment</h1>

<div class="card">
  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>

  <c:if test="${not empty message}">
    <div class="alert alert-success">${message}</div>
  </c:if>

  <p>This module is for progression / next-level registration only. Recovery Plan is handled in the Recovery Plan module for failed courses.</p>

  <form method="post" action="${pageContext.request.contextPath}/academic/enrolment" style="margin-bottom:20px;">
    <input name="student_id" placeholder="Student ID" required style="width:180px;" value="${param.student_id}" />
    <input name="course_code" placeholder="Course Code" required style="width:180px;" value="${param.course_code}" />
    <input name="attempt_no" type="number" min="1" max="3" placeholder="Attempt" required style="width:100px;" value="${param.attempt_no}" />
    <button type="submit">Create Enrolment Record</button>
  </form>

  <hr/>

  <h3>My Records</h3>
  <table border="1" cellpadding="6" cellspacing="0" style="width:100%;border-collapse:collapse;">
    <thead>
      <tr>
        <th>ID</th>
        <th>Student</th>
        <th>Course</th>
        <th>Attempt</th>
        <th>Eligibility</th>
        <th>Status</th>
        <th>Reject Reason</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="r" items="${records}">
        <tr>
          <td>${r.enrolmentId}</td>
          <td>${r.studentId}</td>
          <td>${r.courseCode}</td>
          <td>${r.attemptNo}</td>
          <td>${r.eligibilityStatus}</td>
          <td>${r.enrolmentStatus}</td>
          <td>${r.rejectReason}</td>
        </tr>
      </c:forEach>
      <c:if test="${empty records}">
        <tr>
          <td colspan="7" style="text-align:center;">No enrolment records found.</td>
        </tr>
      </c:if>
    </tbody>
  </table>
</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />
