<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
  request.setAttribute("pageTitle", "Enrolment Request");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Enrolment Request</h1>

<div class="card">

  <c:if test="${not empty error}">
    <div style="color:red;">${error}</div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/academic/enrolments">
    <input name="student_id" placeholder="Student ID" required/>
    <input name="course_code" placeholder="Course Code" required/>
    <button type="submit">Create Request</button>
  </form>

  <hr/>

  <h3>My Requests</h3>
  <table border="1" cellpadding="6">
    <tr>
      <th>ID</th><th>Student</th><th>Course</th><th>Attempt</th>
      <th>Eligibility</th><th>Status</th><th>Reject Reason</th><th>Action</th>
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
        <td>
          <c:if test="${e.enrolmentStatus == 'APPROVED'}">
            <a href="${pageContext.request.contextPath}/academic/recovery-plan?enrolment_id=${e.enrolmentId}">
              Recovery Plan
            </a>
          </c:if>
        </td>
      </tr>
    </c:forEach>
  </table>

</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />