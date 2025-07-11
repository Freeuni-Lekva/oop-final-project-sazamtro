<%@ page import="java.util.List" %>
<%@ page import="bean.User" %>
<%@ page import="Servlets.FriendRequests.RequestAtributeNames" %>

<html>
<head>
    <title>Friends</title>
    <link rel="stylesheet" href="style/friends.css" />
</head>
<body>

<div class="container">

    <!-- Friends List Section -->
    <div class="friends-list">
        <h2>Your Friends</h2>

        <%
            List<User> friends = (List<User>) request.getAttribute(RequestAtributeNames.FRIEND_LIST);
            if (friends != null && !friends.isEmpty()) {
                for (User friend : friends) {
        %>
        <div class="friend-item">
            <a href="profile.jsp?user=<%= friend.getUsername() %>"><%= friend.getUsername() %></a>
            <form method="post" action="/DeleteFriendServlet">
                <input type="hidden" name="friend_id" value="<%= friend.getUserId() %>" />
                <button class="remove-btn" type="submit">Remove Friend</button>
            </form>
        </div>
        <%
            }
        } else {
        %>
        <p>You have no friends yet.</p>
        <% } %>
    </div>

    <!-- Right Side Search Panel -->
    <div class="search-panel">
        <h3>Add Friends</h3>
        <form method="get" action="/SearchUserServlet">
            <input class="search-input" type="text" name="username" placeholder="Search by username" />
            <button class="search-btn" type="submit">Search</button>
        </form>

        <%
            User foundUser = (User) request.getAttribute("foundUser");
            if (foundUser != null) {
        %>
        <%
            if (foundUser != null) {
        %>
        <div class="result-user">
            <p><%= foundUser.getUsername() %></p>

            <% if ((Boolean) request.getAttribute("areFriends")) { %>
            <button disabled>You are already friends</button>
            <% } else if ((Boolean) request.getAttribute("requestSent")) { %>
            <button disabled>Friend Request Sent</button>
            <% } else if ((Boolean) request.getAttribute("requestReceived")) { %>
            <button disabled>They have sent you a request</button>
            <% } else { %>
            <form method="post" action="/SendRequestServlet">
                <input type="hidden" name="<%= RequestAtributeNames.RECEIVER_USERNAME %>" value="<%= foundUser.getUsername() %>" />
                <button type="submit" class="add-friend-btn">Send Friend Request</button>
            </form>
            <% } %>
        </div>
        <% } %>

        <% } %>
    </div>

</div>

</body>
</html>
