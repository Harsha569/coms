# COMS File Guide

This project is a simple Java servlet application for managing college complaints. The files are split into three main parts: source code, web pages, and XML data storage.

## Root Files

- `README.md` explains what the project does, how to set it up, and how to run it on Tomcat.
- `explanation.md` gives a simple guide to the project structure and the role of each file.

## Java Source Files

These files contain the application logic.

- `AdminServlet.java` loads all complaints for the admin dashboard and updates complaint status or remarks.
- `ComplaintDetailServlet.java` opens one complaint by ID and sends it to the detail page.
- `ComplaintServlet.java` handles student complaint submission and saves the complaint to XML.
- `LoginServlet.java` checks user credentials, starts the session, and sends the user to the correct dashboard.
- `LogoutServlet.java` ends the session and returns the user to the login page.
- `RegisterServlet.java` creates a new student account and stores it in the XML user file.
- `StudentDashboardServlet.java` loads only the logged-in student’s complaints.
- `XMLHelper.java` is the data layer. It reads, writes, searches, and updates the XML files.

## Web Pages

These files make up the user interface.

- `index.html` is the home page with links to login and registration.
- `login.html` is the login form for students and admins.
- `register.html` is the student registration form.
- `css/style.css` contains the shared styling for the whole app.

## JSP Pages

These pages show dynamic content from the servlets.

- `adminDashboard.jsp` shows all complaints and allows admins to filter and open them.
- `complaintDetail.jsp` shows one complaint in detail and lets admins update it.
- `studentDashboard.jsp` shows the current student’s complaints.
- `submitComplaint.jsp` is the form students use to submit a new complaint.
- `submitSuccess.jsp` confirms that a complaint was saved and shows the complaint ID.

## WEB-INF Files

These files support deployment and data storage.

- `web.xml` maps servlet URLs and sets the welcome page.
- `WEB-INF/data/users.xml` stores user accounts, including the default admin user.
- `WEB-INF/data/complaints.xml` stores all submitted complaints.

## Build Output

- `WEB-INF/classes/` stores the compiled Java classes used by Tomcat at runtime.

## How It Works

1. A user opens `index.html` and chooses login or registration.
2. The login or registration form sends data to a servlet.
3. The servlet uses `XMLHelper.java` to read or update the XML files.
4. The servlet forwards the user to a JSP page for the dashboard or details view.

This structure keeps the app easy to understand because each file has one clear job.
