<%@ page import="bean.User" %>
<%@ page import="java.util.List" %>
<%@ page import="Servlets.FriendRequests.RequestAtributeNames" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User targetUser = (User) request.getAttribute("targetUser");
    List<User> mutualFriends = (List<User>) request.getAttribute("mutualFriends");
    boolean areFriends = (Boolean) request.getAttribute("areFriends");
    boolean requestExists = (Boolean) request.getAttribute("friendRequestStatus");
    String mode = (String) request.getAttribute("mode");
    boolean isAdminView = "admin".equals(mode);
%>
<html>
<head>
    <title><%= targetUser.getUsername() %>'s Profile</title>
    <link rel="stylesheet" href="/style/editQuiz.css" />
    <link rel="stylesheet" href="/style/userProfile.css">
</head>
<body>
<div class="container">
    <div class="profile-card">
        <img class="profile-pic" src="<%= targetUser.getProfilePictureUrl() != null ? targetUser.getProfilePictureUrl() : "https://cdn-icons-png.flaticon.com/512/847/847969.png\n" %>" alt="Profile Picture" />
        <h2>@<%= targetUser.getUsername() %></h2>

        <% if (!areFriends && !requestExists) { %>
        <form action="/SendRequestServlet" method="post" >
            <input type="hidden" name=<%=RequestAtributeNames.RECEIVER_USERNAME%> value=<%=targetUser.getUsername()%> />
            <input type="hidden" name="redirect" value=<%="/UserProfileServlet?username=" + targetUser.getUsername()%> />
            <button type="submit" class="btn">Send Friend Request</button>
        </form>

        <% } else { %>
        <button class="btn" disabled>
            <%= areFriends ? "You Are Friends" : "Friend Request Exists" %>
        </button>
        <% } %>

        <h3>Mutual Friends</h3>
        <ul class="mutual-list">
            <% for (User friend : mutualFriends) { %>
            <li>@<%= friend.getUsername() %></li>
            <% } %>
        </ul>

        <% if (isAdminView) { %>
        <form action="/promote-user" method="post">
            <input type="hidden" name="username" value="<%= targetUser.getUsername() %>" />
            <button class="btn">Promote to Admin</button>
        </form>

        <form action="/remove-user" method="post" onsubmit="return confirm('Are you sure you want to remove this user?');">
            <input type="hidden" name="username" value="<%= targetUser.getUsername() %>" />
            <button class="btn" style="background-color: var(--pink);">Remove User</button>
        </form>

        <a class="go-back-btn" href="/admin.jsp">&larr; Back to Admin Dashboard</a>
        <% } else { %>
        <a class="go-back-btn" href="/homepage.jsp">&larr; Back to Homepage</a>
        <% } %>
    </div>
</div>
</body>
</html>
