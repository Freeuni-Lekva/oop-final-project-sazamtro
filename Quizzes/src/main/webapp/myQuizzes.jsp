<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.Quiz" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>My Quizzes</title>
  <link rel="stylesheet" href="style/myQuizzes.css" />
</head>
<body>

<div class="container">
  <h1>My Quizzes</h1>
  <p class="subheading">Manage your quizzes below:</p>

  <div class="card-container">
    <%
      List<Quiz> quizzes = (List<Quiz>) request.getAttribute("quizzes");
      if (quizzes != null && !quizzes.isEmpty()) {
        for (Quiz quiz : quizzes) {
    %>
    <div class="card">
      <div class="card-title"><%= quiz.getQuizTitle() %></div>
      <div class="button-group">
        <a href="editQuiz?quizId=<%= quiz.getQuiz_id() %>" class="btn edit-btn">Edit</a>
        <a href="/gradeOverview?quizId=<%= quiz.getQuiz_id() %>" class="btn grade-btn">Grade</a>
      </div>
    </div>
    <%
      }
    } else {
    %>
    <p style="text-align:center; font-size: 18px; color: #555;">You have no quizzes created yet.</p>
    <% } %>
  </div>

  <a href="HomePageServlet" class="home-button">HOME</a>
</div>

</body>
</html>
