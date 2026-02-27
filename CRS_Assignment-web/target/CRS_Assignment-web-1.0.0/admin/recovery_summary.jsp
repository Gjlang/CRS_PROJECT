<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
  request.setAttribute("pageTitle", "Student Recovery Summary Report");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Student Recovery Summary Report</h1>

<div class="card">

  <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">
    <div>
      <a href="${pageContext.request.contextPath}/admin/recovery_summary?export=csv"
         style="padding:8px 12px;border:1px solid #333;text-decoration:none;">Download CSV</a>
    </div>
  </div>

  <table border="1" cellpadding="6" cellspacing="0" style="border-collapse:collapse;width:100%;">
    <thead>
      <tr style="background:#f4f4f4;">
        <th>student_id</th>
        <th>course_code</th>
        <th>attempt_count</th>
        <th>eligibility_status</th>
        <th>enrolment_status</th>
        <th>reject_reason</th>
        <th>recovery_plan_summary</th>
        <th>milestone_count</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="r" items="${rows}">
        <tr>
          <td><c:out value="${r.studentId}" /></td>
          <td><c:out value="${r.courseCode}" /></td>
          <td><c:out value="${r.attemptCount}" /></td>
          <td><c:out value="${r.eligibilityStatus}" /></td>
          <td><c:out value="${r.enrolmentStatus}" /></td>
          <td><c:choose><c:when test="${empty r.rejectReason}">-</c:when><c:otherwise><c:out value="${r.rejectReason}" /></c:otherwise></c:choose></td>
          <td><c:choose><c:when test="${empty r.recoveryPlanSummary}">-</c:when><c:otherwise><c:out value="${r.recoveryPlanSummary}" /></c:otherwise></c:choose></td>
          <td><c:out value="${r.milestoneCount}" /></td>
        </tr>
      </c:forEach>
      <c:if test="${empty rows}">
        <tr>
          <td colspan="8" style="text-align:center;">No data found</td>
        </tr>
      </c:if>
    </tbody>
  </table>

</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />