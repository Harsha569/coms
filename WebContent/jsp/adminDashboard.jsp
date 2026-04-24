<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.Map" %>
<%
  if (!"admin".equals(session.getAttribute("role"))) {
    response.sendRedirect(request.getContextPath() + "/login.html");
    return;
  }
  @SuppressWarnings("unchecked")
  List<Map<String,String>> allComplaints = (List<Map<String,String>>) request.getAttribute("complaints");
  if (allComplaints == null) allComplaints = new java.util.ArrayList<>();
  
  String msgText = (String) request.getAttribute("msgText");
  String msgType = (String) request.getAttribute("msgType");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Admin Dashboard</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"/>
</head>
<body>
  <div class="container">
    <div class="header">
      <h2>Admin Dashboard</h2>
      <p>View and resolve student complaints.</p>
      <a href="<%= request.getContextPath() %>/LogoutServlet" class="btn">Logout</a>
    </div>

    <% if (msgText != null) { %>
      <div class="msg-<%= msgType == null ? "error" : msgType %>"><%= msgText %></div>
    <% } %>

    <h3>All Complaints</h3>
    <table border="1">
      <tr>
        <th>ID</th>
        <th>Student</th>
        <th>Category</th>
        <th>Subject</th>
        <th>Status</th>
        <th>Actions</th>
      </tr>
      <% if (allComplaints.isEmpty()) { %>
        <tr><td colspan="6" style="text-align: center;">No complaints submitted yet.</td></tr>
      <% } else {
           for (Map<String,String> c : allComplaints) { %>
        <tr>
          <td><%= c.getOrDefault("id","") %></td>
          <td><%= c.getOrDefault("studentName","") %> (<%= c.getOrDefault("rollNo","") %>)</td>
          <td><%= c.getOrDefault("category","") %></td>
          <td><%= c.getOrDefault("subject","") %></td>
          <td class="badge"><%= c.getOrDefault("status","Pending") %></td>
          <td>
             <a href="<%= request.getContextPath() %>/ComplaintDetailServlet?id=<%= c.getOrDefault("id","") %>">View & Update</a>
          </td>
        </tr>
      <%   } 
         } %>
    </table>
  </div>
</body>
</html>
