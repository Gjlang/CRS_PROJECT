<%@ page contentType="text/html;charset=UTF-8" %>
<%
  
    Object u = session.getAttribute("user");
    String role = (String) session.getAttribute("role"); // if you store role separately
    if (role == null && u != null) {
    
        role = "UNKNOWN";
    }

    String displayName = (String) session.getAttribute("username"); // if you store username separately
    if (displayName == null && u != null) displayName = "User";
%>

<div class="topbar">
  <div class="brand">
    <span>CRS</span>
    <span class="muted" style="color:rgba(255,255,255,0.75)">Course Recovery System</span>
  </div>

  <div class="userbar">
    <span><%= displayName %></span>

    <%
      String badgeClass = "badge";
      if ("COURSE_ADMIN".equals(role)) badgeClass += " role-admin";
      else if ("ACADEMIC_OFFICER".equals(role)) badgeClass += " role-academic";
    %>
    <span class="<%= badgeClass %>"><%= role %></span>

    <a class="link" href="<%= request.getContextPath() %>/logout">Logout</a>
  </div>
</div>