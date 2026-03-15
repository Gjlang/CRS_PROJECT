<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  request.setAttribute("pageTitle", "Course Recovery Detail");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Course Recovery Detail</h1>

<div class="card">

  <c:if test="${not empty error}">
    <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">
      ${error}
    </div>
  </c:if>

  <div style="margin-bottom:16px;">
    <strong>Student ID:</strong> ${studentId}<br/>
    <strong>Student Name:</strong> ${studentName}<br/>
    <strong>Course Code:</strong> ${courseCode}<br/>
    <strong>Course Name:</strong> ${courseName}<br/>
    <strong>Attempt No:</strong> ${attemptNo}
  </div>

  <h3>Recovery Components</h3>
  <c:choose>
    <c:when test="${empty recoveryComponents}">
      <div style="padding:10px;border:1px solid #ddd;background:#fafafa;">No recovery components found.</div>
    </c:when>
    <c:otherwise>
      <table border="1" cellpadding="6" cellspacing="0" style="width:100%;border-collapse:collapse;margin-bottom:20px;">
        <tr>
          <th>Assessment ID</th>
          <th>Attempt No</th>
          <th>Grade</th>
          <th>Grade Point</th>
          <th>Failed</th>
        </tr>
        <c:forEach items="${recoveryComponents}" var="r">
          <tr>
            <td>${r.assessmentId}</td>
            <td>${r.attemptNo}</td>
            <td>${r.grade}</td>
            <td>${r.gradePoint}</td>
            <td>
              <c:choose>
                <c:when test="${r.failed}">YES</c:when>
                <c:otherwise>NO</c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
      </table>
    </c:otherwise>
  </c:choose>

  <h3>Recovery Plan Recommendation</h3>
  <c:choose>
    <c:when test="${empty plan}">
      <form method="post" action="${planActionUrl}" style="border:1px solid #ddd;padding:12px;margin-bottom:20px;">
        <input type="hidden" name="action" value="createPlan"/>
        <input type="hidden" name="student_id" value="${studentId}"/>
        <input type="hidden" name="course_code" value="${courseCode}"/>
        <input type="hidden" name="attempt_no" value="${attemptNo}"/>

        <label>Recommendation</label><br/>
        <textarea name="recommendation" rows="4" style="width:100%;" required></textarea><br/><br/>

        <button type="submit">Create Recovery Plan</button>
      </form>
    </c:when>

    <c:otherwise>
      <form method="post" action="${planActionUrl}" style="border:1px solid #ddd;padding:12px;margin-bottom:10px;">
        <input type="hidden" name="action" value="updatePlan"/>
        <input type="hidden" name="student_id" value="${studentId}"/>
        <input type="hidden" name="course_code" value="${courseCode}"/>
        <input type="hidden" name="attempt_no" value="${attemptNo}"/>

        <label>Recommendation</label><br/>
        <textarea name="recommendation" rows="4" style="width:100%;">${plan.recommendation}</textarea><br/><br/>

        <button type="submit">Save Recommendation</button>
      </form>

      <form method="post" action="${planActionUrl}" style="margin-bottom:20px;">
        <input type="hidden" name="action" value="removeRecommendation"/>
        <input type="hidden" name="student_id" value="${studentId}"/>
        <input type="hidden" name="course_code" value="${courseCode}"/>
        <input type="hidden" name="attempt_no" value="${attemptNo}"/>
        <button type="submit">Remove Recommendation</button>
      </form>
    </c:otherwise>
  </c:choose>

  <c:if test="${not empty plan}">
    <h3>Add Milestone</h3>
    <form method="post" action="${planActionUrl}" style="border:1px solid #ddd;padding:12px;margin-bottom:20px;">
      <input type="hidden" name="action" value="addMilestone"/>
      <input type="hidden" name="student_id" value="${studentId}"/>
      <input type="hidden" name="course_code" value="${courseCode}"/>
      <input type="hidden" name="attempt_no" value="${attemptNo}"/>

      <label>Title</label><br/>
      <input name="title" required style="width:320px;"/><br/><br/>

      <label>Task</label><br/>
      <textarea name="task" rows="3" style="width:100%;" required></textarea><br/><br/>

      <label>Due Date</label><br/>
      <input type="date" name="due_date"/><br/><br/>

      <label>Remarks</label><br/>
      <input name="remarks" style="width:320px;"/><br/><br/>

      <button type="submit">Add Milestone</button>
    </form>

    <h3>Milestone Progress Tracking</h3>
    <c:choose>
      <c:when test="${empty milestones}">
        <div style="padding:10px;border:1px solid #ddd;background:#fafafa;">No milestones available.</div>
      </c:when>
      <c:otherwise>
        <table border="1" cellpadding="6" cellspacing="0" style="width:100%;border-collapse:collapse;">
          <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Task</th>
            <th>Due Date</th>
            <th>Status</th>
            <th>Grade</th>
            <th>Remarks</th>
            <th>Update</th>
            <th>Delete</th>
          </tr>

          <c:forEach items="${milestones}" var="m">
            <tr>
              <form method="post" action="${planActionUrl}">
                <td>
                  ${m.milestoneId}
                  <input type="hidden" name="action" value="updateMilestone"/>
                  <input type="hidden" name="milestone_id" value="${m.milestoneId}"/>
                  <input type="hidden" name="student_id" value="${studentId}"/>
                  <input type="hidden" name="course_code" value="${courseCode}"/>
                  <input type="hidden" name="attempt_no" value="${attemptNo}"/>
                </td>

                <td>${m.title}</td>
                <td>${m.task}</td>
                <td>${m.dueDate}</td>

                <td>
                  <select name="status">
                    <option value="PENDING" <c:if test="${m.status == 'PENDING'}">selected</c:if>>PENDING</option>
                    <option value="DONE" <c:if test="${m.status == 'DONE'}">selected</c:if>>DONE</option>
                  </select>
                </td>

                <td>
                  <input name="grade" value="${m.grade}" style="width:90px;"/>
                </td>

                <td>
                  <input name="remarks" value="${m.remarks}" style="width:180px;"/>
                </td>

                <td>
                  <button type="submit">Save</button>
                </td>
              </form>

              <td>
                <form method="post" action="${planActionUrl}" style="margin:0;">
                  <input type="hidden" name="action" value="deleteMilestone"/>
                  <input type="hidden" name="milestone_id" value="${m.milestoneId}"/>
                  <input type="hidden" name="student_id" value="${studentId}"/>
                  <input type="hidden" name="course_code" value="${courseCode}"/>
                  <input type="hidden" name="attempt_no" value="${attemptNo}"/>
                  <button type="submit">Delete</button>
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