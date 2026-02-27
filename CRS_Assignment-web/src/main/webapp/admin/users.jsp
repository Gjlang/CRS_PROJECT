<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
  request.setAttribute("pageTitle", "User Management");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">User Management</h1>

<div class="card">

  <c:if test="${not empty message}">
    <div style="padding:10px;background:#e7ffe7;border:1px solid #8bc48b;margin-bottom:10px;">${message}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">${error}</div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/admin/users" style="border:1px solid #ddd;padding:12px;margin-bottom:20px;">
    <h3>Create User</h3>
    <input type="hidden" name="action" value="create"/>
    <label>Full Name</label><br/>
    <input name="full_name" required style="width:320px;"/><br/><br/>
    <label>Email</label><br/>
    <input name="email" type="email" required style="width:320px;"/><br/><br/>
    <label>Role</label><br/>
    <select name="role">
      <option value="COURSE_ADMIN">COURSE_ADMIN</option>
      <option value="ACADEMIC_OFFICER" selected>ACADEMIC_OFFICER</option>
    </select><br/><br/>
    <label>Temp Password</label><br/>
    <input name="password" type="password" required style="width:320px;"/><br/><br/>
    <button type="submit">Create</button>
  </form>

  <h3>All Users</h3>
  <table border="1" cellpadding="6" cellspacing="0">
    <tr>
      <th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Active</th><th>Actions</th>
    </tr>
    <c:forEach items="${users}" var="u">
      <tr>
        <td>${u.userId}</td>
        <td>${u.fullName}</td>
        <td>${u.email}</td>
        <td>${u.role}</td>
        <td><c:choose><c:when test="${u.active}">1</c:when><c:otherwise>0</c:otherwise></c:choose></td>
        <td>
          <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;">
            <input type="hidden" name="action" value="deactivate"/>
            <input type="hidden" name="user_id" value="${u.userId}"/>
            <button type="submit">Deactivate</button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </table>

</div>

<jsp:include page="/WEB-INF/views/common/_layout_end.jsp" />