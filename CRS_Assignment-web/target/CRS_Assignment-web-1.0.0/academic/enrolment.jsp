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

  <p>
    This module is for progression / next-level registration only.
    The system evaluates the student using overall CGPA and total failed courses.
    If eligible, the Academic Officer submits the request first, then the Course Administrator approves or rejects it.
  </p>

  <form method="post" action="${pageContext.request.contextPath}/academic/enrolment" style="margin-bottom:20px;">
    <input name="student_id"
           placeholder="Student ID"
           required
           style="width:220px;"
           value="${param.student_id}" />
    <button type="submit">Submit for Admin Approval</button>
  </form>

  <hr/>

  <h3>My Requests</h3>
  <table border="1" cellpadding="6" cellspacing="0" style="width:100%;border-collapse:collapse;">
    <thead>
      <tr>
        <th>ID</th>
        <th>Student</th>
        <th>CGPA</th>
        <th>Failed Courses</th>
        <th>Eligibility</th>
        <th>Status</th>
        <th>Decision Time</th>
        <th>Reject Reason</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="r" items="${records}">
        <tr>
          <td>${r.registrationId}</td>
          <td>${r.studentId}</td>
          <td>${r.cgpa}</td>
          <td>${r.failedCourseCount}</td>
          <td>${r.eligibilityStatus}</td>
          <td>${r.registrationStatus}</td>
          <td>${r.decidedAt}</td>
          <td>${r.rejectReason}</td>
        </tr>
      </c:forEach>
      <c:if test="${empty records}">
        <tr>
          <td colspan="8" style="text-align:center;">No progression registration records found.</td>
        </tr>
      </c:if>
    </tbody>
  </table>
</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />