package com.vnrvjiet.complaints;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

public class XMLHelper {
    public static String getDataFilePath(String servletContextPath, String filename) {
        return servletContextPath + File.separator + "WEB-INF" + File.separator + "data" + File.separator + filename;
    }

    public static Document loadDocument(String filePath) throws Exception {
        File file = new File(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }

    public static List<Map<String, String>> readAllUsers(String filePath) {
        List<Map<String, String>> users = new ArrayList<>();
        try {
            Document doc = loadDocument(filePath);
            NodeList nodeList = doc.getElementsByTagName("user");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element el = (Element) nodeList.item(i);
                Map<String, String> user = new LinkedHashMap<>();
                user.put("id", getTagValue(el, "id"));
                user.put("firstName", getTagValue(el, "firstName"));
                user.put("lastName", getTagValue(el, "lastName"));
                user.put("rollNo", getTagValue(el, "rollNo"));
                user.put("branch", getTagValue(el, "branch"));
                user.put("year", getTagValue(el, "year"));
                user.put("email", getTagValue(el, "email"));
                user.put("phone", getTagValue(el, "phone"));
                user.put("password", getTagValue(el, "password"));
                user.put("role", getTagValue(el, "role"));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public static List<Map<String, String>> readAllComplaints(String filePath) {
        List<Map<String, String>> complaints = new ArrayList<>();
        try {
            Document doc = loadDocument(filePath);
            NodeList nodeList = doc.getElementsByTagName("complaint");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element el = (Element) nodeList.item(i);
                Map<String, String> c = new LinkedHashMap<>();
                c.put("id", getTagValue(el, "id"));
                c.put("studentId", getTagValue(el, "studentId"));
                c.put("studentName", getTagValue(el, "studentName"));
                c.put("rollNo", getTagValue(el, "rollNo"));
                c.put("branch", getTagValue(el, "branch"));
                c.put("email", getTagValue(el, "email"));
                c.put("category", getTagValue(el, "category"));
                c.put("subject", getTagValue(el, "subject"));
                c.put("description", getTagValue(el, "description"));
                c.put("status", getTagValue(el, "status"));
                c.put("date", getTagValue(el, "date"));
                c.put("adminRemark", getTagValue(el, "adminRemark"));
                complaints.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return complaints;
    }

    public static synchronized void addUser(String filePath, Map<String, String> userData) throws Exception {
        Document doc = loadDocument(filePath);
        Element root = doc.getDocumentElement();

        Element user = doc.createElement("user");
        for (Map.Entry<String, String> entry : userData.entrySet()) {
            Element child = doc.createElement(entry.getKey());
            child.setTextContent(entry.getValue());
            user.appendChild(child);
        }
        root.appendChild(user);
        saveDocument(doc, filePath);
    }

    public static synchronized void addComplaint(String filePath, Map<String, String> complaintData) throws Exception {
        Document doc = loadDocument(filePath);
        Element root = doc.getDocumentElement();

        Element complaint = doc.createElement("complaint");
        for (Map.Entry<String, String> entry : complaintData.entrySet()) {
            Element child = doc.createElement(entry.getKey());
            child.setTextContent(entry.getValue());
            complaint.appendChild(child);
        }
        root.appendChild(complaint);
        saveDocument(doc, filePath);
    }

    public static synchronized boolean updateComplaintStatus(String filePath, String complaintId,
            String newStatus, String remark) throws Exception {
        Document doc = loadDocument(filePath);
        NodeList nodeList = doc.getElementsByTagName("complaint");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element el = (Element) nodeList.item(i);
            if (complaintId.equals(getTagValue(el, "id"))) {
                setTagValue(el, "status", newStatus);
                setTagValue(el, "adminRemark", remark);
                saveDocument(doc, filePath);
                return true;
            }
        }
        return false;
    }

    public static Map<String, String> findComplaintById(String filePath, String complaintId) {
        List<Map<String, String>> all = readAllComplaints(filePath);
        for (Map<String, String> c : all) {
            if (complaintId.equals(c.get("id"))) {
                return c;
            }
        }
        return null;
    }

    public static List<Map<String, String>> getComplaintsByStudent(String filePath, String studentId) {
        List<Map<String, String>> all = readAllComplaints(filePath);
        List<Map<String, String>> result = new ArrayList<>();
        for (Map<String, String> c : all) {
            if (studentId.equals(c.get("studentId"))) {
                result.add(c);
            }
        }
        return result;
    }

    public static String generateNextComplaintId(String filePath) {
        List<Map<String, String>> all = readAllComplaints(filePath);
        int max = 0;
        for (Map<String, String> c : all) {
            String id = c.getOrDefault("id", "C000");
            try {
                int num = Integer.parseInt(id.substring(1));
                if (num > max)
                    max = num;
            } catch (NumberFormatException ignored) {
            }
        }
        return String.format("C%03d", max + 1);
    }

    public static String generateNextUserId(String filePath) {
        List<Map<String, String>> all = readAllUsers(filePath);
        int max = 0;
        for (Map<String, String> u : all) {
            String id = u.getOrDefault("id", "U000");
            if (id.startsWith("U")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max)
                        max = num;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("U%03d", max + 1);
    }

    public static boolean isRollNoTaken(String filePath, String rollNo) {
        List<Map<String, String>> users = readAllUsers(filePath);
        for (Map<String, String> u : users) {
            if (rollNo.equalsIgnoreCase(u.getOrDefault("rollNo", ""))) {
                return true;
            }
        }
        return false;
    }

    private static String getTagValue(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() > 0 && list.item(0).getTextContent() != null) {
            return list.item(0).getTextContent().trim();
        }
        return "";
    }

    private static void setTagValue(Element parent, String tagName, String value) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            list.item(0).setTextContent(value);
        } else {
            Document doc = parent.getOwnerDocument();
            Element child = doc.createElement(tagName);
            child.setTextContent(value);
            parent.appendChild(child);
        }
    }

    private static void saveDocument(Document doc, String filePath) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }
}
