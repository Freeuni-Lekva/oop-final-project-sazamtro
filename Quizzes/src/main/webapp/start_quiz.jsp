<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, bean.Questions.Question, bean.Questions.AnswerOption, bean.Quiz, bean.Questions.QuestionType" %>
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
<div class="quiz-container">

    <h1><%= quiz.getQuizTitle() %></h1>
    <h2><%= quiz.getQuizDescription() %></h2>

    <form method="post" action="/quizzes/submit">
    <%
        for (Map.Entry<Question, List<AnswerOption>> entry : questionAnswers.entrySet()) {
            Question q = entry.getKey();
            List<AnswerOption> opts = entry.getValue();
    %>
        <div class="question-block">
            <p><%= q.getQuestionText() %></p>
    <%
            boolean canSelectSeveral = q.getQuestionType() == QuestionType.MULTI_SELECT;
            String inputType = canSelectSeveral ? "checkbox" : "radio";
            for (AnswerOption currAnswer : opts) {
    %>
            <label>
                <input type="<%= inputType %>" name="q_<%= q.getId() %>" value="<%= currAnswer.getAnswerText() %>" />
                <%= currAnswer.getAnswerText() %>
            </label><br/>
    <%
            }
            if (opts.size() == 0) {
    %>
            <input type="text" name="q_<%= q.getId() %>" />
    <%
            }
    %>
        </div>
    <%
        }
    %>
        <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>" />
        <button type="submit">Submit Quiz</button>
    </form>

    <form action="/HomePageServlet" method="get">
        <button class="btn" type="submit">Home</button>
    </form>

</div>
</body>
</html>
