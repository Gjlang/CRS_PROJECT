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
    <input id="student_id" name="student_id" list="studentList" required style="width:220px;"/><br/><br/>

    <datalist id="studentList">
      <c:forEach var="s" items="${studentOptions}">
        <option value="${s.studentId}">${s.fullName}</option>
      </c:forEach>
    </datalist>

    <label>Student Name</label><br/>
    <input id="student_name" type="text" readonly style="width:320px;"/><br/><br/>

    <label>Major</label><br/>
    <input id="student_major" type="text" readonly style="width:320px;"/><br/><br/>

    <label>Semester</label><br/>
    <input id="semester" name="semester" type="number" min="1" max="3" value="1" required style="width:80px;"/><br/><br/>

    <label>Year</label><br/>
    <input id="year" name="year" type="number" value="2026" required style="width:100px;"/><br/><br/>

    <label>Year of Study</label><br/>
    <input id="year_of_study" name="year_of_study" type="number" min="1" max="4" value="1" required style="width:80px;"/><br/><br/>

    <label>Email report to (optional)</label><br/>
    <input name="email_to" type="email" style="width:320px;"/><br/><br/>

    <button type="submit">Generate Report</button>
  </form>

  <c:if test="${not empty report}">
    <h3>Academic Performance Report</h3>
    <p><b>${report.studentName}</b> (${report.studentId}) | Major: ${report.major}</p>
    <p>Semester ${report.semester}, Year ${report.year}, Year of Study ${report.yearOfStudy}</p>

    <table border="1" cellpadding="6" cellspacing="0">
      <tr>
        <th>Course Code</th>
        <th>Course Name</th>
        <th>Credits</th>
        <th>Grade</th>
        <th>Grade Point</th>
      </tr>
      <c:forEach items="${report.rows}" var="r">
        <tr>
          <td>${r.courseCode}</td>
          <td>${r.courseName}</td>
          <td>${r.credits}</td>
          <td>${r.grade}</td>
          <td>${r.gradePoint}</td>
        </tr>
      </c:forEach>
    </table>

    <p><b>CGPA:</b> ${report.cgpa}</p>
  </c:if>

</div>

<script>
(function() {
  const studentIdInput = document.getElementById('student_id');
  const nameInput = document.getElementById('student_name');
  const majorInput = document.getElementById('student_major');
  const semesterInput = document.getElementById('semester');
  const yearInput = document.getElementById('year');
  const yosInput = document.getElementById('year_of_study');

  async function lookupStudent() {
    const studentId = studentIdInput.value.trim();

    if (!studentId) {
      nameInput.value = '';
      majorInput.value = '';
      return;
    }

    try {
      const res = await fetch('${pageContext.request.contextPath}/academic/report?lookup_student_id=' + encodeURIComponent(studentId));

      if (!res.ok) {
        nameInput.value = '';
        majorInput.value = '';
        return;
      }

      const data = await res.json();

      if (!data.found) {
        nameInput.value = '';
        majorInput.value = '';
        return;
      }

      nameInput.value = data.studentName || '';
      majorInput.value = data.major || '';
      semesterInput.value = data.semester || '';
      yearInput.value = data.year || '';
      yosInput.value = data.yearOfStudy || '';
    } catch (e) {
      console.error(e);
    }
  }

  studentIdInput.addEventListener('change', lookupStudent);
  studentIdInput.addEventListener('blur', lookupStudent);
  studentIdInput.addEventListener('input', function() {
    if (studentIdInput.value.trim().length >= 3) {
      lookupStudent();
    }
  });
})();
</script>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />