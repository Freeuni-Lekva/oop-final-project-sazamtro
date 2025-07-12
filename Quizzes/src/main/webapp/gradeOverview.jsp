<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="bean.QuizAttempt" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Quiz Attempts</title>
  <link rel="stylesheet" href="style/achievements.css" />

</head>
<body>
<div class="container">
  <div class="header">
    <h1 class="quiz-title">Quiz Attempts</h1>
    <%
      Map<QuizAttempt, String> attempts = (Map<QuizAttempt, String>) request.getAttribute("attempts");
      if (attempts == null || attempts.isEmpty()) {
    %>
    <p class="subheading">No one has attempted this quiz yet.</p>
    <%
    } else {
    %>
    <p class="subheading">View the quiz attempts below:</p>
    <%
      }
    %>
  </div>
  <div class="card-container">
    <%
      if (attempts != null && !attempts.isEmpty()) {
        for (QuizAttempt a : attempts.keySet()) {
    %>
    <a href="gradeAttempt?attemptId=<%= a.getAttemptId() %>" class="card-link">
      <div class="card">
        <h2 class="card-title"><span class="username">by <%= attempts.get(a) %></span></h2>
        <p class="card-desc">Submitted at <%= a.getTakenAt() %></p>
      </div>
    </a>
    <%
      }
    } else {
    %>
    <p>No attempts yet!</p>
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
