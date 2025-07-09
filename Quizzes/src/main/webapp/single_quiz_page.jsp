<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String quizIdParam = request.getParameter("quizId");
    int quizId = -1;
    if (quizIdParam != null) {
        try {
            quizId = Integer.parseInt(quizIdParam);
        } catch (NumberFormatException e) {
            // Invalid number, keep quizId as -1
        }
    }
%>
<html>
<head>
    <title>Quiz ID Display</title>
</head>
<body>
<h2>Quiz ID:</h2>
<p>
    <% if (quizId != -1) { %>
    <%= quizId %>
    <% } else { %>
    Invalid or missing quiz_id parameter.
    <% } %>
</p>
</body>
</html>
