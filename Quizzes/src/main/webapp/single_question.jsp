<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, bean.Questions.Question, bean.Questions.AnswerOption, bean.Questions.QuestionType, bean.Quiz" %>

<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    String pictureUrl = (String) request.getAttribute("picture");
    Question currQuestion = (Question) request.getAttribute("question");
    List<AnswerOption> answerOptions = (List<AnswerOption>) request.getAttribute("answer_options");
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

    <form method="post" action="/quizzes/question">
        <div class="question-card">
            <div class="question-text"><%= currQuestion.getQuestionText() %></div>

            <% if (pictureUrl != null && !pictureUrl.trim().isEmpty()) { %>
            <div>
                <img src="<%= pictureUrl %>" alt="Question Image"
                     style="max-width: 400px; max-height: 300px; width: auto; height: auto; margin: 10px 0;" />
            </div>
            <% } %>

            <%
                boolean canSelectSeveral = currQuestion.getQuestionType() == QuestionType.MULTI_SELECT;
                String inputType = canSelectSeveral ? "checkbox" : "radio";
                if (!answerOptions.isEmpty()) {
                    for (AnswerOption currAnswer : answerOptions) {
            %>
            <label class="option-label">
                <input type="<%= inputType %>" name="q_<%= currQuestion.getId() %>" value="<%= currAnswer.getAnswerText() %>" />
                <%= currAnswer.getAnswerText() %>
            </label>
            <%
                }
            } else {
            %>
            <input type="text" name="q_<%= currQuestion.getId() %>" />
            <%
                }
            %>
        </div>

        <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>" />

        <div class="form-actions">
            <button type="submit" class="submit-btn">➡️ Next Question</button>
        </div>
    </form>

    <div class="form-actions">
        <a href="/HomePageServlet" class="go-back-btn">🏠 Home</a>
    </div>
</div>

</body>
</html>