# Complaint Data Storage in COMS

This document explains the data storage mechanism for complaints within the College Complaint Management System (COMS). 

Instead of a traditional relational database (like MySQL or PostgreSQL), the system uses an **XML-based flat-file storage mechanism**. This makes the application lightweight and easy to deploy without needing external database servers.

## 1. Physical Location in Source Code

During development, the complaints are stored in a dedicated data directory inside the `WEB-INF` folder. Since files in `WEB-INF` cannot be accessed directly from the web via a browser, this provides a basic layer of security.

- **Exact Source Path:** `WebContent/WEB-INF/data/complaints.xml`

## 2. Runtime Location (Deployment)

When you deploy your application to a servlet container like Apache Tomcat, the application context relies on the exact runtime path to find the file.

The Java utility class `XMLHelper.java` dynamically calculates the absolute path on the server where the file resides. 

- **Calculated Server Path:** `<TOMCAT_HOME>\webapps\<your-app-context>\WEB-INF\data\complaints.xml`

If you delete or modify the source file, it will not affect the deployed application until you rebuild and redeploy it. Conversely, if users create complaints while the server is running, the complaints are saved to the Tomcat `webapps` directory, **not** your source code directory.

## 3. How the Data is Accessed

All data read/write operations are managed by `com.vnrvjiet.complaints.XMLHelper`.

- **Fetching Path:** The method `XMLHelper.getDataFilePath()` takes the server's root path and builds the directory string:
  ```java
  public static String getDataFilePath(String servletContextPath, String filename) {
      return servletContextPath + File.separator + "WEB-INF" + File.separator + "data" + File.separator + filename;
  }
  ```
- **Adding Complaints:** `XMLHelper.addComplaint()` parses the XML file into a DOM Document, appends a new `<complaint>` element, and reserializes the tree back to the file.
- **Updating Status:** `XMLHelper.updateComplaintStatus()` traverses the tree to find the correct `id`, modifies the `<status>` and `<adminRemark>` fields, and overwrites the file.

## 4. XML Structure

The `complaints.xml` file uses a straightforward node-based structure. Below is an example of what a populated file looks like after a student submits a complaint and an admin responds:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<complaints>
  <complaint>
    <id>C001</id>
    <studentId>U002</studentId>
    <studentName>John Doe</studentName>
    <rollNo>20071A0501</rollNo>
    <branch>CSE</branch>
    <email>john.doe@example.com</email>
    <category>Academic</category>
    <subject>Incomplete Lab Syllabus</subject>
    <description>The week 4 assignments were not covered during the lab hours.</description>
    <status>Resolved</status>
    <date>2026-04-30 10:45 AM</date>
    <adminRemark>We have scheduled an extra lab session on Saturday to cover this.</adminRemark>
  </complaint>
</complaints>
```

> [!WARNING]
> Because it uses an XML file, the `XMLHelper.java` synchronizes the `addComplaint` and `updateComplaintStatus` methods to prevent file corruption from concurrent read/write operations. However, this is not suitable for high-traffic production environments, where a real database would be necessary.
