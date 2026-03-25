<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    request.setAttribute("pageTitle", "Admin Dashboard");
%>

<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<style>
/* ── Summary Strip ── */
.summaryStrip{
    display:flex;
    flex-wrap:nowrap;
    gap:6px;
    overflow-x:auto;
    margin-bottom:12px;
}
.summaryCard{
    flex:0 0 auto;
    min-width:90px;
    background:#fff;
    border:1px solid #e5e7eb;
    border-radius:8px;
    padding:8px 12px;
}
.summaryLabel{font-size:10px;color:#6b7280;white-space:nowrap;}
.summaryValue{font-size:20px;font-weight:800;line-height:1.3;}

/* ── Welcome ── */
.welcomeCard{
    background:#fff;
    border:1px solid #e5e7eb;
    border-radius:10px;
    padding:10px 14px;
    margin-bottom:12px;
    font-size:13px;
}

/* ── Section ── */
.section{
    background:#fff;
    border:1px solid #e5e7eb;
    border-radius:10px;
    margin-bottom:12px;
    overflow:hidden;
}
.sectionHeader{
    display:flex;
    align-items:center;
    justify-content:space-between;
    padding:8px 12px;
    border-bottom:1px solid #e5e7eb;
}
.sectionTitle{margin:0;font-size:13px;font-weight:800;}
.sectionMeta{font-size:11px;color:#6b7280;}

/* ── Table: fits full width, no horizontal scroll ── */
.tableScroll{
    overflow-x:hidden;
    overflow-y:auto;
    max-height:240px;
    width:100%;
}
.tableScroll table{
    width:100%;
    border-collapse:collapse;
    table-layout:fixed;   /* KEY: divides width equally across all columns */
}
.tableScroll th,
.tableScroll td{
    padding:5px 6px;
    border-bottom:1px solid #f0f0f0;
    font-size:11px;
    text-align:left;
    vertical-align:top;
    overflow:hidden;
    text-overflow:ellipsis;
    white-space:nowrap;
}
.tableScroll th{
    background:#f9fafb;
    font-weight:700;
    font-size:10px;
    color:#374151;
    position:sticky;
    top:0;
    z-index:1;
    text-transform:uppercase;
    letter-spacing:0.03em;
}
.tableScroll tr:hover td{background:#fafafe;}

/* ── Pagination bar ── */
.pgBar{
    display:flex;
    align-items:center;
    justify-content:space-between;
    padding:5px 10px;
    background:#f9fafb;
    border-top:1px solid #e5e7eb;
    font-size:11px;
    color:#6b7280;
    min-height:30px;
}
.pgBtns{display:flex;gap:2px;align-items:center;}
.pgBtn{
    display:inline-flex;
    align-items:center;
    justify-content:center;
    min-width:22px;
    height:22px;
    padding:0 5px;
    border:1px solid #d1d5db;
    border-radius:4px;
    background:#fff;
    font-size:10px;
    font-weight:600;
    cursor:pointer;
    color:#374151;
    transition:background .1s;
    user-select:none;
}
.pgBtn:hover:not([disabled]){background:#f3f4f6;}
.pgBtn[disabled]{opacity:.35;cursor:default;pointer-events:none;}
.pgBtn.active{background:#111827;color:#fff;border-color:#111827;}
</style>

<h1 class="pageTitle">Admin Dashboard</h1>

<c:if test="${not empty message}"><div class="alert alert-success">${message}</div></c:if>
<c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>

<div class="welcomeCard">
    <strong>Welcome, ${sessionScope.name}</strong>
    &nbsp;&middot;&nbsp;
    Role: <span class="badge">${sessionScope.role}</span>
    &nbsp;&middot;&nbsp;
    <span style="color:#6b7280;">Read-only mode</span>
</div>

<div class="summaryStrip">
    <div class="summaryCard"><div class="summaryLabel">Users</div><div class="summaryValue">${userCount}</div></div>
    <div class="summaryCard"><div class="summaryLabel">Students</div><div class="summaryValue">${studentCount}</div></div>
    <div class="summaryCard"><div class="summaryLabel">Courses</div><div class="summaryValue">${courseCount}</div></div>
    <div class="summaryCard"><div class="summaryLabel">Assessments</div><div class="summaryValue">${assessmentCount}</div></div>
    <div class="summaryCard"><div class="summaryLabel">Results</div><div class="summaryValue">${studentResultCount}</div></div>
    <div class="summaryCard"><div class="summaryLabel">Prog. Regs</div><div class="summaryValue">${registrationCount}</div></div>
    <div class="summaryCard"><div class="summaryLabel">Enrolments</div><div class="summaryValue">${enrolmentCount}</div></div>
    <div class="summaryCard"><div class="summaryLabel">Recovery</div><div class="summaryValue">${recoveryPlanCount}</div></div>
    <div class="summaryCard"><div class="summaryLabel">Milestones</div><div class="summaryValue">${milestoneCount}</div></div>
    <div class="summaryCard"><div class="summaryLabel">Notifications</div><div class="summaryValue">${notificationCount}</div></div>
</div>

<!-- ══ USERS ══ -->
<div class="section">
    <div class="sectionHeader">
        <h2 class="sectionTitle">Users Overview</h2>
        <span class="sectionMeta">${userCount} records</span>
    </div>
    <div class="tableScroll">
        <table>
            <colgroup>
                <col style="width:6%">   <!-- User ID -->
                <col style="width:16%">  <!-- Full Name -->
                <col style="width:22%">  <!-- Email -->
                <col style="width:14%">  <!-- Role -->
                <col style="width:6%">   <!-- Active -->
                <col style="width:16%">  <!-- Created At -->
                <col style="width:12%">  <!-- Reset Token -->
                <col style="width:8%">   <!-- Expiry -->
            </colgroup>
            <thead><tr>
                <th>ID</th><th>Full Name</th><th>Email</th><th>Role</th>
                <th>Active</th><th>Created At</th><th>Reset Token</th><th>Token Expiry</th>
            </tr></thead>
            <tbody id="body-users">
            <c:forEach var="u" items="${users}"><tr>
                <td>${u.userId}</td><td>${u.fullName}</td><td>${u.email}</td><td>${u.role}</td>
                <td>${u.active}</td><td>${u.createdAt}</td><td>${u.resetToken}</td><td>${u.resetTokenExpiry}</td>
            </tr></c:forEach>
            <c:if test="${empty users}"><tr><td colspan="8">No users found.</td></tr></c:if>
            </tbody>
        </table>
    </div>
    <div class="pgBar" id="pg-users"></div>
</div>

<!-- ══ STUDENTS ══ -->
<div class="section">
    <div class="sectionHeader">
        <h2 class="sectionTitle">Students Overview</h2>
        <span class="sectionMeta">${studentCount} records</span>
    </div>
    <div class="tableScroll">
        <table>
            <colgroup>
                <col style="width:8%">   <!-- Student ID -->
                <col style="width:12%">  <!-- First Name -->
                <col style="width:12%">  <!-- Last Name -->
                <col style="width:16%">  <!-- Full Name -->
                <col style="width:14%">  <!-- Major -->
                <col style="width:5%">   <!-- Year -->
                <col style="width:22%">  <!-- Email -->
                <col style="width:6%">   <!-- Active -->
            </colgroup>
            <thead><tr>
                <th>ID</th><th>First</th><th>Last</th><th>Full Name</th>
                <th>Major</th><th>Yr</th><th>Email</th><th>Active</th>
            </tr></thead>
            <tbody id="body-students">
            <c:forEach var="s" items="${students}"><tr>
                <td>${s.studentId}</td><td>${s.firstName}</td><td>${s.lastName}</td><td>${s.fullName}</td>
                <td>${s.major}</td><td>${s.year}</td><td>${s.email}</td><td>${s.active}</td>
            </tr></c:forEach>
            <c:if test="${empty students}"><tr><td colspan="8">No students found.</td></tr></c:if>
            </tbody>
        </table>
    </div>
    <div class="pgBar" id="pg-students"></div>
</div>

<!-- ══ COURSES ══ -->
<div class="section">
    <div class="sectionHeader">
        <h2 class="sectionTitle">Courses Overview</h2>
        <span class="sectionMeta">${courseCount} records</span>
    </div>
    <div class="tableScroll">
        <table>
            <colgroup>
                <col style="width:10%">
                <col style="width:32%">
                <col style="width:8%">
                <col style="width:15%">
                <col style="width:25%">
                <col style="width:10%">
            </colgroup>
            <thead><tr>
                <th>Course ID</th><th>Course Name</th><th>Credits</th>
                <th>Semester</th><th>Instructor</th><th>Capacity</th>
            </tr></thead>
            <tbody id="body-courses">
            <c:forEach var="c" items="${courses}"><tr>
                <td>${c.courseId}</td><td>${c.courseName}</td><td>${c.credits}</td>
                <td>${c.semester}</td><td>${c.instructor}</td><td>${c.capacity}</td>
            </tr></c:forEach>
            <c:if test="${empty courses}"><tr><td colspan="6">No courses found.</td></tr></c:if>
            </tbody>
        </table>
    </div>
    <div class="pgBar" id="pg-courses"></div>
</div>

<!-- ══ ASSESSMENTS ══ -->
<div class="section">
    <div class="sectionHeader">
        <h2 class="sectionTitle">Assessments Overview</h2>
        <span class="sectionMeta">${assessmentCount} records</span>
    </div>
    <div class="tableScroll">
        <table>
            <colgroup>
                <col style="width:15%">
                <col style="width:20%">
                <col style="width:50%">
                <col style="width:15%">
            </colgroup>
            <thead><tr>
                <th>Assessment ID</th><th>Course Code</th><th>Component Name</th><th>Weight %</th>
            </tr></thead>
            <tbody id="body-assessments">
            <c:forEach var="a" items="${assessments}"><tr>
                <td>${a.assessmentId}</td><td>${a.courseCode}</td>
                <td>${a.componentName}</td><td>${a.weightPercent}</td>
            </tr></c:forEach>
            <c:if test="${empty assessments}"><tr><td colspan="4">No assessments found.</td></tr></c:if>
            </tbody>
        </table>
    </div>
    <div class="pgBar" id="pg-assessments"></div>
</div>

<!-- ══ STUDENT RESULTS ══ -->
<div class="section">
    <div class="sectionHeader">
        <h2 class="sectionTitle">Student Results Overview</h2>
        <span class="sectionMeta">${studentResultCount} records</span>
    </div>
    <div class="tableScroll">
        <table>
            <colgroup>
                <col style="width:7%">   <!-- Result ID -->
                <col style="width:8%">   <!-- Student ID -->
                <col style="width:10%">  <!-- Course Code -->
                <col style="width:10%">  <!-- Assessment ID -->
                <col style="width:7%">   <!-- Attempt -->
                <col style="width:6%">   <!-- Grade -->
                <col style="width:8%">   <!-- Grade Point -->
                <col style="width:6%">   <!-- Failed -->
                <col style="width:9%">   <!-- Semester -->
                <col style="width:6%">   <!-- Year -->
                <col style="width:10%">  <!-- Year of Study -->
                <col style="width:13%">  <!-- Enrolment ID -->
            </colgroup>
            <thead><tr>
                <th>Res.ID</th><th>Stu.ID</th><th>Course</th><th>Assess.ID</th>
                <th>Att</th><th>Grade</th><th>GP</th><th>Failed</th>
                <th>Sem</th><th>Yr</th><th>Yr Study</th><th>Enrol.ID</th>
            </tr></thead>
            <tbody id="body-results">
            <c:forEach var="r" items="${studentResults}"><tr>
                <td>${r.resultId}</td><td>${r.studentId}</td><td>${r.courseCode}</td><td>${r.assessmentId}</td>
                <td>${r.attemptNo}</td><td>${r.grade}</td><td>${r.gradePoint}</td><td>${r.failed}</td>
                <td>${r.semester}</td><td>${r.year}</td><td>${r.yearOfStudy}</td><td>${r.enrolmentId}</td>
            </tr></c:forEach>
            <c:if test="${empty studentResults}"><tr><td colspan="12">No student results found.</td></tr></c:if>
            </tbody>
        </table>
    </div>
    <div class="pgBar" id="pg-results"></div>
</div>

<!-- ══ PROGRESSION REGISTRATIONS ══ -->
<div class="section">
    <div class="sectionHeader">
        <h2 class="sectionTitle">Progression Registrations Overview</h2>
        <span class="sectionMeta">${registrationCount} records</span>
    </div>
    <div class="tableScroll">
        <table>
            <colgroup>
                <col style="width:10%">
                <col style="width:10%">
                <col style="width:8%">
                <col style="width:10%">
                <col style="width:18%">
                <col style="width:18%">
                <col style="width:12%">
                <col style="width:14%">
            </colgroup>
            <thead><tr>
                <th>Reg ID</th><th>Stu.ID</th><th>CGPA</th><th>Failed Ct</th>
                <th>Eligibility</th><th>Reg Status</th><th>Created By</th><th>Created At</th>
            </tr></thead>
            <tbody id="body-regs">
            <c:forEach var="r" items="${registrations}"><tr>
                <td>${r.registrationId}</td><td>${r.studentId}</td><td>${r.cgpa}</td><td>${r.failedCourseCount}</td>
                <td>${r.eligibilityStatus}</td><td>${r.registrationStatus}</td>
                <td>${r.createdByUserId}</td><td>${r.createdAt}</td>
            </tr></c:forEach>
            <c:if test="${empty registrations}"><tr><td colspan="8">No progression registrations found.</td></tr></c:if>
            </tbody>
        </table>
    </div>
    <div class="pgBar" id="pg-regs"></div>
</div>

<!-- ══ ENROLMENTS ══ -->
<div class="section">
    <div class="sectionHeader">
        <h2 class="sectionTitle">Enrolments Overview</h2>
        <span class="sectionMeta">${enrolmentCount} records</span>
    </div>
    <div class="tableScroll">
        <table>
            <colgroup>
                <col style="width:8%">
                <col style="width:8%">
                <col style="width:9%">
                <col style="width:6%">
                <col style="width:10%">
                <col style="width:9%">
                <col style="width:12%">
                <col style="width:8%">
                <col style="width:8%">
                <col style="width:12%">
                <col style="width:10%">
            </colgroup>
            <thead><tr>
                <th>Enrol.ID</th><th>Stu.ID</th><th>Course</th><th>Att</th>
                <th>Eligibility</th><th>Status</th><th>Created At</th>
                <th>By</th><th>Dec.By</th><th>Dec.At</th><th>Reject Reason</th>
            </tr></thead>
            <tbody id="body-enrolments">
            <c:forEach var="e" items="${enrolments}"><tr>
                <td>${e.enrolmentId}</td><td>${e.studentId}</td><td>${e.courseCode}</td><td>${e.attemptNo}</td>
                <td>${e.eligibilityStatus}</td><td>${e.enrolmentStatus}</td><td>${e.createdAt}</td>
                <td>${e.createdByUserId}</td><td>${e.decidedByUserId}</td><td>${e.decidedAt}</td><td>${e.rejectReason}</td>
            </tr></c:forEach>
            <c:if test="${empty enrolments}"><tr><td colspan="11">No enrolments found.</td></tr></c:if>
            </tbody>
        </table>
    </div>
    <div class="pgBar" id="pg-enrolments"></div>
</div>

<!-- ══ RECOVERY PLANS ══ -->
<div class="section">
    <div class="sectionHeader">
        <h2 class="sectionTitle">Recovery Plans Overview</h2>
        <span class="sectionMeta">${recoveryPlanCount} records</span>
    </div>
    <div class="tableScroll">
        <table>
            <colgroup>
                <col style="width:9%">
                <col style="width:10%">
                <col style="width:12%">
                <col style="width:8%">
                <col style="width:37%">
                <col style="width:10%">
                <col style="width:14%">
            </colgroup>
            <thead><tr>
                <th>Plan ID</th><th>Stu.ID</th><th>Course</th>
                <th>Att</th><th>Recommendation</th><th>Created By</th><th>Created At</th>
            </tr></thead>
            <tbody id="body-plans">
            <c:forEach var="p" items="${recoveryPlans}"><tr>
                <td>${p.planId}</td><td>${p.studentId}</td><td>${p.courseCode}</td>
                <td>${p.attemptNo}</td><td>${p.recommendation}</td>
                <td>${p.createdByUserId}</td><td>${p.createdAt}</td>
            </tr></c:forEach>
            <c:if test="${empty recoveryPlans}"><tr><td colspan="7">No recovery plans found.</td></tr></c:if>
            </tbody>
        </table>
    </div>
    <div class="pgBar" id="pg-plans"></div>
</div>

<!-- ══ MILESTONES ══ -->
<div class="section">
    <div class="sectionHeader">
        <h2 class="sectionTitle">Milestones Overview</h2>
        <span class="sectionMeta">${milestoneCount} records</span>
    </div>
    <div class="tableScroll">
        <table>
            <colgroup>
                <col style="width:10%">
                <col style="width:8%">
                <col style="width:9%">
                <col style="width:18%">
                <col style="width:16%">
                <col style="width:10%">
                <col style="width:9%">
                <col style="width:8%">
                <col style="width:12%">
            </colgroup>
            <thead><tr>
                <th>Mile.ID</th><th>Plan ID</th><th>Week</th><th>Task</th>
                <th>Title</th><th>Due Date</th><th>Status</th><th>Grade</th><th>Remarks</th>
            </tr></thead>
            <tbody id="body-milestones">
            <c:forEach var="m" items="${milestones}"><tr>
                <td>${m.milestoneId}</td><td>${m.planId}</td><td>${m.studyWeek}</td><td>${m.task}</td>
                <td>${m.title}</td><td>${m.dueDate}</td><td>${m.status}</td>
                <td>${m.grade}</td><td>${m.remarks}</td>
            </tr></c:forEach>
            <c:if test="${empty milestones}"><tr><td colspan="9">No milestones found.</td></tr></c:if>
            </tbody>
        </table>
    </div>
    <div class="pgBar" id="pg-milestones"></div>
</div>

<!-- ══ NOTIFICATIONS ══ -->
<div class="section">
    <div class="sectionHeader">
        <h2 class="sectionTitle">Notifications Overview</h2>
        <span class="sectionMeta">${notificationCount} records</span>
    </div>
    <div class="tableScroll">
        <table>
            <colgroup>
                <col style="width:8%">
                <col style="width:20%">
                <col style="width:18%">
                <col style="width:24%">
                <col style="width:9%">
                <col style="width:8%">
                <col style="width:13%">
            </colgroup>
            <thead><tr>
                <th>ID</th><th>Recipient</th><th>Subject</th><th>Body</th>
                <th>Type</th><th>Status</th><th>Created At</th>
            </tr></thead>
            <tbody id="body-notifications">
            <c:forEach var="n" items="${notifications}"><tr>
                <td>${n.notificationId}</td><td>${n.recipientEmail}</td><td>${n.subject}</td><td>${n.body}</td>
                <td>${n.type}</td><td>${n.status}</td><td>${n.createdAt}</td>
            </tr></c:forEach>
            <c:if test="${empty notifications}"><tr><td colspan="7">No notifications found.</td></tr></c:if>
            </tbody>
        </table>
    </div>
    <div class="pgBar" id="pg-notifications"></div>
</div>

<!-- ══ JS Pagination (JSP-safe) ══ -->
<script>
(function () {
    var PAGE_SIZE = 10;

    function paginate(bodyId, barId) {
        var body = document.getElementById(bodyId);
        var bar  = document.getElementById(barId);
        if (!body || !bar) return;

        var allRows = Array.prototype.slice.call(body.querySelectorAll('tr'));
        if (allRows.length === 0) return;

        var total   = allRows.length;
        var pages   = Math.ceil(total / PAGE_SIZE);
        var current = 1;

        function show(page) {
            current = page;
            var start = (page - 1) * PAGE_SIZE;
            var end   = start + PAGE_SIZE;
            allRows.forEach(function (row, i) {
                row.style.display = (i >= start && i < end) ? '' : 'none';
            });
            render();
        }

        function render() {
            var from  = (current - 1) * PAGE_SIZE + 1;
            var to    = Math.min(current * PAGE_SIZE, total);
            var delta = 2;
            var range = [];
            for (var p = Math.max(1, current - delta); p <= Math.min(pages, current + delta); p++) {
                range.push(p);
            }

            var html = '<span>Rows ' + from + '&ndash;' + to + ' of ' + total + '</span>';
            if (pages > 1) {
                html += '<div class="pgBtns">';
                html += '<button class="pgBtn" ' + (current === 1 ? 'disabled' : '') + ' data-pg="1">\u00AB</button>';
                html += '<button class="pgBtn" ' + (current === 1 ? 'disabled' : '') + ' data-pg="' + (current - 1) + '">\u2039</button>';
                for (var i = 0; i < range.length; i++) {
                    var pg = range[i];
                    html += '<button class="pgBtn ' + (pg === current ? 'active' : '') + '" data-pg="' + pg + '">' + pg + '</button>';
                }
                html += '<button class="pgBtn" ' + (current === pages ? 'disabled' : '') + ' data-pg="' + (current + 1) + '">\u203A</button>';
                html += '<button class="pgBtn" ' + (current === pages ? 'disabled' : '') + ' data-pg="' + pages + '">\u00BB</button>';
                html += '</div>';
            }

            bar.innerHTML = html;
            bar.querySelectorAll('.pgBtn:not([disabled])').forEach(function (btn) {
                btn.addEventListener('click', function () {
                    show(parseInt(btn.getAttribute('data-pg'), 10));
                });
            });
        }

        show(1);
    }

    [
        ['body-users',         'pg-users'],
        ['body-students',      'pg-students'],
        ['body-courses',       'pg-courses'],
        ['body-assessments',   'pg-assessments'],
        ['body-results',       'pg-results'],
        ['body-regs',          'pg-regs'],
        ['body-enrolments',    'pg-enrolments'],
        ['body-plans',         'pg-plans'],
        ['body-milestones',    'pg-milestones'],
        ['body-notifications', 'pg-notifications']
    ].forEach(function (s) { paginate(s[0], s[1]); });
})();
</script>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />
