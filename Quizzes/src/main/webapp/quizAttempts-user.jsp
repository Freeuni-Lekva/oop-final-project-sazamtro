<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List, bean.QuizAttempt" %>
<%
  List<QuizAttempt> attempts = (List<QuizAttempt>) request.getAttribute("attempts");
  String filter = (String) request.getAttribute("filter");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Your Attempts</title>
  <link rel="stylesheet" href="style/quiz-attempts-p2.css">
</head>
<body>
<div class="container">
  <div class="header">
    <h1>Your Attempts</h1>
    <p class="subheading">Here you can see your attempts:</p>
  </div>

  <div class="card-container">
    <% if (attempts != null && !attempts.isEmpty()) {
      for (QuizAttempt attempt : attempts) { %>
    <div class="card">
      <h2 class="card-title">Quiz: <%= attempt.getQuizId() %></h2>
      <p class="card-desc">
        Score: <%= attempt.getScore() %><br/>
        Time Taken: <%= attempt.getTimeTakenMin() %> min<br/>
        Taken At: <%= attempt.getTakenAt() %>
      </p>
    </div>
    <% }
    } else { %>
    <p>You don’t have any attempts yet. Try a quiz!</p>
    <% } %>
  </div>

  <div style="text-align: center; margin-top: 30px;">
    <a href="HomePageServlet" class="home-button">HOME</a>
    <a href="QuizAttemptsPageForUserServlet" class="home-button">GO BACK</a>
  </div>
</div>
</body>
</html>
