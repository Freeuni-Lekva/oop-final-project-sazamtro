<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Quiz Attempts - Quiz Options</title>
  <link rel="stylesheet" href="style/quiz-attempts.css">
</head>
<body>
<%
  String quizId = request.getParameter("quizId");
%>
<div class="window-frame">
  <h1>Quiz History</h1>
  <p>Choose what you want to see</p>
  <div class="cards-horizontal">
    <a href="QuizAttemptsPageForQuizServlet?quizId=<%= quizId %>&filter=real" class="option-card card-real">
      Real Attempts
    </a>
    <a href="QuizAttemptsPageForQuizServlet?quizId=<%= quizId %>&filter=all" class="option-card card-all">
      All Attempts
    </a>
    <a href="QuizAttemptsPageForQuizServlet?quizId=<%= quizId %>&filter=practice" class="option-card card-practice">
      Practice Attempts
    </a>
  </div>
</div>
</body>
</html>
