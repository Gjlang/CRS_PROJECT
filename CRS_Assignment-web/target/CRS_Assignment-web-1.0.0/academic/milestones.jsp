<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%
  request.setAttribute("pageTitle", "Milestones");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Milestones</h1>

<div class="card">

  <c:if test="${not empty message}">
    <div style="padding:10px;background:#e7ffe7;border:1px solid #8bc48b;margin-bottom:10px;">${message}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">${error}</div>
  </c:if>

  <p>Use Recovery Plan page to manage milestones.</p>

</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />