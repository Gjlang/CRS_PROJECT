<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<h2>Recovery Plan (Enrolment ID: ${enrolmentId})</h2>

<c:if test="${not empty error}">
  <div style="color:red;">${error}</div>
</c:if>

<c:choose>
  <c:when test="${empty plan}">
    <h3>No plan yet</h3>
    <form method="post" action="${pageContext.request.contextPath}/academic/recovery-plan">
      <input type="hidden" name="action" value="createPlan"/>
      <input type="hidden" name="enrolment_id" value="${enrolmentId}"/>

      <label>Recommendation:</label><br/>
      <textarea name="recommendation" rows="4" cols="60" required></textarea><br/><br/>

      <button type="submit">Create Recovery Plan</button>
    </form>
  </c:when>

  <c:otherwise>
    <h3>Plan</h3>
    <form method="post" action="${pageContext.request.contextPath}/academic/recovery-plan">
      <input type="hidden" name="action" value="updatePlan"/>
      <input type="hidden" name="enrolment_id" value="${enrolmentId}"/>

      <textarea name="recommendation" rows="4" cols="60">${plan.recommendation}</textarea><br/><br/>
      <button type="submit">Update Plan</button>
    </form>

    <hr/>

    <h3>Milestones</h3>

    <form method="post" action="${pageContext.request.contextPath}/academic/recovery-plan">
      <input type="hidden" name="action" value="addMilestone"/>
      <input type="hidden" name="enrolment_id" value="${enrolmentId}"/>

      <input name="title" placeholder="Milestone title" required/>
      <input type="date" name="due_date"/>
      <input name="remarks" placeholder="Remarks"/>
      <button type="submit">Add</button>
    </form>

    <br/>

    <table border="1" cellpadding="6">
      <tr>
        <th>Title</th><th>Due</th><th>Status</th><th>Remarks</th><th>Action</th>
      </tr>

      <c:forEach var="m" items="${milestones}">
        <tr>
          <td>${m.title}</td>
          <td>${m.dueDate}</td>
          <td>${m.status}</td>
          <td>${m.remarks}</td>
          <td>
            <form method="post" action="${pageContext.request.contextPath}/academic/recovery-plan" style="display:inline;">
              <input type="hidden" name="action" value="updateMilestone"/>
              <input type="hidden" name="enrolment_id" value="${enrolmentId}"/>
              <input type="hidden" name="milestone_id" value="${m.milestoneId}"/>

              <select name="status">
                <option value="PENDING" ${m.status=='PENDING'?'selected':''}>PENDING</option>
                <option value="DONE" ${m.status=='DONE'?'selected':''}>DONE</option>
              </select>
              <input name="remarks" value="${m.remarks}" />
              <button type="submit">Save</button>
            </form>
          </td>
        </tr>
      </c:forEach>
    </table>
  </c:otherwise>
</c:choose>