<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
  request.setAttribute("pageTitle", "Student Recovery Detail");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Student Recovery Detail</h1>

<div class="card">
  <div style="margin-bottom:16px;">
    <a class="btn btn-ghost" href="${pageContext.request.contextPath}${basePath}/recovery_plan">← Back to Recovery Plan List</a>
  </div>

  <c:if test="${not empty error}">
    <div class="alert alert-danger">
      <c:out value="${error}"/>
    </div>
  </c:if>

  <c:if test="${not empty studentId}">
    <div class="selectedInfo">
      <div><b>Student ID:</b> ${studentId}</div>
      <div><b>Student Name:</b> ${studentName}</div>
    </div>
  </c:if>

  <h3>Failed Courses</h3>

  <c:choose>
    <c:when test="${empty courses}">
      <div class="alert alert-info">No failed courses found for this student.</div>
    </c:when>
    <c:otherwise>
      <div class="tableWrap">
        <table>
          <thead>
            <tr>
              <th>Course Code</th>
              <th>Course Name</th>
              <th>Attempt</th>
              <th>Failed Components</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="c" items="${courses}">
              <tr>
                <td>${c.courseCode}</td>
                <td>${c.courseName}</td>
                <td>${c.attemptNo}</td>
                <td>${c.failedComponentCount}</td>
                <td>
                  <a class="btn btn-primary"
                     href="${pageContext.request.contextPath}${basePath}/course_recovery_detail?student_id=${c.studentId}&course_code=${c.courseCode}&attempt_no=${c.attemptNo}">
                    Manage Course Recovery
                  </a>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />