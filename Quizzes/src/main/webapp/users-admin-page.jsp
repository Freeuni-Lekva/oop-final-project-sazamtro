<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.User" %>

<!DOCTYPE html>
<html>
<head>
    <title>Manage Users</title>
    <link rel="stylesheet" href="style/admin-page.css">
    <link rel="stylesheet" href="style/quizzes-admin.css">
    <link rel="stylesheet" href="style/users-admin-page.css">
</head>
<body>
<div class="container">

    <header class="header">
        <h1>Manage <span class="brand">Users</span></h1>
        <p class="subheading">Promote admins and remove accounts with ease.</p>
    </header>

    <div class="search-container">
        <form action="UserAdminServlet" method="get">
            <input type="text" name="search" placeholder="Search by username...">
            <button type="submit">Search</button>
        </form>
    </div>

    <div class="user-grid">
        <%
            List<User> users = (List<User>) request.getAttribute("userList");
            if (users != null && !users.isEmpty()) {
                for (User u : users) {
        %>
        <div class="user-card">
            <a class="username-link" href="UserProfileServlet?username=<%= u.getUsername() %>&mode=admin">
                <%= u.getUsername() %>
            </a>

            <p>Role: <%= u.checkIfAdmin() ? "Admin" : "User" %></p>

            <div class="user-actions">
                <form action="/PromoteUserServlet" method="post">
                    <input type="hidden" name="userId" value="<%= u.getUserId() %>">
                    <button class="btn-promote" type="submit">Promote</button>
                </form>
                <form action="/RemoveUserServlet" method="post" onsubmit="return confirm('Are you sure you want to remove this user?');">
                    <input type="hidden" name="userId" value="<%= u.getUserId() %>">
                    <button class="btn-remove" type="submit">Remove</button>
                </form>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <p style="text-align: center;">No users found.</p>
        <% } %>
    </div>

    <div class="center-button">
        <a href="/admin.jsp">← Return to Admin Page</a>
    </div>

</div>
</body>
</html>
