<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="bean.Quiz" %>

<%
Quiz quiz = (Quiz) request.getAttribute("quiz");
%>


<html>
<head>
    <title> Successful Practice </title>
    <link rel="stylesheet" type="text/css" href="/style/finished.css" />
</head>
<body>

<div class="result-box">

    <p><b>Congratulations, you have already answered all the questions correctly from this quiz!</b></p>

    <form action="/HomePageServlet" method="get">
        <button class="btn">Home</button>
    </form>

    <form action="/quizzes/start" method="get">
        <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>" />
        <button class="btn">Start Normal Mode</button>
    </form>

</div>