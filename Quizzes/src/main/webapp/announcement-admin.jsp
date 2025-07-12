<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.Announcement" %>

<!DOCTYPE html>
<html>
<head>
  <title>Manage Announcements</title>
  <link rel="stylesheet" href="style/admin-page.css">
  <link rel="stylesheet" href="style/announcement-admin.css">
</head>
<body>
<div class="container">
  <header class="header">
    <h1>Manage <span class="brand">Announcements</span></h1>
    <p class="subheading">Edit or delete announcements, and create new ones.</p>
  </header>

  <div class="announcement-admin">
    <!-- Left: Existing Announcements -->
    <div class="announcement-list">
      <%
        List<Announcement> announcements = (List<Announcement>) request.getAttribute("announcements");
        if (announcements != null && !announcements.isEmpty()) {
          for (Announcement a : announcements) {
      %>
      <div class="announcement-item">
        <form method="post" action="/EditAnnouncementServlet" class="announcement-form">
          <input type="hidden" name="announcementId" value="<%= a.getId() %>">
          <textarea name="announcementText"><%= a.getText() %></textarea>
          <div class="announcement-actions">
            <button class="btn-save" type="submit">💾</button>
          </div>
        </form>

        <form method="post" action="/DeleteAnnouncementServlet" class="announcement-actions">
          <input type="hidden" name="announcementId" value="<%= a.getId() %>">
          <button class="btn-delete" type="submit">🗑️</button>
        </form>
      </div>
      <%
        }
      } else {
      %>
      <p>No announcements available.</p>
      <%
        }
      %>
    </div>

    <!-- Right: New Announcement Form -->
    <div class="new-announcement">
      <form action="/CreateAnnouncementServlet" method="post">
        <textarea name="announcementText" placeholder="Write a new announcement..."></textarea>
        <button type="submit">📢 Post Announcement</button>
      </form>
    </div>
  </div>

  <footer class="auth-box">
    <a href="admin.jsp" class="auth-card">
      <p class="auth-title">Return to Admin Page</p>
      <span class="btn-auth">Return To Admin Page</span>
    </a>
  </footer>
</div>
</body>
</html>
