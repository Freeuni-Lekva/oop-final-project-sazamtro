<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, bean.Questions.Question, bean.Questions.AnswerOption, bean.Questions.QuestionType, bean.Quiz" %>

<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    Question currQuestion = (Question) request.getAttribute("question");
    List<AnswerOption> answerOptions = (List<AnswerOption>) request.getAttribute("answer_options");
%>

<html>
<head>
    <title><%= quiz.getQuizTitle() %></title>
    <link rel="stylesheet" type="text/css" href="/style/single_question.css" />
    <style>
        .button-container {
            margin-top: 20px;
            display: flex;
            gap: 10px;
        }
    </style>
</head>
<body>

<%
    boolean showScorePerQuestion = quiz.checkIfImmediate_correction();
    Integer singleScore = (Integer) session.getAttribute("single_ques_score");
%>

<h1><%= quiz.getQuizTitle() %></h1>
<h2><%= quiz.getQuizDescription() %></h2>

<form method="post" action="/quizzes/question">
    <p><b><%= currQuestion.getQuestionText() %></b></p>

    <%
        boolean canSelectSeveral = currQuestion.getQuestionType() == QuestionType.MULTI_SELECT;
        String questionId = "q_" + currQuestion.getId();
        if (!answerOptions.isEmpty()) {
            for (AnswerOption currAnswer : answerOptions) {
                String inputType = canSelectSeveral ? "checkbox" : "radio";
    %>
                <input type="<%= inputType %>" name="q_<%= currQuestion.getId() %>" value="<%= currAnswer.getAnswerText() %>"/>
                <%= currAnswer.getAnswerText() %><br/>
    <%
            }
        } else {
    %>
            <input type="text" name="q_<%= currQuestion.getId() %>" />
    <%
        }
    %>

    <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>" />

    <div class="button-container">
        <button type="submit">Next Question</button>
    </div>
</form>

<div class="button-container">
    <form action="/HomePageServlet" method="get">
        <button type="submit" class="btn">Home</button>
    </form>
</div>

</body>
</html>
