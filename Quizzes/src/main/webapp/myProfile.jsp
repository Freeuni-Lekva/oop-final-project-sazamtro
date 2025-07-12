<%@ page import="bean.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("/login.jsp");
        return;
    }
%>
<html>
<head>
    <title>My Profile</title>
    <link rel="stylesheet" href="/style/editQuiz.css" />
    <link rel="stylesheet" href="/style/myProfile.css" />

    <script>
        function validateImageURL() {
            const input = document.querySelector('input[name="profilePicUrl"]');
            const url = input.value.trim();
            const img = new Image();

            img.onload = function () {
                document.querySelector('.url-form').submit();
            };

            img.onerror = function () {
                alert("Invalid image URL. Please check the link and try again.");
            };

            img.src = url;
            return false;
        }
    </script>
</head>
<body>
<div class="container">
    <h1>My Profile</h1>

    <div class="profile-card">
        <img src="<%= user.getProfilePictureUrl() != null ? user.getProfilePictureUrl() : "https://cdn-icons-png.flaticon.com/512/847/847969.png\n" %>" alt="Profile Picture" class="profile-pic">
        <h2>@<%= user.getUsername() %></h2>

        <form class="url-form" action="/UpdateUserProfilePictureServlet" method="post" onsubmit="return validateImageURL();">
            <label>Profile Picture URL:</label>
            <br/>
            <input type="text" name="profilePicUrl" placeholder="Enter image URL..." required />
            <br/>
            <input type="submit" value="Update Picture" />
        </form>

        <% if (user.checkIfAdmin()) { %>
        <form action="/admin.jsp">
            <button type="submit" class="admin-button">Act As Administrator</button>
        </form>
        <% } %>

        <form action="HomePageServlet">
            <button type="submit" class="home-button">🏠 Go to Home</button>
        </form>
    </div>
</div>
</body>
</html>