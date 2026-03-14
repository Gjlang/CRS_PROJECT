<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    request.setAttribute("pageTitle", "Recovery Plan");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Recovery Plan List</h1>

<div class="card">
    <form method="get" action="${pageContext.request.contextPath}${basePath}/recovery_plan" style="margin-bottom:16px;">
        <input type="text" name="q" value="${q}" placeholder="Search by student ID or name" />
        <button type="submit" class="btn btn-primary">Search</button>
    </form>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">
            <c:out value="${error}" />
        </div>
    </c:if>

    <c:choose>
        <c:when test="${empty students}">
            <div class="alert alert-info">No recovery candidates found.</div>
        </c:when>
        <c:otherwise>
            <div class="tableWrap">
                <table>
                    <thead>
                    <tr>
                        <th>Student ID</th>
                        <th>Student Name</th>
                        <th>Failed Courses Count</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="s" items="${students}">
                        <tr>
                            <td>${s.studentId}</td>
                            <td>${s.studentName}</td>
                            <td>${s.failedCoursesCount}</td>
                            <td>
                                <a class="btn btn-primary"
                                   href="${pageContext.request.contextPath}${basePath}/student_recovery_detail?student_id=${s.studentId}">
                                    View Student Recovery
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />