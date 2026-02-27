<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    // Page title for layout
    request.setAttribute("pageTitle", "Dashboard");
%>

<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Dashboard</h1>

<!-- Success Message -->
<c:if test="${not empty message}">
    <div class="alert alert-success">
        ${message}
    </div>
</c:if>

<!-- Error Message -->
<c:if test="${not empty error}">
    <div class="alert alert-danger">
        ${error}
    </div>
</c:if>

<div class="card">
    <div class="cardHeader">
        <div>
            <strong>Welcome, ${sessionScope.name}</strong>
            <div class="muted">
                Role: <span class="badge">${sessionScope.role}</span>
            </div>
        </div>
    </div>

    <p class="muted" style="margin-top:8px;">
        Select a module from the sidebar navigation to continue.
    </p>
</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />