<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.QuizAttempt" %>
<%@ page import="java.util.Map" %>

<html>
<head>
  <title>Quiz Attempts</title>
  <link rel="stylesheet" href="style/quiz-attempts.css" />
  <link rel="stylesheet" href="style/gradeOverview.css">
</head>
<body>
<div class="container">
  <h1>Quiz Attempts</h1>

  <%
    Map<QuizAttempt, String> attempts = (Map<QuizAttempt, String>) request.getAttribute("attempts");
    if (attempts == null || attempts.isEmpty()) {
  %>
  <p>No one has attempted this quiz yet.</p>
  <%
  } else {
    for (QuizAttempt a : attempts.keySet()) {
  %>
  <a href="gradeAttempt?attemptId=<%= a.getAttemptId() %>" class="card-link">
    <div class="attempt-box">
      <div class="tooltip">Grade attempt</div>
      <p><strong>Username:</strong> <%= attempts.get(a) %></p>
      <p><strong>Submitted At:</strong> <%= a.getTakenAt() %></p>
    </div>
  </a>
  <%
      }
    }
  %>
</div>
</body>
</html>
