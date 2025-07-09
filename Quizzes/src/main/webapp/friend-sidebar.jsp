<%@ page import="java.util.List" %>
<%@ page import="bean.User" %>
<%@ page import="Servlets.FriendRequests.RequestAtributeNames" %>

<link rel="stylesheet" href="style/friend-bar.css">

<%
  List<User> friends = (List<User>) request.getAttribute(RequestAtributeNames.FRIEND_LIST);
%>

<div class="friends-bar">
  <h3>Friends</h3>
  <ul>
    <% if (friends != null && !friends.isEmpty()) {
      for (User friend : friends) { %>
    <li><%= friend.getUsername() %></li>
    <% }
    } else { %>
    <li>No friends found.</li>
    <% } %>
  </ul>
</div>
