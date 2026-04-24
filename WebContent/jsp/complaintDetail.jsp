<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%
  String sessionRole = (String) session.getAttribute("role");
  if (sessionRole == null) {
    response.sendRedirect(request.getContextPath() + "/login.html");
    return;
  }
  @SuppressWarnings("unchecked")
  Map<String,String> c = (Map<String,String>) request.getAttribute("complaint");
  if (c == null) {
    response.sendRedirect(request.getContextPath() + "/index.html");
    return;
  }
  String status = c.getOrDefault("status","Pending");
  boolean isAdmin = "admin".equals(sessionRole);
  String backUrl = isAdmin ? "/AdminServlet" : "/StudentDashboardServlet";
%>
<!DOCTYPE html>
<html>
<head>
  <title>Complaint Detail</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"/>
</head>
<body>
  <div class="container">
    <div class="header">
      <h2>Complaint #<%= c.getOrDefault("id","") %></h2>
      <a href="<%= request.getContextPath() %><%= backUrl %>">Back to Dashboard</a>
    </div>

    <p><strong>Status:</strong> <span class="badge"><%= status %></span></p>
    <p><strong>Submitted Date:</strong> <%= c.getOrDefault("date","") %></p>
    <p><strong>Student Name:</strong> <%= c.getOrDefault("studentName","-") %></p>
    <p><strong>Roll Number:</strong> <%= c.getOrDefault("rollNo","-") %></p>
    <p><strong>Category:</strong> <%= c.getOrDefault("category","-") %></p>
    <p><strong>Subject:</strong> <%= c.getOrDefault("subject","-") %></p>
    
    <div style="border: 1px solid #ccc; padding: 10px; margin-top: 10px; background: #fff;">
      <p><strong>Description:</strong></p>
      <p><%= c.getOrDefault("description","No description provided.") %></p>
    </div>

    <div style="border: 1px solid #ccc; padding: 10px; margin-top: 10px; background: #ffffee;">
      <p><strong>Admin Remark:</strong></p>
      <p><%= c.getOrDefault("adminRemark","No remark yet.") %></p>
    </div>

    <% if (isAdmin) { %>
      <hr style="margin: 20px 0;">
      <h3>Update Status (Admin)</h3>
      <form action="<%= request.getContextPath() %>/AdminServlet" method="POST">
        <input type="hidden" name="action" value="updateStatus" />
        <input type="hidden" name="complaintId" value="<%= c.getOrDefault("id","") %>" />
        
        <div class="form-group">
          <label>Status:</label>
          <select name="status">
            <option value="Pending" <%= "Pending".equals(status)? "selected":"" %>>Pending</option>
            <option value="In Progress" <%= "In Progress".equals(status)? "selected":"" %>>In Progress</option>
            <option value="Resolved" <%= "Resolved".equals(status)? "selected":"" %>>Resolved</option>
            <option value="Rejected" <%= "Rejected".equals(status)? "selected":"" %>>Rejected</option>
          </select>
        </div>
        <div class="form-group">
          <label style="vertical-align: top;">Remark:</label>
          <textarea name="remark" rows="3" style="width: 300px;"><%= c.getOrDefault("adminRemark","") %></textarea>
        </div>
        <button type="submit" class="btn">Update Complaint</button>
      </form>
    <% } %>

  </div>
</body>
</html>
