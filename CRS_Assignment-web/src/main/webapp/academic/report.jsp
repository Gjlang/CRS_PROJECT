<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%
  request.setAttribute("pageTitle", "Performance Report");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Performance Report</h1>

<div class="card">

  <c:if test="${not empty message}">
    <div style="padding:10px;background:#e7ffe7;border:1px solid #8bc48b;margin-bottom:10px;">${message}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">${error}</div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/academic/report" style="border:1px solid #ddd;padding:12px;margin-bottom:20px;">
    <label>Student ID</label><br/>
    <input name="student_id" required style="width:220px;"/><br/><br/>
    <label>Semester</label><br/>
    <input name="semester" type="number" min="1" max="3" value="1" required style="width:80px;"/><br/><br/>
    <label>Year</label><br/>
    <input name="year" type="number" value="2026" required style="width:100px;"/><br/><br/>
    <label>Year of Study</label><br/>
    <input name="year_of_study" type="number" min="1" max="4" value="1" required style="width:80px;"/><br/><br/>
    <label>Email report to (optional)</label><br/>
    <input name="email_to" type="email" style="width:320px;"/><br/><br/>
    <button type="submit">Generate Report</button>
  </form>

  <c:if test="${not empty report}">
    <h3>Academic Performance Report</h3>
    <p><b>${report.studentName}</b> (${report.studentId}) | Program: ${report.program}</p>
    <p>Semester ${report.semester}, Year ${report.year}, Year of Study ${report.yearOfStudy}</p>
    <table border="1" cellpadding="6" cellspacing="0">
      <tr>
        <th>Course Code</th><th>Course Title</th><th>Credit Hours</th><th>Grade</th><th>Grade Point</th>
      </tr>
      <c:forEach items="${report.rows}" var="r">
        <tr>
          <td>${r.courseCode}</td>
          <td>${r.courseTitle}</td>
          <td>${r.creditHours}</td>
          <td>${r.grade}</td>
          <td>${r.gradePoint}</td>
        </tr>
      </c:forEach>
    </table>
    <p><b>CGPA:</b> ${report.cgpa}</p>
  </c:if>

</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />