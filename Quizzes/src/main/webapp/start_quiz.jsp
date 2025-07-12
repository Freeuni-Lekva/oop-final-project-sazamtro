<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, bean.Questions.Question, bean.Questions.AnswerOption, bean.Quiz, bean.Questions.QuestionType" %>
<%

    Quiz quiz = (Quiz) request.getAttribute("quiz");
    List<String> pictures = (List<String>) request.getAttribute("pictures");
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

        <%
            int i = 0;
            for (Map.Entry<Question, List<AnswerOption>> entry : questionAnswers.entrySet()) {
                String pictureUrl = pictures.get(i);
                i++;
                Question q = entry.getKey();
                List<AnswerOption> opts = entry.getValue();
        %>
        <div class="question-block">
            <p><%= q.getQuestionText() %></p>

            <% if (pictureUrl != null && !pictureUrl.trim().isEmpty()) { %>
            <div>
                <img src="<%= pictureUrl %>" alt="Question Image"
                     style="max-width: 400px; max-height: 300px; width: auto; height: auto; display: block; margin: 10px 0;" />
            </div>
            <% } %>

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