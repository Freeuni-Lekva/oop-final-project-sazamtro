<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, bean.Questions.Question, bean.Questions.AnswerOption, bean.Questions.QuestionType, bean.Quiz" %>

<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    Question currQuestion = (Question)request.getAttribute("question");
    List<AnswerOption> answerOptions = (List<AnswerOption>)request.getAttribute("answer_options");
%>

<html>
<head>
    <title><%= quiz.getQuizTitle() %></title>
    <link rel="stylesheet" type="text/css" href="/style/single_question.css" />
</head>
<body>

<%
    boolean showScorePerQuestion = quiz.checkIfImmediate_correction();
    Integer singleScore = (Integer) session.getAttribute("single_ques_score");
    String nextAction = showScorePerQuestion ? "/SingleQuestionScoreServlet" : "/quizzes/question";
%>

<%
%>

<h1><%= quiz.getQuizTitle() %></h1>
<h2><%= quiz.getQuizDescription() %></h2>
<form method="post" action="/quizzes/question">

<p><b><%= currQuestion.getQuestionText() %></b></p>

<%
    boolean canSelectSeveral = currQuestion.getQuestionType() == QuestionType.MULTI_SELECT;
    String questionId = "q_" + currQuestion.getId();
    if (answerOptions.size() != 0) {
        for (int i = 0; i < answerOptions.size(); i++) {
            AnswerOption currAnswer = answerOptions.get(i);
            String inputType = canSelectSeveral ? "checkbox" : "radio";
            String inputName = questionId;
%>
            <input type="<%= inputType %>" name="q_<%= currQuestion.getId() %>" value="<%= currAnswer.getAnswerText() %>"/>
            <%= currAnswer.getAnswerText() %><br/>
<%
        }
    } else {
%>
        <input type="text" name="q_<%= currQuestion.getId() %>"/>
<%
    }
%>
<br/>
    <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>" />
    <button type="submit">Next Question</button>

    <form action="/HomePageServlet" method="get">
        <button class="btn">Home</button>
    </form>

</form>
