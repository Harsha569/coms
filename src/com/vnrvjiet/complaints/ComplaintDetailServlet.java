package com.vnrvjiet.complaints;

import java.io.IOException;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class ComplaintDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String complaintId = request.getParameter("id");
        if (complaintId == null || complaintId.trim().isEmpty()) {
            response.sendRedirect("login.html");
            return;
        }

        String complaintsFile = XMLHelper.getDataFilePath(getServletContext().getRealPath(""), "complaints.xml");
        Map<String, String> complaint = XMLHelper.findComplaintById(complaintsFile, complaintId.trim());

        if (complaint == null) {
            String role = (String) session.getAttribute("role");
            if ("admin".equals(role)) {
                response.sendRedirect("AdminServlet");
            } else {
                response.sendRedirect("StudentDashboardServlet");
            }
            return;
        }

        request.setAttribute("complaint", complaint);
        request.getRequestDispatcher("/jsp/complaintDetail.jsp").forward(request, response);
    }
}
