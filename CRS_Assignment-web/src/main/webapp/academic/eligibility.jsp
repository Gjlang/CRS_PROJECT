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
  </c:if>

</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />