package com.vnrvjiet.complaints;

import java.io.IOException;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

/**
 * AdminServlet serves the admin dashboard and handles complaint status updates.
 *
 * GET  → loads all complaints and forwards to adminDashboard.jsp
 * POST → updates complaint status & remark, then reloads the dashboard
 */
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verify admin session
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        String complaintsFile = XMLHelper.getDataFilePath(getServletContext().getRealPath(""), "complaints.xml");
        List<Map<String, String>> allComplaints = XMLHelper.readAllComplaints(complaintsFile);

        request.setAttribute("complaints", allComplaints);
        request.getRequestDispatcher("/jsp/adminDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Verify admin session
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        String action = request.getParameter("action");

        if ("updateStatus".equals(action)) {
            String complaintId = request.getParameter("complaintId");
            String newStatus   = request.getParameter("status");
            String remark      = request.getParameter("remark");

            if (complaintId == null || newStatus == null) {
                response.sendRedirect("AdminServlet");
                return;
            }

            String complaintsFile = XMLHelper.getDataFilePath(getServletContext().getRealPath(""), "complaints.xml");

            try {
                boolean updated = XMLHelper.updateComplaintStatus(
                    complaintsFile,
                    complaintId.trim(),
                    newStatus.trim(),
                    remark != null ? remark.trim() : ""
                );

                if (updated) {
                    request.setAttribute("msgType", "success");
                    request.setAttribute("msgText", "Complaint #" + complaintId + " updated to \"" + newStatus + "\".");
                } else {
                    request.setAttribute("msgType", "danger");
                    request.setAttribute("msgText", "Complaint #" + complaintId + " not found.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("msgType", "danger");
                request.setAttribute("msgText", "Server error while updating complaint.");
            }
        }

        // Reload dashboard
        doGet(request, response);
    }
}
