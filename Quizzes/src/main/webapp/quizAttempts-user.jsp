<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List, bean.QuizAttempt" %>
<%
  List<QuizAttempt> attempts = (List<QuizAttempt>) request.getAttribute("attempts");
  String filter = (String) request.getAttribute("filter");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Your Attempts</title>
  <link rel="stylesheet" href="style/quiz-attempts.css">
</head>
<body>
<div class="container">
  <div class="header">
    <h1 class="brand">Your Attempts - <%= filter.toUpperCase() %></h1>
    <p class="subheading">Below are your attempts.</p>
  </div>

  <div class="card-slider-wrapper">
    <div class="card-container">
      <% for (QuizAttempt attempt : attempts) { %>
      <div class="card">
        <div class="card-title">Quiz ID: <%= attempt.getQuizId() %></div>
        <div class="card-desc">
          Score: <%= attempt.getScore() %><br/>
          Time Taken: <%= attempt.getTimeTakenMin() %> min<br/>
          Type: <%= attempt.isPractice() ? "Practice" : "Real" %><br/>
          Taken At: <%= attempt.getTakenAt() %>
        </div>
      </div>
      <% } %>
    </div>
  </div>
</div>
</body>
</html>
