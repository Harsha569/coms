<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.Map" %>
<%
  if (session.getAttribute("userId") == null || !"student".equals(session.getAttribute("role"))) {
    response.sendRedirect(request.getContextPath() + "/login.html");
    return;
  }
  @SuppressWarnings("unchecked")
  List<Map<String,String>> complaints = (List<Map<String,String>>) request.getAttribute("complaints");
  if (complaints == null) complaints = new java.util.ArrayList<>();
  
  String msgText = (String) request.getAttribute("msgText");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Student Dashboard</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"/>
</head>
<body>
  <div class="container">
    <div class="header">
      <h2>Student Dashboard</h2>
      <p>Welcome, <%= session.getAttribute("name") %></p>
      <a href="<%= request.getContextPath() %>/jsp/submitComplaint.jsp" class="btn">Submit New Complaint</a>
      <a href="<%= request.getContextPath() %>/LogoutServlet" class="btn">Logout</a>
    </div>
    
    <% if (msgText != null) { %>
      <div class="msg-error"><%= msgText %></div>
    <% } %>
    
    <h3>My Complaints</h3>
    <table border="1">
      <tr>
        <th>ID</th>
        <th>Category</th>
        <th>Subject</th>
        <th>Date</th>
        <th>Status</th>
        <th>Action</th>
      </tr>
      <% if (complaints.isEmpty()) { %>
        <tr><td colspan="6" style="text-align: center;">No complaints found.</td></tr>
      <% } else {
           for (Map<String,String> c : complaints) { %>
        <tr>
          <td><%= c.getOrDefault("id","") %></td>
          <td><%= c.getOrDefault("category","") %></td>
          <td><%= c.getOrDefault("subject","") %></td>
          <td><%= c.getOrDefault("date","") %></td>
          <td class="badge"><%= c.getOrDefault("status","Pending") %></td>
          <td>
            <a href="<%= request.getContextPath() %>/ComplaintDetailServlet?id=<%= c.getOrDefault("id","") %>">View</a>
          </td>
        </tr>
      <%   } 
         } %>
    </table>
  </div>
</body>
</html>
