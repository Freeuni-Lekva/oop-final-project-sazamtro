<%@ page import="java.util.UUID" %>
<%
    String chatboxId = "chat-" + UUID.randomUUID().toString().replace("-", "");
    String friendUsername = request.getParameter("friend");
%>

<div class="chat-box" data-username="<%= friendUsername %>" id="chat-box-<%= friendUsername %>">
    <div class="chat-header">
        <span><%= friendUsername %></span>
        <button class="close-chat">&times;</button>
    </div>
    <div class="chat-messages" id="chat-messages-<%= friendUsername %>"></div>
    <div class="chat-input">
        <input type="text" placeholder="Type a message..." />
        <button class="send-btn">Send</button>
    </div>
</div>
