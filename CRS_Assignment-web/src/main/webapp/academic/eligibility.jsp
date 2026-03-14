<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
  request.setAttribute("pageTitle", "Eligibility Check");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Eligibility Check and Progression</h1>

<div class="card">
  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>

  <c:if test="${not empty message}">
    <div class="alert alert-success">${message}</div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/academic/eligibility" style="margin-bottom:20px;">
    <label>Check Student ID</label><br/>
    <input name="student_id" value="${studentId}" required style="width:220px;" />
    <button type="submit" class="btn btn-primary">Check</button>
  </form>

  <c:if test="${not empty result}">
    <div style="border:1px solid #ccc; padding:16px; margin-bottom:24px; background:#f9f9f9;">
      <h3>Eligibility Result</h3>
      <p><b>Student ID:</b> ${result.studentId}</p>
      <p><b>CGPA:</b> ${result.cgpa}</p>
      <p><b>Failed Courses:</b> ${result.failedCourseCount}</p>
      <p>
        <b>Status:</b>
        <c:choose>
          <c:when test="${result.eligible}">
            <span style="color:green;font-weight:bold;">ELIGIBLE TO PROGRESS</span>
          </c:when>
          <c:otherwise>
            <span style="color:red;font-weight:bold;">NOT ELIGIBLE TO PROGRESS</span>
          </c:otherwise>
        </c:choose>
      </p>

      <h4>Reasons</h4>
      <ul>
        <c:forEach var="reason" items="${result.reasons}">
          <li>${reason}</li>
        </c:forEach>
      </ul>
    </div>
  </c:if>

  <h3>Students Not Eligible to Progress</h3>
  <table border="1" cellpadding="6" cellspacing="0" style="width:100%;border-collapse:collapse;margin-bottom:24px;">
    <thead>
      <tr>
        <th>Student ID</th>
        <th>CGPA</th>
        <th>Failed Courses</th>
        <th>Reasons</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="r" items="${notEligibleList}">
        <tr>
          <td>${r.studentId}</td>
          <td>${r.cgpa}</td>
          <td>${r.failedCourseCount}</td>
          <td>
            <c:forEach var="reason" items="${r.reasons}">
              <div>${reason}</div>
            </c:forEach>
          </td>
        </tr>
      </c:forEach>
      <c:if test="${empty notEligibleList}">
        <tr>
          <td colspan="4" style="text-align:center;">No ineligible students found.</td>
        </tr>
      </c:if>
    </tbody>
  </table>
</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />
