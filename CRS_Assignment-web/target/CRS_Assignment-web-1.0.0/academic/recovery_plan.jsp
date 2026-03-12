<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%
  request.setAttribute("pageTitle", "Recovery Plan");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">Recovery Plan</h1>

<div class="card">

  <c:if test="${not empty error}">
    <div class="alert alert-danger">
      <c:out value="${error}"/>
    </div>
  </c:if>

  <form method="get" action="${pageContext.request.contextPath}${pageContext.request.servletPath}" class="recovery-search">
    <div class="field">
      <label>Search Recovery Candidates</label>
      <div class="searchRow">
        <input type="text" name="q" value="${q}" placeholder="Search by Student ID, Name, Course Code, or Course Name" />
        <button type="submit" class="btn btn-primary">Search</button>
        <a href="${pageContext.request.contextPath}${pageContext.request.servletPath}" class="btn btn-ghost">Reset</a>
      </div>
    </div>
  </form>

  <h3 style="margin-top:18px;">Students Needing Recovery</h3>

  <c:choose>
    <c:when test="${empty groupedCandidates}">
      <div class="alert alert-info">No recovery candidates found.</div>
    </c:when>
    <c:otherwise>
      <div class="studentCards">
        <c:forEach var="entry" items="${groupedCandidates}">
          <c:set var="rows" value="${entry.value}" />
          <c:set var="first" value="${rows[0]}" />

          <div class="studentCard">
            <div class="studentCardHeader">
              <div>
                <div class="studentName">${first.studentName}</div>
                <div class="studentMeta">
                  Student ID: ${first.studentId}
                  &nbsp; | &nbsp;
                  Failed Courses: ${fn:length(rows)}
                </div>
              </div>
            </div>

            <div class="tableWrap">
              <table>
                <thead>
                  <tr>
                    <th>Course Code</th>
                    <th>Course Name</th>
                    <th>Attempt</th>
                    <th>Failed Components</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="c" items="${rows}">
                    <tr>
                      <td>${c.courseCode}</td>
                      <td>${c.courseName}</td>
                      <td>${c.attemptNo}</td>
                      <td>${c.failedComponentCount}</td>
                      <td>
                       <a class="btn btn-primary"
						   href="${pageContext.request.contextPath}${pageContext.request.servletPath}?student_id=${c.studentId}&course_code=${c.courseCode}&attempt_no=${c.attemptNo}#selected-recovery">
						   Manage Recovery Plan
						</a>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
        </c:forEach>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<c:if test="${not empty studentId and not empty courseCode and not empty attemptNo}">
  <div class="card" id="selected-recovery">
    <h3>Selected Course Recovery Plan</h3>

    <div class="selectedInfo">
      <div><b>Student ID:</b> ${studentId}</div>
      <div><b>Student Name:</b> ${studentName}</div>
      <div><b>Course Code:</b> ${courseCode}</div>
      <div><b>Course Name:</b> ${courseName}</div>
      <div><b>Attempt:</b> ${attemptNo}</div>
    </div>

    <h4>Failed Components</h4>
    <c:choose>
      <c:when test="${empty failedComponents}">
        <div class="alert alert-info">No failed components found for this course.</div>
      </c:when>
      <c:otherwise>
        <div class="tableWrap">
          <table>
            <thead>
              <tr>
                <th>Assessment ID</th>
                <th>Grade</th>
                <th>Grade Point</th>
                <th>Failed</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="fc" items="${failedComponents}">
                <tr>
                  <td>${fc.assessmentId}</td>
                  <td>${fc.grade}</td>
                  <td>${fc.gradePoint}</td>
                  <td>
                    <c:choose>
                      <c:when test="${fc.failed}">YES</c:when>
                      <c:otherwise>NO</c:otherwise>
                    </c:choose>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </c:otherwise>
    </c:choose>

    <h4 style="margin-top:20px;">Recommendation Entry</h4>
    <c:choose>
      <c:when test="${empty plan}">
        <div class="alert alert-info">No recovery recommendation created yet.</div>

        <form method="post" action="${planActionUrl}">
          <input type="hidden" name="action" value="createPlan"/>
          <input type="hidden" name="student_id" value="${studentId}"/>
          <input type="hidden" name="course_code" value="${courseCode}"/>
          <input type="hidden" name="attempt_no" value="${attemptNo}"/>

          <div class="field">
            <label>Recommendation</label>
            <textarea name="recommendation" rows="4" required
                      placeholder="Example: Week 1-2 review topics, Week 3 meet lecturer, Week 4 recovery exam"></textarea>
          </div>

          <button type="submit" class="btn btn-primary">Create Recovery Recommendation</button>
        </form>
      </c:when>

      <c:otherwise>
        <form method="post" action="${planActionUrl}" style="margin-bottom:20px;">
          <input type="hidden" name="action" value="updatePlan"/>
          <input type="hidden" name="student_id" value="${studentId}"/>
          <input type="hidden" name="course_code" value="${courseCode}"/>
          <input type="hidden" name="attempt_no" value="${attemptNo}"/>

          <div class="field">
            <label>Recommendation</label>
            <textarea name="recommendation" rows="4"><c:out value="${plan.recommendation}"/></textarea>
          </div>

          <button type="submit" class="btn btn-primary">Update Recommendation</button>
        </form>
      </c:otherwise>
    </c:choose>

    <h4>Milestone / Action Plan</h4>

    <form method="post" action="${planActionUrl}" class="milestoneForm">
      <input type="hidden" name="action" value="addMilestone"/>
      <input type="hidden" name="student_id" value="${studentId}"/>
      <input type="hidden" name="course_code" value="${courseCode}"/>
      <input type="hidden" name="attempt_no" value="${attemptNo}"/>

      <input name="title" placeholder="Study Week / Milestone Title" required/>
      <input name="task" placeholder="Task / Action plan" required/>
      <input type="date" name="due_date"/>
      <input name="remarks" placeholder="Remarks / Notes"/>
      <button type="submit" class="btn btn-primary">Add Milestone</button>
    </form>

    <h4>Recovery Progress Tracking</h4>
    <c:choose>
      <c:when test="${empty milestones}">
        <div class="alert alert-info">No milestones added yet.</div>
      </c:when>
      <c:otherwise>
        <div class="tableWrap">
          <table>
            <thead>
              <tr>
                <th>Study Week / Title</th>
                <th>Task</th>
                <th>Due Date</th>
                <th>Status</th>
                <th>Remarks</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="m" items="${milestones}">
                <tr>
                  <td>${m.title}</td>
                  <td>${m.task}</td>
                  <td>${m.dueDate}</td>
                  <td>${m.status}</td>
                  <td>${m.remarks}</td>
                  <td>
                    <form method="post" action="${planActionUrl}" class="inlineForm">
                      <input type="hidden" name="action" value="updateMilestone"/>
                      <input type="hidden" name="student_id" value="${studentId}"/>
                      <input type="hidden" name="course_code" value="${courseCode}"/>
                      <input type="hidden" name="attempt_no" value="${attemptNo}"/>
                      <input type="hidden" name="milestone_id" value="${m.milestoneId}"/>

                      <select name="status">
                        <option value="PENDING" <c:if test="${m.status == 'PENDING'}">selected</c:if>>PENDING</option>
                        <option value="DONE" <c:if test="${m.status == 'DONE'}">selected</c:if>>DONE</option>
                      </select>

                      <input name="remarks" value="${fn:escapeXml(m.remarks)}"/>
                      <button type="submit" class="btn btn-primary">Save</button>
                    </form>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </c:otherwise>
    </c:choose>
  </div>
</c:if>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />