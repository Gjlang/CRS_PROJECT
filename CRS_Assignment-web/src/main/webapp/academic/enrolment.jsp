<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
  request.setAttribute("pageTitle", "Eligibility / Enrolment");
  request.setAttribute("activePage", "academic_enrolments");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Eligibility / Enrolment</h1>

<div class="card">

  <c:if test="${not empty error}">
    <div style="color:red; margin-bottom:12px;">${error}</div>
  </c:if>

  <div style="margin-bottom:12px; color:#555;">
    This module is for progression / enrolment tracking only.  
    Recovery Plan is now handled directly from the Recovery Plan module for failed courses.
  </div>

  <form method="post" action="${pageContext.request.contextPath}/academic/enrolments" style="display:flex; gap:10px; flex-wrap:wrap;">
    <input name="student_id" placeholder="Student ID" required/>
    <input name="course_code" placeholder="Course Code" required/>
    <input name="attempt_no" type="number" min="1" max="3" placeholder="Attempt No" required/>
    <button type="submit">Create Enrolment Record</button>
  </form>

  <hr/>

  <h3>My Records</h3>
  <table border="1" cellpadding="6">
    <tr>
      <th>ID</th>
      <th>Student</th>
      <th>Course</th>
      <th>Attempt</th>
      <th>Eligibility</th>
      <th>Status</th>
      <th>Reject Reason</th>
    </tr>

    <c:forEach var="e" items="${requests}">
      <tr>
        <td>${e.enrolmentId}</td>
        <td>${e.studentId}</td>
        <td>${e.courseCode}</td>
        <td>${e.attemptNo}</td>
        <td>${e.eligibilityStatus}</td>
        <td>${e.enrolmentStatus}</td>
        <td>${e.rejectReason}</td>
      </tr>
    </c:forEach>
  </table>

</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />