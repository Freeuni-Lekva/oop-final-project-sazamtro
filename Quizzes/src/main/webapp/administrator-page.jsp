<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <title>Administrator Panel</title>
  <link rel="stylesheet" href="style/admin.css">
</head>
<body>
<div class="container">
  <div class="header">
    <h1>Admin Dashboard</h1>
    <p class="subheading">Manage announcements, users, quizzes and stats</p>
  </div>

  <%
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj != null) {
      String msg = (String) sessionObj.getAttribute("message");
      if (msg != null) {
  %>
  <div class="message"><%= msg %></div>
  <%
        sessionObj.removeAttribute("message");
      }
    }
  %>

  <div class="admin-section">

    <!-- Create Announcement -->
    <div class="admin-card">
      <h2>Create Announcement</h2>
      <form action="AdministratorServlet" method="post">
        <input type="hidden" name="action" value="createAnnouncement" />
        <label for="announcementText">Announcement Text:</label>
        <textarea name="text" id="announcementText" rows="4" required></textarea>
        <button class="btn-auth signup" type="submit">Create</button>
      </form>
    </div>

    <!-- Remove User -->
    <div class="admin-card">
      <h2>Remove User</h2>
      <form action="AdministratorServlet" method="post">
        <input type="hidden" name="action" value="removeUser" />
        <label for="usernameRemove">Username (exact):</label>
        <input type="text" name="username" id="usernameRemove" required />
        <button class="btn-auth login" type="submit">Remove User</button>
      </form>
    </div>

    <!-- Remove Quiz -->
    <div class="admin-card">
      <h2>Remove Quiz</h2>
      <form action="AdministratorServlet" method="post">
        <input type="hidden" name="action" value="removeQuiz" />
        <label for="quizTitleRemove">Quiz Title (exact):</label>
        <input type="text" name="quizTitle" id="quizTitleRemove" required />
        <button class="btn-auth login" type="submit">Remove Quiz</button>
      </form>
    </div>

    <!-- Clear Quiz History -->
    <div class="admin-card">
      <h2>Clear Quiz History</h2>
      <form action="AdministratorServlet" method="post">
        <input type="hidden" name="action" value="clearQuizHistory" />
        <label for="quizTitleClear">Quiz Title (exact):</label>
        <input type="text" name="quizTitle" id="quizTitleClear" required />
        <button class="btn-auth login" type="submit">Clear History</button>
      </form>
    </div>

    <!-- Promote User -->
    <div class="admin-card">
      <h2>Promote User to Admin</h2>
      <form action="AdministratorServlet" method="post">
        <input type="hidden" name="action" value="promote" />
        <label for="usernamePromote">Username (exact):</label>
        <input type="text" name="username" id="usernamePromote" required />
        <button class="btn-auth signup" type="submit">Promote</button>
      </form>
    </div>

    <!-- View Site Statistics -->
    <div class="admin-card">
      <h2>View Site Statistics</h2>
      <form action="AdministratorServlet" method="get">
        <input type="hidden" name="action" value="viewStatistics" />
        <button class="btn-auth login" type="submit">View Stats</button>
      </form>

      <% Integer numUsers = (Integer) request.getAttribute("numUsers");
        Integer numAttempts = (Integer) request.getAttribute("numAttempts");
        if (numUsers != null && numAttempts != null) { %>
      <p>Total Users: <strong><%= numUsers %></strong></p>
      <p>Quizzes Taken: <strong><%= numAttempts %></strong></p>
      <% } %>
    </div>

  </div>
</div>
</body>
</html>
