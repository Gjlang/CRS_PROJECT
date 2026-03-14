<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    request.setAttribute("pageTitle", "Progression Registration Monitoring");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Progression Registration Monitoring</h1>

<div class="card">
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <h3>All Progression Registrations</h3>

    <table border="1" cellpadding="6" cellspacing="0" style="width:100%;border-collapse:collapse;">
        <thead>
            <tr>
                <th>ID</th>
                <th>Student</th>
                <th>CGPA</th>
                <th>Failed Courses</th>
                <th>Eligibility</th>
                <th>Status</th>
                <th>Submitted By</th>
                <th>Created At</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="r" items="${registrations}">
                <tr>
                    <td>${r.registrationId}</td>
                    <td>${r.studentId}</td>
                    <td>${r.cgpa}</td>
                    <td>${r.failedCourseCount}</td>
                    <td>${r.eligibilityStatus}</td>
                    <td>${r.registrationStatus}</td>
                    <td>${r.createdByUserId}</td>
                    <td>${r.createdAt}</td>
                </tr>
            </c:forEach>

            <c:if test="${empty registrations}">
                <tr>
                    <td colspan="8" style="text-align:center;">No progression registrations found.</td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />
