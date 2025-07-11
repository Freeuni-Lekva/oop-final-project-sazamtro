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

    <p><b>You scored <%= singleScore %> / <%=maxScore%></b></p>

    <form action="/HomePageServlet" method="get">
        <button class="btn">Home</button>
    </form>

</div>