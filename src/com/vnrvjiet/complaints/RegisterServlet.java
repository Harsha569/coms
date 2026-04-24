package com.vnrvjiet.complaints;

import java.io.IOException;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

/**
 * RegisterServlet handles new student registration.
 *
 * Validates input, checks for duplicate roll numbers,
 * generates a unique user ID, and appends the user to users.xml.
 */
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String firstName = request.getParameter("firstName");
        String lastName  = request.getParameter("lastName");
        String rollNo    = request.getParameter("rollNo");
        String branch    = request.getParameter("branch");
        String year      = request.getParameter("year");
        String email     = request.getParameter("email");
        String phone     = request.getParameter("phone");
        String password  = request.getParameter("password");

        // Basic server-side validation
        if (firstName == null || firstName.trim().isEmpty() ||
            lastName == null  || lastName.trim().isEmpty()  ||
            rollNo == null    || rollNo.trim().isEmpty()    ||
            branch == null    || branch.trim().isEmpty()    ||
            year == null      || year.trim().isEmpty()      ||
            email == null     || email.trim().isEmpty()     ||
            phone == null     || phone.trim().isEmpty()     ||
            password == null  || password.length() < 6) {

            response.sendRedirect("register.html?error=validation");
            return;
        }

        String usersFile = XMLHelper.getDataFilePath(getServletContext().getRealPath(""), "users.xml");

        // Check for duplicate roll number
        if (XMLHelper.isRollNoTaken(usersFile, rollNo.trim())) {
            response.sendRedirect("register.html?error=duplicate");
            return;
        }

        try {
            String userId = XMLHelper.generateNextUserId(usersFile);

            Map<String, String> userData = new LinkedHashMap<>();
            userData.put("id",        userId);
            userData.put("firstName", firstName.trim());
            userData.put("lastName",  lastName.trim());
            userData.put("rollNo",    rollNo.trim().toUpperCase());
            userData.put("branch",    branch.trim());
            userData.put("year",      year.trim());
            userData.put("email",     email.trim());
            userData.put("phone",     phone.trim());
            userData.put("password",  password);   // In production, this should be hashed
            userData.put("role",      "student");

            XMLHelper.addUser(usersFile, userData);

            // Auto-login after registration
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", userId);
            session.setAttribute("rollNo", rollNo.trim().toUpperCase());
            session.setAttribute("name", firstName.trim() + " " + lastName.trim());
            session.setAttribute("branch", branch.trim());
            session.setAttribute("email", email.trim());
            session.setAttribute("role", "student");

            response.sendRedirect("StudentDashboardServlet");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("register.html?error=server");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("register.html");
    }
}
