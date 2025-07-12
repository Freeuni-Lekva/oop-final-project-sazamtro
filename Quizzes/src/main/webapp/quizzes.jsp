<%@ page import="java.util.List" %>
<%@ page import="bean.Quiz" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>All Quizzes</title>
    <link rel="stylesheet" href="style/quizzes.css" />
</head>
<body>

    <div class="quizzes-header">
        <h1>QUIZZES</h1>
        <p class="quizzes-subtitle">Explore, take, or share quizzes with your friends!</p>
    </div>

    <div class="quizzes-list">
        <%
            List<Quiz> quizzes = (List<Quiz>) request.getAttribute("quizzes");
            if (quizzes != null) {
                for (Quiz quiz : quizzes) {
                    request.setAttribute("quiz", quiz);
        %>
        <jsp:include page="quizCard.jsp" />
        <%
            }
        } else {
        %>
        <p>No quizzes available</p>
        <%
            }
        %>
    </div>
</body>
</html>
