<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Quiz Attempts Options</title>
  <link rel="stylesheet" href="style/quiz-attempts.css">
</head>

<body>
<div class="options-frame">
  <h1>My Quiz Attempts</h1>
  <p>Choose what you want to see</p>

  <div class="auth-cards">
    <a href="QuizAttemptsListForUserServlet?filter=real" class="option-card card-real">Real Attempts</a>

    <a href="QuizAttemptsListForUserServlet?filter=all" class="option-card card-all">All Attempts</a>

    <a href="QuizAttemptsListForUserServlet?filter=practice" class="option-card card-practice">Practice Attempts</a>
  </div>
</div>
</body>

</html>
