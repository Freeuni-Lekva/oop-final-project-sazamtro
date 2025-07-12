<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.Announcement" %>
<%@ page import="java.text.SimpleDateFormat" %>

<link rel="stylesheet" href="style/cards.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<%
  List<Announcement> announcements = (List<Announcement>) request.getAttribute("announcements");
  SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Announcements</title>
  <link rel="stylesheet" href="style/cards.css">
</head>
<body>

<div class="container">
  <div class="header">
    <h1>Announcements</h1>
    <p class="subheading">See the announcements!</p>
  </div>

  <div class="card-container">
    <% if (announcements != null && !announcements.isEmpty()) {
      for (Announcement a : announcements) {
    %>
    <div class="card announcement-card">
      <div class="announcement-header">
        <h3><i class="fas fa-bullhorn"></i> Announcement</h3>
        <p class="announcement-user">by <%= a.getAdministratorUsername() %></p>
        <span class="announcement-date"><%= formatter.format(a.getDoneAt()) %></span>
      </div>
      <p><%= a.getText() %></p>
    </div>
    <% }
    } else { %>
    <p>No announcements available</p>
    <% } %>
  </div>

</div>
</body>
</html>
