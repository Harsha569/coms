package com.vnrvjiet.complaints;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

/**
 * ComplaintServlet handles new complaint submissions from students.
 *
 * Reads category, subject, and description from the form,
 * generates a unique complaint ID, stores it in complaints.xml,
 * and forwards to the success page.
 */
public class ComplaintServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Verify student session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null
                || !"student".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        String studentId   = (String) session.getAttribute("userId");
        String studentName = (String) session.getAttribute("name");
        String rollNo      = (String) session.getAttribute("rollNo");
        String branch      = (String) session.getAttribute("branch");
        String email       = (String) session.getAttribute("email");

        String category    = request.getParameter("category");
        String subject     = request.getParameter("subject");
        String description = request.getParameter("description");

        // Validate
        if (category == null || category.isEmpty() ||
            subject == null  || subject.trim().isEmpty() ||
            description == null || description.trim().length() < 20) {

            // Redirect back to dashboard with error
            request.setAttribute("msgType", "danger");
            request.setAttribute("msgText", "Validation failed. Please fill all fields (description must be at least 20 characters).");
            request.getRequestDispatcher("/StudentDashboardServlet").forward(request, response);
            return;
        }

        String complaintsFile = XMLHelper.getDataFilePath(getServletContext().getRealPath(""), "complaints.xml");

        try {
            String complaintId = XMLHelper.generateNextComplaintId(complaintsFile);

            Map<String, String> data = new LinkedHashMap<>();
            data.put("id",          complaintId);
            data.put("studentId",   studentId);
            data.put("studentName", studentName);
            data.put("rollNo",      rollNo);
            data.put("branch",      branch != null ? branch : "");
            data.put("email",       email  != null ? email  : "");
            data.put("category",    category.trim());
            data.put("subject",     subject.trim());
            data.put("description", description.trim());
            data.put("status",      "Pending");
            data.put("date",        LocalDate.now().toString());
            data.put("adminRemark", "");

            XMLHelper.addComplaint(complaintsFile, data);

            // Forward to success page
            request.setAttribute("complaintId", complaintId);
            request.getRequestDispatcher("/jsp/submitSuccess.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msgType", "danger");
            request.setAttribute("msgText", "Server error while submitting complaint. Please try again.");
            request.getRequestDispatcher("/StudentDashboardServlet").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("StudentDashboardServlet");
    }
}
