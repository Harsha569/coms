# College Complaint System (VNRVJIET)

A simple, user-friendly Java web application built to allow students to log grievances, track their statuses, and enable college administrators to resolve them. 

This project was built intentionally with straightforward Java Servlets and raw HTML/CSS/JSPs, making it extremely easy to understand for beginner and novice web developers.

## Features
* **Student Portal:** Students can register using their Roll Number, submit complaints across various categories (Academic, Hostel, Canteen, etc.), and view their statuses.
* **Admin Portal:** Administrators can view all complaints systematically and update their resolution statuses, providing feedback directly to the students.
* **Database-free:** Uses **XML file processing (DOM Parser)** for complete data persistence instead of requiring SQL database setup. 

## Technology Stack
* **Frontend:** HTML5, CSS3 (Styling), JavaScript (Client-side Validation)
* **Backend:** Java EE (Servlets), JSP (Dynamic Web Pages)
* **Data Storage:** XML (`users.xml` & `complaints.xml`)
* **Server:** Apache Tomcat 10.x

## Prerequisites
1. **Java Development Kit (JDK 17+)** installed.
2. **Apache Tomcat 10.x** installed.

## Setup & Deployment Instructions

### 1. Compile the Backend (Java Servlets)
Ensure you compile the Java files against the Tomcat 10 `jakarta.servlet` API.

Open your terminal in the project root folder and run:
```bash
# Example compilation command pointing to your Tomcat lib path
javac -cp "C:\path\to\your\apache-tomcat-10.x\lib\servlet-api.jar" -d WebContent\WEB-INF\classes src\com\vnrvjiet\complaints\*.java
```

### 2. Deploy to Tomcat
1. Copy the entire `WebContent` folder from this repository.
2. Navigate to your Tomcat's `webapps/` folder.
3. Paste the folder inside and rename it to `complaints` (or whatever Context Path you desire).
    * Resulting path should look like: `apache-tomcat-10.x/webapps/complaints/`

### 3. Run the Application
1. Start your Tomcat server (e.g., execute `bin/startup.bat` on Windows).
2. Open your web browser and navigate to:
   ```
   http://localhost:8080/complaints/
   ```

## Default Setup Credentials
By default, the Administrative account is pre-seeded in the XML file. 

* **Admin Username:** `admin`
* **Admin Password:** `admin123`

Students must manually register on the landing page to create their accounts.
