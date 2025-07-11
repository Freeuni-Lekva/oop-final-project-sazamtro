
<title>Quiz Deleted</title>

<%
    String quizTitle = (String) request.getAttribute("quiz_title");
%>

<p>You have successfully deleted Quiz <%= quizTitle %></p>

<form action="/HomePageServlet" method="get">
                 <button class="btn">Home</button>
</form>