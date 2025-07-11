<%@ page import="java.util.List" %>
<%@ page import="bean.User" %>
<%@ page import="Servlets.FriendRequests.RequestAtributeNames" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Friends</title>
    <link rel="stylesheet" href="style/friends.css" />
</head>
<body>

<div class="page-wrapper">

    <div class="main-section">

        <div class="main-content">

            <div class="content-center">
                <h2>Your Friends</h2>

                <%
                    List<User> friends = (List<User>) request.getAttribute(RequestAtributeNames.FRIEND_LIST);
                    if (friends != null && !friends.isEmpty()) {
                        for (User friend : friends) {
                %>
                <div class="friend-item">
                    <span><%= friend.getUsername() %></span>
                    <form method="post" action="/DeleteFriendServlet">
                        <input type="hidden" name="friend_id" value="<%= friend.getUserId() %>" />
                        <button class="remove-btn" type="submit">Remove</button>
                    </form>
                </div>
                <% } } else { %>
                <p class="no-friends">You have no friends yet.</p>
                <% } %>
            </div>


            <div class="friends-sidebar">
                <h2>Add Friends</h2>
                <form method="get" action="/SearchUserServlet">
                    <input class="search-input" type="text" name="username" placeholder="Search by username" />
                    <button class="search-btn" type="submit">Search</button>
                </form>

                <%
                    User foundUser = (User) request.getAttribute("foundUser");
                    if (foundUser != null) {
                %>
                <div class="result-user">
                    <p><%= foundUser.getUsername() %></p>

                    <% if ((Boolean) request.getAttribute("areFriends")) { %>
                    <button class="disabled-btn" disabled>Already Friends</button>
                    <% } else if ((Boolean) request.getAttribute("requestSent")) { %>
                    <button class="disabled-btn" disabled>Request Sent</button>
                    <% } else if ((Boolean) request.getAttribute("requestReceived")) { %>
                    <button class="disabled-btn" disabled>Request Received</button>
                    <% } else { %>
                    <form method="post" action="/SendRequestServlet">
                        <input type="hidden" name="<%= RequestAtributeNames.RECEIVER_USERNAME %>" value="<%= foundUser.getUsername() %>" />
                        <button type="submit" class="add-friend-btn">Send Friend Request</button>
                    </form>
                    <% } %>
                </div>
                <% } %>
            </div>

        </div>

        <div class="bottom-bar">
            <a href="HomePageServlet" class="home-btn">HOME</a>
        </div>

    </div>

</div>

</body>
</html>
