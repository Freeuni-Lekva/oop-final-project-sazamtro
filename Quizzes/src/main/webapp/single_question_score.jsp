<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="bean.Quiz" %>
<%
    Integer singleScore = (Integer) session.getAttribute("single_ques_score");
    Quiz quiz = (Quiz) session.getAttribute("quiz");
    int currIndex = (Integer) session.getAttribute("current_index");
    int totalQuestions = ((java.util.List<?>) session.getAttribute("quiz_questions")).size();
%>
<html>
<head>
    <title>Question Result</title>
    <link rel="stylesheet" type="text/css" href="/style/single_question.css" />
</head>
<body>

<h1><%= quiz.getQuizTitle() %></h1>
<h2>Question <%= currIndex + 1 %> of <%= totalQuestions %></h2>

<p style="font-size: 18px;">
    You got <strong><%= singleScore %></strong> point<%= (singleScore == 1 ? "" : "s") %> on this question.
</p>

<form method="post" action="/quizzes/question">
    <input type="hidden" name="advance" value="true" />
    <button type="submit">Next Question</button>
</form>

</body>
</html>
