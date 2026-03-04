<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
  request.setAttribute("pageTitle", "Recovery Plan");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Recovery Plan (Enrolment ID: <c:out value="${enrolmentId}"/>)</h1>

<div class="card">

  <c:if test="${not empty error}">
    <div style="color:red;font-weight:bold;"><c:out value="${error}"/></div>
  </c:if>
  <c:if test="${not empty message}">
    <div style="color:green;"><c:out value="${message}"/></div>
  </c:if>

  <c:choose>
    <c:when test="${empty plan}">
      <h3>No plan yet</h3>
      <form method="post" action="${planActionUrl}">
        <input type="hidden" name="action" value="createPlan"/>
        <input type="hidden" name="enrolment_id" value="${enrolmentId}"/>
        <label>Recommendation:</label><br/>
        <textarea name="recommendation" rows="4" cols="60" required></textarea><br/><br/>
        <button type="submit">Create Recovery Plan</button>
      </form>
    </c:when>

    <c:otherwise>
      <h3>Plan (ID: <c:out value="${plan.planId}"/>)</h3>
      <form method="post" action="${planActionUrl}">
        <input type="hidden" name="action" value="updatePlan"/>
        <input type="hidden" name="enrolment_id" value="${enrolmentId}"/>
        <textarea name="recommendation" rows="4" cols="60"><c:out value="${plan.recommendation}"/></textarea><br/><br/>
        <button type="submit">Update Plan</button>
      </form>

      <hr/>

      <h3>Milestones</h3>
      <form method="post" action="${planActionUrl}">
        <input type="hidden" name="action" value="addMilestone"/>
        <input type="hidden" name="enrolment_id" value="${enrolmentId}"/>
        <input name="title" placeholder="Milestone title" required/>
        <input name="task" placeholder="Task / Activity" required/>
        <input type="date" name="due_date"/>
        <input name="remarks" placeholder="Remarks"/>
        <button type="submit">Add</button>
      </form>

      <br/>

      <table border="1" cellpadding="6">
        <tr>
          <th>Title</th><th>Task</th><th>Due</th><th>Status</th><th>Remarks</th><th>Action</th>
        </tr>
        <c:forEach var="m" items="${milestones}">
          <tr>
            <td><c:out value="${m.title}"/></td>
            <td><c:out value="${m.task}"/></td>
            <td><c:out value="${m.dueDate}"/></td>
            <td><c:out value="${m.status}"/></td>
            <td><c:out value="${m.remarks}"/></td>
            <td>
              <form method="post" action="${planActionUrl}" style="display:inline;">
                <input type="hidden" name="action" value="updateMilestone"/>
                <input type="hidden" name="enrolment_id" value="${enrolmentId}"/>
                <input type="hidden" name="milestone_id" value="${m.milestoneId}"/>
                <select name="status">
                  <option value="PENDING" <c:if test="${m.status == 'PENDING'}">selected</c:if>>PENDING</option>
                  <option value="DONE"    <c:if test="${m.status == 'DONE'}">selected</c:if>>DONE</option>
                </select>
                <input name="remarks" value="<c:out value="${m.remarks}"/>"/>
                <button type="submit">Save</button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </table>
    </c:otherwise>
  </c:choose>

</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />
