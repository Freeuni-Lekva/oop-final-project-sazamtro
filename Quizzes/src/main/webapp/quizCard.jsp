<%@ page import="bean.Quiz" %>
<%
    bean.Quiz quiz = (bean.Quiz) request.getAttribute("quiz");
%>

<style>
    .quiz-card {
        border: 1px solid #ccc;
        padding: 16px;
        margin: 12px;
        border-radius: 12px;
        background-color: #f9f9f9;
        box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        width: 300px;
        display: inline-block;
        vertical-align: top;
        text-align: center;
    }

    .quiz-card h3 {
        margin-top: 0;
        font-size: 20px;
        color: #333;
    }

    .quiz-card button {
        margin: 6px;
        padding: 8px 16px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        background-color: #007BFF;
        color: white;
        font-weight: bold;
    }

    .quiz-card button:hover {
        background-color: #0056b3;
    }
</style>

<div class="quiz-card">
    <h3><%= quiz.getQuizTitle() %></h3>
    <button onclick="startQuiz(<%= quiz.getQuiz_id() %>)">Start</button>
    <button onclick="shareQuiz(<%= quiz.getQuiz_id() %>)">Share</button>
</div>
