<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, bean.Quiz" %>

<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
%>

<html>
<head>
    <title><%= quiz.getQuizTitle() %></title>
    <link rel="stylesheet" type="text/css" href="/style/quiz_result.css" />
</head>
<body>

<%
    Integer singleScore = (Integer) request.getAttribute("score");
%>

<p><b><%= singleScore %></b></p>

<form action="/HomePageServlet" method="get">
                 <button class="btn">Home</button>
            </form>