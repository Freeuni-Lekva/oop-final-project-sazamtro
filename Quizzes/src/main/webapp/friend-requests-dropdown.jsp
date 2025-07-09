<%@ page import="java.util.List" %>
<%@ page import="bean.FriendRequest" %>
<%@ page import="bean.User" %>
<%@ page import="Servlets.FriendRequests.RequestAtributeNames" %>

<%
  List<FriendRequest> requests = (List<FriendRequest>) request.getAttribute("friendRequests");
  List<User> senders = (List<User>)request.getAttribute("requestSenders");
%>

<link rel="stylesheet" href="style/topbar.css">

<div id="friend-dropdown" class="friend-dropdown">
  <%
    if (requests != null && !requests.isEmpty()) {
      for (int i = 0; i < requests.size(); i++) {
        FriendRequest fr = requests.get(i);
  %>
  <div class="friend-request">
    <span class="request-text"><%= senders.get(i).getUsername() + " has sent you a friend request" %></span>
    <div class="friend-buttons">
      <form action="RespondToRequestServlet" method="post">
        <input type="hidden" name= "<%=RequestAtributeNames.REQUEST_ID  %>" value="<%= fr.getRequestId() %>">
        <input type="hidden" name= "<%= RequestAtributeNames.RESPONSE%>" value="accept">
        <button type="submit" class="accept-btn">Accept</button>
      </form>
      <form action="RespondToRequestServlet" method="post">
        <input type="hidden" name="<%=RequestAtributeNames.REQUEST_ID  %>" value="<%= fr.getRequestId() %>">
        <input type="hidden" name="<%= RequestAtributeNames.RESPONSE%>" value="reject">
        <button type="submit" class="reject-btn">Reject</button>
      </form>
    </div>
  </div>
  <%
    }
  } else {
  %>
  <div class="no-requests">No requests</div>
  <% } %>
</div>