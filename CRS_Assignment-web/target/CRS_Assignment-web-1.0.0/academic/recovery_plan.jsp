<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%
  request.setAttribute("pageTitle", "Recovery Plan");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Recovery Plan</h1>

<div class="card">

  <c:if test="${not empty error}">
    <div style="color:red;font-weight:bold;margin-bottom:12px;">
      <c:out value="${error}"/>
    </div>
  </c:if>

  <!-- STEP A: Search student -->
  <form method="get" action="${pageContext.request.contextPath}${pageContext.request.servletPath}" style="margin-bottom:20px;">
    <label>Student ID</label><br/>
    <input type="text" name="student_id" value="${studentId}" placeholder="Enter Student ID" required/>
    <button type="submit">Search Failed Courses</button>
  </form>

  <!-- STEP B: Show failed course attempts -->
  <c:if test="${not empty studentId}">
    <h3>Failed Course Attempts for ${studentId}</h3>

    <c:choose>
      <c:when test="${empty candidates}">
        <p>No failed course attempts found for this student.</p>
      </c:when>
      <c:otherwise>
        <table border="1" cellpadding="6" cellspacing="0" style="margin-bottom:20px;">
          <tr>
            <th>Course Code</th>
            <th>Course Name</th>
            <th>Attempt</th>
            <th>Failed Components</th>
            <th>Action</th>
          </tr>
          <c:forEach var="c" items="${candidates}">
            <tr>
              <td>${c.courseCode}</td>
              <td>${c.courseName}</td>
              <td>${c.attemptNo}</td>
              <td>${c.failedComponentCount}</td>
              <td>
                <a href="${pageContext.request.contextPath}${pageContext.request.servletPath}?student_id=${c.studentId}&course_code=${c.courseCode}&attempt_no=${c.attemptNo}">
                  Manage Recovery Plan
                </a>
              </td>
            </tr>
          </c:forEach>
        </table>
      </c:otherwise>
    </c:choose>
  </c:if>

  <!-- STEP C: Show selected course recovery plan -->
  <c:if test="${not empty courseCode and not empty attemptNo}">
    <hr/>
    <h3>Selected Course: ${courseCode} (Attempt ${attemptNo})</h3>

    <c:choose>
      <c:when test="${empty plan}">
        <h4>No recovery plan yet</h4>
        <form method="post" action="${planActionUrl}">
          <input type="hidden" name="action" value="createPlan"/>
          <input type="hidden" name="student_id" value="${studentId}"/>
          <input type="hidden" name="course_code" value="${courseCode}"/>
          <input type="hidden" name="attempt_no" value="${attemptNo}"/>

          <label>Recommendation</label><br/>
          <textarea name="recommendation" rows="4" cols="60" required></textarea><br/><br/>
          <button type="submit">Create Recovery Plan</button>
        </form>
      </c:when>

      <c:otherwise>
        <h4>Recovery Plan ID: ${plan.planId}</h4>

        <form method="post" action="${planActionUrl}" style="margin-bottom:20px;">
          <input type="hidden" name="action" value="updatePlan"/>
          <input type="hidden" name="student_id" value="${studentId}"/>
          <input type="hidden" name="course_code" value="${courseCode}"/>
          <input type="hidden" name="attempt_no" value="${attemptNo}"/>

          <label>Recommendation</label><br/>
          <textarea name="recommendation" rows="4" cols="60"><c:out value="${plan.recommendation}"/></textarea><br/><br/>
          <button type="submit">Update Recommendation</button>
        </form>

        <h4>Add Milestone</h4>
        <form method="post" action="${planActionUrl}" style="margin-bottom:20px;">
          <input type="hidden" name="action" value="addMilestone"/>
          <input type="hidden" name="student_id" value="${studentId}"/>
          <input type="hidden" name="course_code" value="${courseCode}"/>
          <input type="hidden" name="attempt_no" value="${attemptNo}"/>

          <input name="title" placeholder="Milestone title" required/>
          <input name="task" placeholder="Task / Action plan" required/>
          <input type="date" name="due_date"/>
          <input name="remarks" placeholder="Remarks"/>
          <button type="submit">Add Milestone</button>
        </form>

        <h4>Milestones</h4>
        <table border="1" cellpadding="6" cellspacing="0">
          <tr>
            <th>Title</th>
            <th>Task</th>
            <th>Due Date</th>
            <th>Status</th>
            <th>Remarks</th>
            <th>Action</th>
          </tr>
          <c:forEach var="m" items="${milestones}">
            <tr>
              <td>${m.title}</td>
              <td>${m.task}</td>
              <td>${m.dueDate}</td>
              <td>${m.status}</td>
              <td>${m.remarks}</td>
              <td>
                <form method="post" action="${planActionUrl}">
                  <input type="hidden" name="action" value="updateMilestone"/>
                  <input type="hidden" name="student_id" value="${studentId}"/>
                  <input type="hidden" name="course_code" value="${courseCode}"/>
                  <input type="hidden" name="attempt_no" value="${attemptNo}"/>
                  <input type="hidden" name="milestone_id" value="${m.milestoneId}"/>

                  <select name="status">
                    <option value="PENDING" <c:if test="${m.status == 'PENDING'}">selected</c:if>>PENDING</option>
                    <option value="DONE" <c:if test="${m.status == 'DONE'}">selected</c:if>>DONE</option>
                  </select>
                  <input name="remarks" value="${fn:escapeXml(m.remarks)}"/>
                  <button type="submit">Save</button>
                </form>
              </td>
            </tr>
          </c:forEach>
        </table>
      </c:otherwise>
    </c:choose>
  </c:if>

</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />