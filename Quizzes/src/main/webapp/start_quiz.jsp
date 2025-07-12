<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, bean.Questions.Question, bean.Questions.AnswerOption, bean.Quiz, bean.Questions.QuestionType" %>
<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    Map<Question, List<AnswerOption>> questionAnswers = (Map<Question, List<AnswerOption>>) request.getAttribute("question_answers");
%>
<html>
<head>
    <title><%= quiz.getQuizTitle() %></title>
    <link rel="stylesheet" href="/style/startQuiz.css" />
</head>
<body>
<div class="container">
    <h1><%= quiz.getQuizTitle() %></h1>
    <h2><%= quiz.getQuizDescription() %></h2>

    <form method="post" action="/quizzes/submit">
        <% for (Map.Entry<Question, List<AnswerOption>> entry : questionAnswers.entrySet()) {
            Question q = entry.getKey();
            List<AnswerOption> opts = entry.getValue();
        %>
        <div class="question-card">
            <h2>Question</h2>
            <p class="question-text"><%= q.getQuestionText() %></p>

            <% if (q.getQuestionType() == QuestionType.MULTIPLE_CHOICE || q.getQuestionType() == QuestionType.MULTI_SELECT) {
                boolean multi = q.getQuestionType() == QuestionType.MULTI_SELECT;
                String inputType = multi ? "checkbox" : "radio";
                for (AnswerOption option : opts) { %>
            <label class="option-label">
                <input type="<%= inputType %>" name="q_<%= q.getId() %><%= multi ? "[]" : "" %>" value="<%= option.getAnswerText() %>">
                <%= option.getAnswerText() %>
            </label>
            <%   }
            } else { %>
            <input type="text" name="q_<%= q.getId() %>" placeholder="Your answer" />
            <% } %>
        </div>
        <% } %>

        <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>" />

        <div class="form-actions">
            <button type="submit" class="submit-btn">🚀 Submit Quiz</button>
            <a href="/HomePageServlet" class="go-back-btn">🏠 Home</a>
        </div>
    </form>
</div>
</body>
</html>
