<%@ page import="java.util.List" %>
<%@ page import="bean.Quiz" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>All Quizzes</title>
    <link rel="stylesheet" href="style/quizzes.css" /> <!-- Your external CSS -->
</head>
<body>

<jsp:include page="topbar.jsp" />

<div class="page-content">
    <jsp:include page="sidebar.jsp" />

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
        <p>No quizzes available.</p>
        <%
            }
        %>
    </div>
</div>

<script>
    function startQuiz(id) {
        window.location.href = "StartQuizServlet?id=" + id;
    }

    function shareQuiz(id) {
        const link = window.location.origin + "/TakeQuiz.jsp?id=" + id;
        navigator.clipboard.writeText(link);
        alert("Quiz link copied!");
    }
</script>

</body>
</html>
