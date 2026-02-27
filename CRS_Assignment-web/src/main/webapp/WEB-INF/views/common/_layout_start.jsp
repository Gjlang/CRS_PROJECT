<%@ page contentType="text/html;charset=UTF-8" %>
<%
  String pageTitle = (String) request.getAttribute("pageTitle");
  if (pageTitle == null) pageTitle = "CRS";
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title><%= pageTitle %></title>

  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css" />
</head>
<body>

<jsp:include page="/WEB-INF/views/common/_header.jsp" />

<div class="container">
  <jsp:include page="/WEB-INF/views/common/_sidebar.jsp" />

  <main class="main">