<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Something went wrong</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css" />
</head>
<body>
<div class="card">
  <h2>Something went wrong</h2>
  <p>Sorry — an unexpected error occurred. Please try again.</p>
  <p><a href="<%= request.getContextPath() %>/login.jsp" class="btn btn-primary">Back to Login</a></p>
</div>
</body>
</html>
