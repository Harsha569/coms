package com.vnrvjiet.complaints;

import java.io.IOException;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class StudentDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null
                || !"student".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        String studentId = (String) session.getAttribute("userId");
        String complaintsFile = XMLHelper.getDataFilePath(getServletContext().getRealPath(""), "complaints.xml");

        List<Map<String, String>> studentComplaints = XMLHelper.getComplaintsByStudent(complaintsFile, studentId);

        request.setAttribute("complaints", studentComplaints);
        request.getRequestDispatcher("/jsp/studentDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
