<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="bean.Quiz" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="bean.User" %>
<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    User creator = (User) request.getAttribute("creator");
    bean.User user = (session != null) ? (bean.User) session.getAttribute("user") : null;
    boolean isOwner = (user != null && user.getUserId() == quiz.getCreator_id());
    boolean isAdmin = (user != null && user.checkIfAdmin());
%>
<html>
<body>
    <title><%= quiz.getQuizTitle() %> - Quiz Summary</title>
    <link rel="stylesheet" type="text/css" href="/style/single_quiz_page.css" />
</head>

    <div class="container">
        <h1><%= quiz.getQuizTitle() %></h1>

        <div class="metadata">
            Created by <span class="username"><%= creator != null ? creator.getUsername() : "UNKNOWN" %></span> on <%= quiz.getCreationDate() %><br>
            <% if (quiz.checkIfRandom()) { %> Randomized Questions · <% } %>
            <% if (quiz.checkIfMultipage()) { %> Multi-page · <% } %>
            <% if (quiz.checkIfImmediate_correction()) { %> Immediate Correction <% } %>
        </div>

        <div class="description">
            <%= quiz.getQuizDescription() != null ? quiz.getQuizDescription() : "No description provided." %>
        </div>

        <div class="actions">
            <form action="/HomePageServlet" method="get">
                 <button class="btn">Home</button>
            </form>
            <form action="/quizzes/start" method="get">
                <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>">
                <button class="btn">Start Quiz</button>
            </form>

            <form action="/quizzes/start" method="get">
                <input type="hidden" name="practice" value="true" />
                <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>">
                <button class="btn">Practice Mode</button>
            </form>

            <% if (isOwner || isAdmin) { %>
                <form action="/editQuiz" method="get">
                    <input type="hidden" name="quizId" value="<%= quiz.getQuiz_id() %>">
                    <button class="btn">Edit Quiz</button>
                </form>
                <form action="/quizzes/delete" method="post">
                    <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>">
                    <button class="btn danger">Delete Quiz</button>
                </form>
            <% } %>
        </div>
    </div>
</body>
</html>
