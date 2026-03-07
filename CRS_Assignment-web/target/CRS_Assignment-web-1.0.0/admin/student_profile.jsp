<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
  request.setAttribute("pageTitle", "Admin Student Profile");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Admin Student Profile</h1>

<div style="margin-bottom:16px;">
  <a href="${pageContext.request.contextPath}/admin/students">&larr; Back to Student List</a>
</div>

<c:if test="${not empty error}">
  <div class="card">
    <div class="alert alert-danger">${error}</div>
  </div>
</c:if>

<c:if test="${not empty profile}">
  <!-- Section 1 -->
  <div class="card" style="margin-bottom:16px;">
    <h2 style="margin-top:0;">1. Student Details</h2>
    <table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
      <tbody>
        <tr>
          <th style="width:220px; background:#f4f4f4;">Student ID</th>
          <td><c:out value="${profile.studentId}" /></td>
        </tr>
        <tr>
          <th style="background:#f4f4f4;">Name</th>
          <td><c:out value="${profile.fullName}" /></td>
        </tr>
        <tr>
          <th style="background:#f4f4f4;">Major</th>
          <td><c:out value="${profile.major}" /></td>
        </tr>
        <tr>
          <th style="background:#f4f4f4;">Year</th>
          <td><c:out value="${profile.year}" /></td>
        </tr>
        <tr>
          <th style="background:#f4f4f4;">Email</th>
          <td><c:out value="${profile.email}" /></td>
        </tr>
        <tr>
          <th style="background:#f4f4f4;">Active Status</th>
          <td>
            <c:choose>
              <c:when test="${profile.active}">ACTIVE</c:when>
              <c:otherwise>INACTIVE</c:otherwise>
            </c:choose>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <!-- Section 2 -->
  <div class="card" style="margin-bottom:16px;">
    <h2 style="margin-top:0;">2. Enrolled Courses</h2>
    <table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
      <thead>
        <tr style="background:#f4f4f4;">
          <th>Course Code</th>
          <th>Course Name</th>
          <th>Attempt No</th>
          <th>Eligibility Status</th>
          <th>Enrolment Status</th>
          <th>Created At</th>
          <th>Reject Reason</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="e" items="${profile.enrolments}">
          <tr>
            <td><c:out value="${e.courseCode}" /></td>
            <td><c:out value="${e.courseName}" /></td>
            <td><c:out value="${e.attemptNo}" /></td>
            <td><c:out value="${e.eligibilityStatus}" /></td>
            <td><c:out value="${e.enrolmentStatus}" /></td>
            <td><c:out value="${e.createdAt}" /></td>
            <td>
              <c:choose>
                <c:when test="${empty e.rejectReason}">-</c:when>
                <c:otherwise><c:out value="${e.rejectReason}" /></c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty profile.enrolments}">
          <tr>
            <td colspan="7" style="text-align:center;">No enrolments found.</td>
          </tr>
        </c:if>
      </tbody>
    </table>
  </div>

  <!-- Section 3 -->
  <div class="card" style="margin-bottom:16px;">
    <h2 style="margin-top:0;">3. Grades / Results</h2>
    <table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
      <thead>
        <tr style="background:#f4f4f4;">
          <th>Course</th>
          <th>Assessment Component</th>
          <th>Grade</th>
          <th>Grade Point</th>
          <th>Pass / Fail</th>
          <th>Attempt No</th>
          <th>Semester</th>
          <th>Year</th>
          <th>Year of Study</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="r" items="${profile.results}">
          <tr>
            <td>
              <c:out value="${r.courseCode}" />
              -
              <c:out value="${r.courseName}" />
            </td>
            <td><c:out value="${r.assessmentComponent}" /></td>
            <td><c:out value="${r.grade}" /></td>
            <td><c:out value="${r.gradePoint}" /></td>
            <td>
              <c:choose>
                <c:when test="${r.failed}">FAILED</c:when>
                <c:otherwise>PASSED</c:otherwise>
              </c:choose>
            </td>
            <td><c:out value="${r.attemptNo}" /></td>
            <td><c:out value="${r.semester}" /></td>
            <td><c:out value="${r.year}" /></td>
            <td><c:out value="${r.yearOfStudy}" /></td>
          </tr>
        </c:forEach>
        <c:if test="${empty profile.results}">
          <tr>
            <td colspan="9" style="text-align:center;">No results found.</td>
          </tr>
        </c:if>
      </tbody>
    </table>
  </div>

  <!-- Section 4 -->
  <div class="card" style="margin-bottom:16px;">
    <h2 style="margin-top:0;">4. Recovery Plans</h2>
    <table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
      <thead>
        <tr style="background:#f4f4f4;">
          <th>Plan ID</th>
          <th>Enrolment ID</th>
          <th>Course</th>
          <th>Attempt No</th>
          <th>Recommendation</th>
          <th>Milestone Count</th>
          <th>Created At</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="rp" items="${profile.recoveryPlans}">
          <tr>
            <td><c:out value="${rp.planId}" /></td>
            <td><c:out value="${rp.enrolmentId}" /></td>
            <td>
              <c:out value="${rp.courseCode}" />
              -
              <c:out value="${rp.courseName}" />
            </td>
            <td><c:out value="${rp.attemptNo}" /></td>
            <td><c:out value="${rp.recommendation}" /></td>
            <td><c:out value="${rp.milestoneCount}" /></td>
            <td><c:out value="${rp.createdAt}" /></td>
          </tr>
        </c:forEach>
        <c:if test="${empty profile.recoveryPlans}">
          <tr>
            <td colspan="7" style="text-align:center;">No recovery plans found.</td>
          </tr>
        </c:if>
      </tbody>
    </table>
  </div>

  <!-- Section 5 -->
  <div class="card" style="margin-bottom:16px;">
    <h2 style="margin-top:0;">5. Milestones</h2>
    <table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
      <thead>
        <tr style="background:#f4f4f4;">
          <th>Course</th>
          <th>Study Week</th>
          <th>Title</th>
          <th>Task</th>
          <th>Due Date</th>
          <th>Progress Status</th>
          <th>Grade</th>
          <th>Remarks</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="m" items="${profile.milestones}">
          <tr>
            <td>
              <c:out value="${m.courseCode}" />
              -
              <c:out value="${m.courseName}" />
            </td>
            <td><c:out value="${m.studyWeek}" /></td>
            <td><c:out value="${m.title}" /></td>
            <td><c:out value="${m.task}" /></td>
            <td><c:out value="${m.dueDate}" /></td>
            <td><c:out value="${m.status}" /></td>
            <td><c:out value="${m.grade}" /></td>
            <td><c:out value="${m.remarks}" /></td>
          </tr>
        </c:forEach>
        <c:if test="${empty profile.milestones}">
          <tr>
            <td colspan="8" style="text-align:center;">No milestones found.</td>
          </tr>
        </c:if>
      </tbody>
    </table>
  </div>
</c:if>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />