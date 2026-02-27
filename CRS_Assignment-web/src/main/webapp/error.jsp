<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Something went wrong</title>
</head>
<body>
  <h2>Something went wrong</h2>
  <p>Sorry — an unexpected error occurred. Please try again.</p>
  <p><a href="<%= request.getContextPath() %>/dashboard.jsp">Back to Dashboard</a></p>
  <p><a href="<%= request.getContextPath() %>/login.jsp">Login</a></p>
</body>
</html>