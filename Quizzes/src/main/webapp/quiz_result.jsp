<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, bean.Quiz" %>

<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    String title = quiz.getQuizTitle();
    Integer singleScore = (Integer) request.getAttribute("score");
    Integer maxScore = (Integer) request.getAttribute("max_score");
%>

<html>
<head>
    <title><%= title %></title>
    <link rel="stylesheet" type="text/css" href="/style/quiz_result.css" />
</head>
<body>

<div class="result-box">

    <p><b>You scored <%= singleScore %> / <%= maxScore %></b></p>

    <form action="/HomePageServlet" method="get">
        <button class="btn home-btn">Home</button>
    </form>

    <form action="/quizzes/start" method="get">
        <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>" />
        <button class="btn again-btn">Again</button>
    </form>

    <form action="/quizzes/show" method="get">
        <input type="hidden" name="quizId" value="<%= quiz.getQuiz_id() %>" />
        <button class="btn summary-btn">Summary</button>
    </form>

</div>

</body>
</html>
