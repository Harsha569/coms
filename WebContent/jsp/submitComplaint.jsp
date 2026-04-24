<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  if (session.getAttribute("userId") == null || !"student".equals(session.getAttribute("role"))) {
    response.sendRedirect(request.getContextPath() + "/login.html");
    return;
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title>Submit Complaint</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"/>
</head>
<body>
  <div class="container">
    <div class="header">
      <h2>Submit New Complaint</h2>
      <a href="<%= request.getContextPath() %>/StudentDashboardServlet">Back to Dashboard</a>
    </div>
    <form action="<%= request.getContextPath() %>/ComplaintServlet" method="POST">
      <div class="form-group">
        <label>Category:</label>
        <select name="category" required>
          <option value="">--Select--</option>
          <option>Academic</option>
          <option>Hostel</option>
          <option>Canteen</option>
          <option>Infrastructure</option>
          <option>Library</option>
          <option>Transport</option>
          <option>Other</option>
        </select>
      </div>
      <div class="form-group">
        <label>Subject:</label>
        <input type="text" name="subject" required style="width: 300px;">
      </div>
      <div class="form-group">
        <label style="vertical-align: top;">Description:</label>
        <textarea name="description" required rows="5" style="width: 300px;"></textarea>
      </div>
      <button type="submit" class="btn">Submit Complaint</button>
    </form>
  </div>
</body>
</html>
