<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="bean.Achievement" %>
<%@ page import="java.util.List" %>
<%
  List<Achievement> achievements = (List<Achievement>) request.getAttribute("achievements");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Friends' Achievements</title>
  <link rel="stylesheet" href="style/achievements.css">
</head>
<body>
<div class="container">
  <div class="header">
    <h1>🏅 Friends' Achievements</h1>
    <p class="subheading">See what your friends have accomplished!</p>
  </div>

  <div class="card-container">
    <%
      if (achievements != null && !achievements.isEmpty()) {
        for (Achievement a : achievements) {
    %>
    <div class="card">
      <div class="card-username">@<%= a.getUsername() %></div>
      <img src="<%= a.getIcon_url() %>" alt="icon" class="card-icon">
      <h2 class="card-title"><%= a.getAchievement_name() %></h2>
      <p class="card-desc"><%= a.getAchievement_descr() %></p>
    </div>
    <%
      }
    } else {
    %>
    <p>No achievements from friends yet.</p>
    <%
      }
    %>
  </div>
</div>
</body>
</html>

