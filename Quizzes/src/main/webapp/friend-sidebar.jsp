<%@ page import="java.util.List" %>
<%@ page import="bean.User" %>
<%@ page import="Servlets.FriendRequests.RequestAtributeNames" %>
<%@ page import="Servlets.Message.MessageAtributeNames" %>
<%@ page import="java.util.Set" %>

<!-- Styles -->
<link rel="stylesheet" href="style/friend-bar.css">
<%--<link rel="stylesheet" href="style/homepage.css">--%>
<link rel="stylesheet" href="style/chatbox.css">

<%
  List<User> friends = (List<User>) request.getAttribute(RequestAtributeNames.FRIEND_LIST);
  Set<Integer> unreadUsernames = (Set<Integer>) request.getAttribute(MessageAtributeNames.UNREAD_SENDER_IDS);
%>

<!-- Friends Sidebar -->
<div class="friends-sidebar">
  <h3>MY FRIENDS</h3>
  <ul>
    <% if (friends != null && !friends.isEmpty()) {
      for (User friend : friends) {boolean hasUnread = unreadUsernames != null && unreadUsernames.contains(friend.getUserId()); %>
    <li class="friend-name" onclick="openChatBox('<%= friend.getUsername() %>')">
      <img class="friend-avatar" src="<%= friend.getProfilePictureUrl() != null ? friend.getProfilePictureUrl() : "https://cdn-icons-png.flaticon.com/512/847/847969.png\n" %>" alt="avatar">
      <span class="friend-username"><%= friend.getUsername() %></span>
      <% if (hasUnread) { %><span class="unread-dot"></span><% } %>
    </li>
    <%}
    } else { %>
    <li>No friends found.</li>
    <% } %>
  </ul>

</div>
<div id="chat-container"></div>

<!-- Chatbox Frame -->

<%--
  <div id="chat-container"></div>
--%>


<!-- Chatbox JavaScript -->
<script>
  function openChatBox(username) {
    if (document.getElementById("chat-box-" + username)) return;

    fetch("/chatbox.jsp?friend=" + encodeURIComponent(username))
            .then(res => res.text())
            .then(html => {
              const wrapper = document.createElement("div");
              wrapper.innerHTML = html;
              const chatBox = wrapper.firstElementChild;
              chatBox.setAttribute("data-username", username);
              chatBox.id = "chat-box-" + username;
              document.getElementById("chat-container").appendChild(chatBox);
              attachChatboxEvents(chatBox);
            });
  }

  function attachChatboxEvents(box) {
    const username = box.getAttribute("data-username");
    const messageArea = box.querySelector('.chat-messages');
    const inputField = box.querySelector('.chat-input input');
    const sendBtn = box.querySelector('.send-btn');
    const closeBtn = box.querySelector('.close-chat');

    fetch(`/GetMessagesServlet?<%= MessageAtributeNames.RECEIVER_USERNAME %>=${encodeURIComponent(username)}`)
            .then(res => res.json())
            .then(messages => {
              messages.forEach(msg => {
                const msgDiv = document.createElement('div');
                msgDiv.className = msg.sender === 'YOU' ? 'message mine' : 'message theirs';
                msgDiv.textContent = msg.content;
                messageArea.appendChild(msgDiv);
              });
              messageArea.scrollTop = messageArea.scrollHeight;
            });

    sendBtn.addEventListener('click', () => {
      const message = inputField.value.trim();
      if (!message) return;


      const msgDiv = document.createElement('div');
      msgDiv.className = 'message mine';
      msgDiv.textContent = message;
      messageArea.appendChild(msgDiv);
      messageArea.scrollTop = messageArea.scrollHeight;

      fetch('/SendMessageServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `receiver_username=${encodeURIComponent(username)}&content=${encodeURIComponent(message)}`
      });

      inputField.value = '';
    });

    inputField.addEventListener('keydown', (e) => {
      if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault(); // prevents new line
        sendBtn.click();    // simulate clicking the send button
      }
    });

    closeBtn.addEventListener('click', () => {
      box.remove();
    });
  }
</script>