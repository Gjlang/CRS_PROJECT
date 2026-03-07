<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
  request.setAttribute("pageTitle", "Admin Student List");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Admin Student List</h1>

<div class="card">
  <p style="margin-top:0;">
    This page lets admin view all students and open a full profile page for each student.
  </p>

  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>

  <table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
    <thead>
      <tr style="background:#f4f4f4;">
        <th>Student ID</th>
        <th>Name</th>
        <th>Major</th>
        <th>Year</th>
        <th>Email</th>
        <th>Status</th>
        <th>Action</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="s" items="${students}">
        <tr>
          <td><c:out value="${s.studentId}" /></td>
          <td><c:out value="${s.fullName}" /></td>
          <td><c:out value="${s.major}" /></td>
          <td><c:out value="${s.year}" /></td>
          <td><c:out value="${s.email}" /></td>
          <td>
            <c:choose>
              <c:when test="${s.active}">ACTIVE</c:when>
              <c:otherwise>INACTIVE</c:otherwise>
            </c:choose>
          </td>
          <td>
            <a href="${pageContext.request.contextPath}/admin/student_profile?student_id=${s.studentId}">
              View Profile
            </a>
          </td>
        </tr>
      </c:forEach>

      <c:if test="${empty students}">
        <tr>
          <td colspan="7" style="text-align:center;">No students found.</td>
        </tr>
      </c:if>
    </tbody>
  </table>
</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />