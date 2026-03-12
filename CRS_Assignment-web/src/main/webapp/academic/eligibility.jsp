<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
  request.setAttribute("pageTitle", "Eligibility Check");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Eligibility Check</h1>

<div class="card">

  <c:if test="${not empty error}">
    <div style="color:red;">${error}</div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/academic/eligibility">
    <input name="student_id" placeholder="Student ID" value="${studentId}" required/>
    <button type="submit">Check</button>
  </form>

  <c:if test="${not empty result}">
    <hr/>
    <h3>Result:
      <span style="font-weight:bold;">
        <c:choose>
          <c:when test="${result.eligible}">PASS</c:when>
          <c:otherwise>FAIL</c:otherwise>
        </c:choose>
      </span>
    </h3>

    <p>CGPA: ${result.cgpa}</p>
    <p>Failed Courses: ${result.failedCourseCount}</p>

    <c:if test="${not empty result.reasons}">
      <p><b>Reasons:</b></p>
      <ul>
        <c:forEach var="r" items="${result.reasons}">
          <li>${r}</li>
        </c:forEach>
      </ul>
    </c:if>

    <c:if test="${not empty result.grades}">
      <h3>Student Grades</h3>
      <table border="1" cellpadding="6" cellspacing="0">
        <tr>
          <th>Course Code</th>
          <th>Course Name</th>
          <th>Component</th>
          <th>Grade</th>
          <th>Grade Point</th>
          <th>Attempt</th>
          <th>Semester</th>
          <th>Year</th>
          <th>Year of Study</th>
          <th>Status</th>
        </tr>
        <c:forEach var="g" items="${result.grades}">
          <tr>
            <td>${g.courseCode}</td>
            <td>${g.courseName}</td>
            <td>${g.assessmentComponent}</td>
            <td>${g.grade}</td>
            <td>${g.gradePoint}</td>
            <td>${g.attemptNo}</td>
            <td>${g.semester}</td>
            <td>${g.year}</td>
            <td>${g.yearOfStudy}</td>
            <td>
              <c:choose>
                <c:when test="${g.failed}">FAILED</c:when>
                <c:otherwise>PASSED</c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </c:if>

</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />