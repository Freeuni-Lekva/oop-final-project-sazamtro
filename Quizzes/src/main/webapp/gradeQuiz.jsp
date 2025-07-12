<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, bean.*" %>
<%@ page import="bean.Questions.Question" %>
<%@ page import="bean.Questions.AnswerOption" %>
<html>
<head>
    <title>Grade Attempt</title>
    <link rel="stylesheet" href="style/gradeQuiz.css" />
</head>
<body>
<div class="container">
    <div style="text-align: center; margin: 20px 0;">
        <h1>Quiz Title: <%= request.getAttribute("quizTitle") %></h1>
        <h3>Grading Attempt by: <%= request.getAttribute("username") %></h3>
    </div>


    <form action="/gradeAttempt" method="post" id="gradeForm">
        <input type="hidden" name="attemptId" value="<%= request.getAttribute("attemptId") %>"/>
        <%
            List<Question> questions = (List<Question>) request.getAttribute("questions");
            Map<Integer, List<AnswerOption>> optionsMap = (Map<Integer, List<AnswerOption>>) request.getAttribute("options");
            Map<Integer, List<String>> correctAnswersMap = (Map<Integer, List<String>>) request.getAttribute("correctAnswers");
            Map<Integer, List<String>> userAnswersMap = (Map<Integer, List<String>>) request.getAttribute("userAnswers");

            int selectIndex = 0;

            for (int j = 0; j < questions.size(); j++) {
                Question q = questions.get(j);
                int qid = q.getId();
                List<AnswerOption> opts = optionsMap.getOrDefault(qid, new ArrayList<AnswerOption>());
                List<String> correct = correctAnswersMap.getOrDefault(qid, new ArrayList<String>());
                List<String> user = userAnswersMap.getOrDefault(qid, new ArrayList<String>());
        %>
        <div class="question-card">
            <div class="question-header">
                <h3>Question <%= j + 1 %>: <%= q.getQuestionText() %></h3>
            </div>

            <% if (q.getImageUrl() != null && !q.getImageUrl().isEmpty()) { %>
            <img src="<%= q.getImageUrl() %>" alt="Question Image" class="question-img"/>
            <% } %>

            <% if (!opts.isEmpty()) { %>
            <ul class="option-list">
                <% for (AnswerOption opt : opts) {
                    String answerText = opt.getAnswerText();
                    boolean isCorrect = correct.contains(answerText);
                    boolean isSelected = user.contains(answerText);
                %>
                <li class="<%= isSelected ? "selected" : "" %>">
                    <div class="option-left">
                        <span><%= answerText %></span>
                        <% if (isCorrect) { %><span class="label-correct">(Correct)</span><% } %>
                        <% if (isSelected) { %><span class="label-user">(Selected)</span><% } %>
                    </div>
                </li>
                <% } %>
            </ul>

            <div class="grade-right">
                <label>
                    <select name="score<%= selectIndex %>" class="score-select">
                        <%
                            int maxScore = correct.size(); // for MULTI_SELECT
                            if (correct.size() == 1) maxScore = 1; // MULTIPLE_CHOICE
                            for (int i = 0; i <= maxScore; i++) {
                        %>
                        <option value="<%= i %>"><%= i %></option>
                        <% } %>
                    </select>
                </label>
            </div>
            <% selectIndex++; %>

            <% } else { %>
            <p><strong>User Answer:</strong> <%= user.isEmpty() ? "—" : user.get(0) %></p>
            <p><strong>Correct Answer:</strong> <%= correct.isEmpty() ? "—" : correct.get(0) %></p>
            <div class="grade-right">
                <label>
                    <select name="score<%= selectIndex %>" class="score-select">
                        <option value="0">0</option>
                        <option value="1">1</option>
                    </select>
                </label>
            </div>
            <% selectIndex++; %>
            <% } %>
        </div>
        <% } %>

        <div class="grading-summary">
            <p><strong>Total Score:</strong> <span id="score">0</span></p>
            <input type="hidden" name="score" id="scoreInput" value="0"/>
            <button type="submit" class="submit-btn">Save Grade</button>
        </div>
    </form>
</div>

<script>
    const form = document.getElementById("gradeForm");
    const selects = form.querySelectorAll(".score-select");
    const scoreDisplay = document.getElementById("score");
    const scoreInput = document.getElementById("scoreInput");

    function updateScore() {
        let total = 0;
        selects.forEach(select => {
            total += parseInt(select.value);
        });
        scoreDisplay.textContent = total;
        scoreInput.value = total;
    }

    selects.forEach(select => {
        select.addEventListener("change", updateScore);
    });

    form.addEventListener("submit", updateScore);
    updateScore(); // On load
</script>
</body>
</html>
