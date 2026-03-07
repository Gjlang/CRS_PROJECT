<%@ page contentType="text/html;charset=UTF-8" %>
<%
  String role = (String) session.getAttribute("role");
  if (role == null) role = "UNKNOWN";

  String active = (String) request.getAttribute("activePage");
  if (active == null) active = "";
%>

<div class="sidebar">
  <div class="navTitle">Navigation</div>

  <div class="nav">
    <% if ("COURSE_ADMIN".equals(role)) { %>
      <a href="<%= request.getContextPath() %>/admin/dashboard"
         class="<%= "admin_dashboard".equals(active) ? "active" : "" %>">
        Dashboard
      </a>

      <a href="<%= request.getContextPath() %>/admin/users"
         class="<%= "admin_users".equals(active) ? "active" : "" %>">
        Users Management
      </a>

      <a href="<%= request.getContextPath() %>/admin/enrolments"
         class="<%= "admin_enrolments".equals(active) ? "active" : "" %>">
        Enrolment Approvals
      </a>

      <a href="<%= request.getContextPath() %>/admin/recovery_summary"
         class="<%= "admin_recovery_summary".equals(active) ? "active" : "" %>">
        Recovery Summary Report
      </a>

      <a href="<%= request.getContextPath() %>/admin/notification_history"
         class="<%= "admin_notifications".equals(active) ? "active" : "" %>">
        Notification History
      </a>

    <% } else if ("ACADEMIC_OFFICER".equals(role)) { %>
      <a href="<%= request.getContextPath() %>/academic/dashboard"
         class="<%= "academic_dashboard".equals(active) ? "active" : "" %>">
        Dashboard
      </a>

      <a href="<%= request.getContextPath() %>/academic/eligibility"
         class="<%= "academic_eligibility".equals(active) ? "active" : "" %>">
        Eligibility Check
      </a>

      <a href="<%= request.getContextPath() %>/academic/enrolments"
         class="<%= "academic_enrolments".equals(active) ? "active" : "" %>">
        Enrolments
      </a>

      <a href="<%= request.getContextPath() %>/academic/recovery_plan"
         class="<%= "academic_recovery_plan".equals(active) ? "active" : "" %>">
        Recovery Plan
      </a><%-- ✏️ added --%>

      <a href="<%= request.getContextPath() %>/academic/milestones"
         class="<%= "academic_milestones".equals(active) ? "active" : "" %>">
        Milestones
      </a><%-- ✏️ added --%>

      <a href="<%= request.getContextPath() %>/academic/report"
         class="<%= "report".equals(active) ? "active" : "" %>">
        Performance Report
      </a>

    <% } else { %>
      <div class="muted" style="padding:10px 8px;">
        No role detected. Please login again.
      </div>
    <% } %>
  </div>
</div>