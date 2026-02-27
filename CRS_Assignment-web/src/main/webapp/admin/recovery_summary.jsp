<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Recovery Summary Report</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; }
        th { background: #f4f4f4; text-align: left; }
        .topbar { display: flex; justify-content: space-between; align-items: center; margin: 10px 0; }
        .btn { padding: 8px 12px; border: 1px solid #333; text-decoration: none; }
    </style>
</head>
<body>

<h2>Student Recovery Summary Report</h2>

<div class="topbar">
    <div>
        <a class="btn" href="${pageContext.request.contextPath}/dashboard.jsp">Back to Dashboard</a>
    </div>
    <div>
        <a class="btn" href="${pageContext.request.contextPath}/admin/recovery_summary?export=csv">Download CSV</a>
    </div>
</div>

<table>
    <thead>
    <tr>
        <th>student_id</th>
        <th>course_code</th>
        <th>attempt_count</th>
        <th>eligibility_status</th>
        <th>enrolment_status</th>
        <th>reject_reason</th>
        <th>recovery_plan_summary</th>
        <th>milestone_count</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="r" items="${rows}">
        <tr>
            <td><c:out value="${r.studentId}" /></td>
            <td><c:out value="${r.courseCode}" /></td>
            <td><c:out value="${r.attemptCount}" /></td>
            <td><c:out value="${r.eligibilityStatus}" /></td>
            <td><c:out value="${r.enrolmentStatus}" /></td>

            <td>
                <c:choose>
                    <c:when test="${empty r.rejectReason}">-</c:when>
                    <c:otherwise><c:out value="${r.rejectReason}" /></c:otherwise>
                </c:choose>
            </td>

            <td>
                <c:choose>
                    <c:when test="${empty r.recoveryPlanSummary}">-</c:when>
                    <c:otherwise><c:out value="${r.recoveryPlanSummary}" /></c:otherwise>
                </c:choose>
            </td>

            <td><c:out value="${r.milestoneCount}" /></td>
        </tr>
    </c:forEach>

    <c:if test="${empty rows}">
        <tr>
            <td colspan="8" style="text-align:center;">No data found</td>
        </tr>
    </c:if>
    </tbody>
</table>

</body>
</html>