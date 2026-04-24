<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String complaintId = (String) request.getAttribute("complaintId");
  String sessionRole = (String) session.getAttribute("role");
  if (sessionRole == null) {
    response.sendRedirect(request.getContextPath() + "/login.html");
    return;
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title>Success</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"/>
</head>
<body>
  <div class="container" style="text-align: center;">
    <h2 class="msg-success">Complaint Submitted Successfully!</h2>
    <p>Your Complaint ID is: <strong><%= complaintId != null ? complaintId : "N/A" %></strong></p>
    <br><br>
    <a href="<%= request.getContextPath() %>/StudentDashboardServlet" class="btn">Go to Dashboard</a>
  </div>
</body>
</html>
