<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  request.setAttribute("pageTitle", "User Management");
%>
<jsp:include page="/WEB-INF/views/common/_layout_start.jsp" />

<h1 class="pageTitle">User Management</h1>

<div class="card">

  <c:if test="${not empty message}">
    <div style="padding:10px;background:#e7ffe7;border:1px solid #8bc48b;margin-bottom:10px;">
      ${message}
    </div>
  </c:if>

  <c:if test="${not empty error}">
    <div style="padding:10px;background:#ffe7e7;border:1px solid #c48b8b;margin-bottom:10px;">
      ${error}
    </div>
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

    <label>Temporary Password</label><br/>
    <input name="password" type="password" required style="width:320px;"/><br/><br/>

    <button type="submit">Create User</button>
  </form>

  <h3>All Users</h3>

  <table border="1" cellpadding="6" cellspacing="0" style="width:100%;border-collapse:collapse;">
    <tr>
      <th>ID</th>
      <th>Full Name</th>
      <th>Email</th>
      <th>Role</th>
      <th>Active</th>
      <th>Update</th>
      <th>Deactivate</th>
    </tr>

    <c:forEach items="${users}" var="u">
      <tr>
        <form method="post" action="${pageContext.request.contextPath}/admin/users">
          <td>
            ${u.userId}
            <input type="hidden" name="action" value="update"/>
            <input type="hidden" name="user_id" value="${u.userId}"/>
          </td>

          <td>
            <input name="full_name" value="${u.fullName}" required style="width:180px;"/>
          </td>

          <td>
            <input name="email" type="email" value="${u.email}" required style="width:220px;"/>
          </td>

          <td>
            <select name="role">
              <option value="COURSE_ADMIN" <c:if test="${u.role == 'COURSE_ADMIN'}">selected</c:if>>COURSE_ADMIN</option>
              <option value="ACADEMIC_OFFICER" <c:if test="${u.role == 'ACADEMIC_OFFICER'}">selected</c:if>>ACADEMIC_OFFICER</option>
            </select>
          </td>

          <td>
            <select name="active">
              <option value="1" <c:if test="${u.active}">selected</c:if>>1</option>
              <option value="0" <c:if test="${not u.active}">selected</c:if>>0</option>
            </select>
          </td>

          <td>
            <button type="submit">Update</button>
          </td>
        </form>

        <td>
          <form method="post" action="${pageContext.request.contextPath}/admin/users" style="margin:0;">
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