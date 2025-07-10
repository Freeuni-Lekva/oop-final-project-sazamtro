<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, bean.Questions.Question, bean.Questions.AnswerOption, bean.Quiz" %>
<%
Quiz quiz = (Quiz) request.getAttribute("quiz");
Map<Question, List<AnswerOption>> questionAnswers = (Map<Question, List<AnswerOption>>) request.getAttribute("question_answers");
%>

<html>
<head>
    <title><%= quiz.getQuizTitle() %></title>
    <link rel="stylesheet" type="text/css" href="/style/startQuiz.css" />
</head>
<body>

<h1><%= quiz.getQuizTitle() %></h1>
<h2><%= quiz.getQuizDescription() %></h2>

<form method="post" action="/quizzes/submit">
<%
    for (Map.Entry<Question, List<AnswerOption>> entry : questionAnswers.entrySet()){
        Question q = entry.getKey();
        List<AnswerOption> opts = entry.getValue();
%>
        <p><b><%= q.getQuestionText() %></b></p>
<%
        if(opts.size() != 0){
            for(AnswerOption currOption : opts){
%>
                <input type="radio" name="q_<%= q.getId() %>" value="<%= currOption.getAnswerText() %>" />
                <%= currOption.getAnswerText() %><br/>
<%
            }
        }
        else {
%>
            <input type="text" name="q_<%= q.getId() %>" />
<%        }
    }

%>
<br/>
    <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>" />
    <button type="submit">Submit Quiz</button>
</form>