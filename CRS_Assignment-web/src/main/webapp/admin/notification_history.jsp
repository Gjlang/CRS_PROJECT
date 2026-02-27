<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
  request.setAttribute("pageTitle", "Notification History");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Notification History</h1>

<div class="card">

  <c:if test="${not empty message}">
    <div style="padding:10px;background:#e7ffe7;border:1px solid #8bc48b;margin-bottom:10px;">${message}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">${error}</div>
  </c:if>

  <h3>Notification History (latest 50)</h3>
  <table border="1" cellpadding="6" cellspacing="0">
    <tr>
      <th>ID</th><th>To</th><th>Type</th><th>Status</th><th>Subject</th><th>Created</th><th>Error</th>
    </tr>
    <c:forEach items="${history}" var="n">
      <tr>
        <td>${n.notificationId}</td>
        <td>${n.recipientEmail}</td>
        <td>${n.type}</td>
        <td>${n.status}</td>
        <td>${n.subject}</td>
        <td>${n.createdAt}</td>
        <td>${n.errorMessage}</td>
      </tr>
    </c:forEach>
  </table>

</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />