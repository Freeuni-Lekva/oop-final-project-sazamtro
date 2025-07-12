<%@ page import="java.util.List" %>
<%@ page import="bean.User" %>
<%@ page import="Servlets.FriendRequests.RequestAtributeNames" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Friends</title>
    <link rel="stylesheet" href="style/friends.css" />
    <style>
        .friend-button-wrapper {
            display: block;
            border: none;
            background: none;
            padding: 0;
            margin: 0;
            width: 100%;
            text-align: inherit;
        }
        .friend-button-wrapper:hover .friend-item {
            background: var(--pink-hover-bg);
        }
    </style>
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
                    <form method="get" action="/UserProfileServlet" style="flex-grow: 1;" onsubmit="event.stopPropagation();">
                        <input type="hidden" name="username" value="<%= friend.getUsername() %>" />
                        <button type="submit" class="friend-button-wrapper">
                            <span><%= friend.getUsername() %></span>
                        </button>
                    </form>
                    <form method="post" action="/DeleteFriendServlet" onsubmit="event.stopPropagation();">
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
                    <form method="get" action="/UserProfileServlet">
                        <input type="hidden" name="username" value="<%= foundUser.getUsername() %>" />
                        <button type="submit" class="friend-button-wrapper">
                            <p style="margin: 0; font-weight: bold;"> <%= foundUser.getUsername() %> </p>
                        </button>
                    </form>

                    <% if ((Boolean) request.getAttribute("areFriends")) { %>
                    <button class="disabled-btn" disabled>Already Friends</button>
                    <% } else if ((Boolean) request.getAttribute("requestSent")) { %>
                    <button class="disabled-btn" disabled>Request Exists</button>
                    <% } else if ((Boolean) request.getAttribute("requestReceived")) { %>
                    <button class="disabled-btn" disabled>Request Exists</button>
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
