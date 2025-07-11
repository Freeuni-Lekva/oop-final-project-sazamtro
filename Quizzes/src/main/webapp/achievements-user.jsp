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
  <title>My Achievements</title>
  <link rel="stylesheet" href="style/achievements.css">
</head>
<body>
<div class="container">
  <div class="header">
    <h1>My Achievements</h1>
    <%
      if (achievements != null && !achievements.isEmpty()) {
    %>
    <p class="subheading">Congratulations on what you've earned so far!</p>
    <%
      }
    %>
  </div>
  <div class="card-container">
    <%
      if (achievements != null && !achievements.isEmpty()) {
        for (Achievement a : achievements) {
    %>
    <div class="card">
      <img src="<%= a.getIcon_url() %>" alt="icon" class="card-icon">
      <h2 class="card-title"><%= a.getAchievement_name() %></h2>
      <p class="card-desc"><%= a.getAchievement_descr() %></p>
    </div>
    <%
      }
    } else {
    %>
    <p>You don't have any achievements yet. Keep going!</p>
    <%
      }
    %>
  </div>
  <div style="text-align: center;">
    <a href="HomePageServlet" class="home-button">HOME</a>
  </div>
</div>
</body>
</html>