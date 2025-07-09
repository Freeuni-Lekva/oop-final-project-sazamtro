<%@ page import="java.util.List" %>
<%@ page import="bean.Message.Message" %>
<%@ page import="bean.User" %>
<%@ page import="bean.Message.ChallengeMessage" %>

<%
  List<Message> challenges = (List<Message>) request.getAttribute("challengeMessages");
  List<User> senders = (List<User>) request.getAttribute("challengeSenders");
%>

<link rel="stylesheet" href="style/challenge-dropdown.css">

<div id="challenge-dropdown" class="dropdown-content">
  <% if (challenges != null && !challenges.isEmpty()) { %>
  <% for (int i = 0; i < challenges.size(); i++) {
        ChallengeMessage msg = (ChallengeMessage) challenges.get(i);
        User sender = senders.get(i);
  %>
  <div class="challenge-message">
    <a href="quiz.jsp?quiz_id=<%= msg.getQuiz_id() %>">
      <%= sender.getUsername() %> has challenged you to a quiz!: <%= "\n" + msg.getContent()%>
    </a>
  </div>
  <% } %>
  <% } else { %>
  <div class="no-challenges">No challenge messages</div>
  <% } %>
</div>
