<%@ page import="java.util.List" %>
<%@ page import="bean.Quiz" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  List<Quiz> quizzes = (List<Quiz>) request.getAttribute("quizzes");
  String searchTerm = request.getParameter("search");
%>
<html>
<head>
  <title>Manage Quizzes</title>
  <link rel="stylesheet" href="style/admin-page.css" />
  <link rel="stylesheet" href="style/quizzes-admin.css" />

</head>
<body>
<div class="page-header">
  <h1>Manage Quizzes</h1>
  <p>Remove or clear quiz attempts. Search quizzes by title.</p>

  <form method="get" action="/showAllQuizzes" class="search-bar">
    <input type="text" name="search" placeholder="Search by title..." value="<%= searchTerm != null ? searchTerm : "" %>"/>
    <button type="submit" class="btn btn-clear">Search</button>
  </form>
</div>

<div class="card-grid">
  <% if (quizzes != null && !quizzes.isEmpty()) {
    for (Quiz quiz : quizzes) {
      String title = quiz.getQuizTitle();
      if (searchTerm == null || title.toLowerCase().contains(searchTerm.toLowerCase())) {
  %>
  <div class="quiz-card">
    <h3><%= title %></h3>
    <p>Author ID: <%= quiz.getCreator_id() %></p>

    <form action="/ClearQuizAttemptsServlet" method="post">
      <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>" />
      <button class="btn btn-clear" type="submit">Clear Attempts</button>
    </form>

    <form action="/quizzes/delete?mode=admin" method="post" onsubmit="return confirm('Are you sure you want to delete this quiz?');">
      <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>" />
      <button class="btn btn-remove" type="submit">Remove</button>
    </form>
  </div>
  <%  } } } else { %>
  <div class="quiz-card">
    <p>No quizzes found.</p>
  </div>
  <% } %>
</div>

<div class="center-button">
  <a href="/admin.jsp">← Return to Admin Page</a>
</div>
</body>
</html>
