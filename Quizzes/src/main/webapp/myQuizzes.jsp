<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.Quiz" %>

<html>
<head>
  <title>My Quizzes</title>
  <link rel="stylesheet" href="style/sidebar.css" />
  <link rel="stylesheet" href="style/myQuizzes.css" />
</head>
<body>

<div class="container">
  <%@ include file="sidebar.jsp" %>

  <div class="main-content">
    <div class="main-inner">
      <h1>My Quizzes</h1>

      <%
        List<Quiz> quizzes = (List<Quiz>) request.getAttribute("quizzes");
        if (quizzes == null || quizzes.isEmpty()) {
      %>
      <p>You have no quizzes created yet.</p>
      <%
      } else {
        for (Quiz quiz : quizzes) {
      %>
      <div class="quiz-card">
        <div class="quiz-name"><%= quiz.getQuizTitle() %></div>
        <div class="button-group">
          <a href="editQuiz?quizId=<%= quiz.getQuiz_id() %>" class="btn edit-btn">Edit</a>
          <a href="/gradeOverview?quizId=<%= quiz.getQuiz_id() %>" class="btn grade-btn">Grade</a>
        </div>
      </div>
      <%
          }
        }
      %>
    </div>
  </div>
</div>

</body>
</html>
