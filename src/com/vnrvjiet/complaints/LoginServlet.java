package com.vnrvjiet.complaints;

import java.io.IOException;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String identifier = request.getParameter("identifier");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (identifier == null || password == null || role == null) {
            response.sendRedirect("login.html");
            return;
        }

        identifier = identifier.trim();
        role = role.trim().toLowerCase();

        String usersFile = XMLHelper.getDataFilePath(getServletContext().getRealPath(""), "users.xml");
        List<Map<String, String>> users = XMLHelper.readAllUsers(usersFile);

        Map<String, String> matchedUser = null;
        for (Map<String, String> u : users) {
            String uRole = u.getOrDefault("role", "student");
            String uRollNo = u.getOrDefault("rollNo", "");
            String uPwd = u.getOrDefault("password", "");

            if (role.equals(uRole) && identifier.equalsIgnoreCase(uRollNo) && password.equals(uPwd)) {
                matchedUser = u;
                break;
            }
        }

        if (matchedUser != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", matchedUser.get("id"));
            session.setAttribute("rollNo", matchedUser.get("rollNo"));
            session.setAttribute("name", matchedUser.get("firstName") + " " + matchedUser.get("lastName"));
            session.setAttribute("branch", matchedUser.getOrDefault("branch", ""));
            session.setAttribute("email", matchedUser.getOrDefault("email", ""));
            session.setAttribute("role", role);

            if ("admin".equals(role)) {
                response.sendRedirect("AdminServlet");
            } else {
                response.sendRedirect("StudentDashboardServlet");
            }
        } else {
            response.sendRedirect("login.html?error=invalid");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.html");
    }
}
