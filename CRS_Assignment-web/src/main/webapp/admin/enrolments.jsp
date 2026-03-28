<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    request.setAttribute("pageTitle", "Progression Registration Approval");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Progression Registration Approval</h1>

<div class="card">
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <c:if test="${not empty message}">
        <div class="alert alert-success">${message}</div>
    </c:if>

    <h3>All Progression Registration Requests</h3>

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
                <th>Decided By</th>
                <th>Decided At</th>
                <th>Reject Reason</th>
                <th>Action</th>
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
                    <td>${r.decidedByUserId}</td>
                    <td>${r.decidedAt}</td>
                    <td>${r.rejectReason}</td>
                    <td>
                        <c:choose>
                            <c:when test="${r.registrationStatus == 'PENDING'}">
                                <form method="post" action="${pageContext.request.contextPath}/admin/enrolments" style="margin-bottom:6px;">
                                    <input type="hidden" name="registration_id" value="${r.registrationId}" />
                                    <input type="hidden" name="action" value="approve" />
                                    <button type="submit">Approve</button>
                                </form>

                                <form method="post" action="${pageContext.request.contextPath}/admin/enrolments">
                                    <input type="hidden" name="registration_id" value="${r.registrationId}" />
                                    <input type="hidden" name="action" value="reject" />
                                    <input type="text" name="reject_reason" placeholder="Reject reason" required style="width:140px;" />
                                    <button type="submit">Reject</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <span>No action</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty registrations}">
                <tr>
                    <td colspan="12" style="text-align:center;">No progression registrations found.</td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />