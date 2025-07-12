<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Site Statistics</title>
    <link rel="stylesheet" type="text/css" href="style/statistics-page.css">
</head>
<body>

<%
    Integer numUsers = (Integer) request.getAttribute("numUsers");
    Integer numAttempts = (Integer) request.getAttribute("numAttempts");
%>

<div class="page-header">
    <h1>Site Statistics</h1>
    <p>Overview of platform activity</p>
</div>

<div class="card-grid">
    <div class="quiz-card">
        <h3>Total Users</h3>
        <p><strong><%= numUsers != null ? numUsers : "N/A" %></strong></p>
    </div>

    <div class="quiz-card">
        <h3>Total Quiz Attempts</h3>
        <p><strong><%= numAttempts != null ? numAttempts : "N/A" %></strong></p>
    </div>
</div>

<div class="center-button">
    <a href="admin.jsp">Back to Admin Panel</a>
</div>



</body>
</html>