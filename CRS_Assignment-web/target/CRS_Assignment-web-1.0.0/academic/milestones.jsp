<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
  request.setAttribute("pageTitle", "Milestones");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Milestones (Enrolment ID: <c:out value="${enrolmentId}"/>)</h1>

<div class="card">

  <c:if test="${not empty error}">
    <div style="color:red;font-weight:bold;"><c:out value="${error}"/></div>
  </c:if>

  <c:choose>
    <c:when test="${empty milestones}">
      <p class="muted">No milestones found for this enrolment.</p>
    </c:when>
    <c:otherwise>
      <table border="1" cellpadding="6">
        <tr>
          <th>ID</th><th>Title</th><th>Due Date</th><th>Status</th><th>Remarks</th>
        </tr>
        <c:forEach var="m" items="${milestones}">
          <tr>
            <td><c:out value="${m.milestoneId}"/></td>
            <td><c:out value="${m.title}"/></td>
            <td><c:out value="${m.dueDate}"/></td>
            <td><c:out value="${m.status}"/></td>
            <td><c:out value="${m.remarks}"/></td>
          </tr>
        </c:forEach>
      </table>
    </c:otherwise>
  </c:choose>

  <br/>
  <a href="${pageContext.request.contextPath}/academic/recovery_plan?enrolment_id=${enrolmentId}">
    &larr; Back to Recovery Plan
  </a>

</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />
