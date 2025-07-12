<html>
<head>
    <title>Quiz Deleted</title>
    <link rel="stylesheet" type="text/css" href="/style/quiz_deleted.css" />
</head>
<body>

<%
    String quizTitle = (String) request.getAttribute("quiz_title");
%>

<div class="center-box">
    <p>You have successfully deleted Quiz <%= quizTitle %></p>

    <div class="button-container">
        <form action="/HomePageServlet" method="get">
            <button class="btn">Home</button>
        </form>
    </div>
</div>

</body>
</html>