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
    <select id="statusFilter" onchange="filterByStatus()" style="margin-bottom: 10px; padding: 5px; width: 100%; max-width: 300px;">
      <option value="">All Statuses</option>
      <option value="PENDING">Pending</option>
      <option value="IN PROGRESS">In Progress</option>
      <option value="RESOLVED">Resolved</option>
      <option value="REJECTED">Rejected</option>
    </select>
    <table border="1" id="complaintsTable">
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
  <script>
    function filterByStatus() {
      var select, filter, table, tr, td, i, txtValue;
      select = document.getElementById("statusFilter");
      filter = select.value.toUpperCase();
      table = document.getElementById("complaintsTable");
      tr = table.getElementsByTagName("tr");
      for (i = 1; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[4];
        if (td) {
          txtValue = td.textContent || td.innerText;
          if (filter === "" || txtValue.toUpperCase() === filter) {
            tr[i].style.display = "";
          } else {
            tr[i].style.display = "none";
          }
        }
      }
    }
  </script>
</body>
</html>
