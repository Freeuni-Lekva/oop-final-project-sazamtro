<%@ page import="java.util.List" %>
<%@ page import="bean.FriendRequest" %>
<%@ page import="bean.Message.Message" %>

<%
  List<FriendRequest> requests = (List<FriendRequest>) request.getAttribute("requests");
  int requestCount = (requests != null) ? requests.size() : 0;

  List<Message> challengeMessages = (List<Message>) request.getAttribute("challenges");
  int challengeCount = (challengeMessages != null) ? challengeMessages.size() : 0;
%>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<link rel="stylesheet" href="style/topbar.css">

<div class="topbar">
  <!-- Middle icons -->
  <div class="topbar-center">
    <a href="/AnnouncementServlet" class="topbar-circle" title="Announcements">
      <i class="fas fa-bullhorn"></i>
    </a>
    <a href="/showAllQuizzes" class="topbar-circle" title="Quizzes">
      <i class="fas fa-clipboard-question"></i>
    </a>
    <a href="ShowFriendsAchievementsServlet" class="topbar-circle" title="Achievements">
      <i class="fas fa-trophy"></i>
    </a>

  </div>

  <!-- Right-side icons -->
  <div class="topbar-right">
    <!-- Friends icon -->
    <div class="topbar-circle" onclick="toggleFriendDropdown()" title="Friends" style="position: relative;">
      <i class="fas fa-user-friends"></i>
      <% if (requestCount > 0) { %>
      <span class="notif-dot"><%= requestCount %></span>
      <% } %>
    </div>

    <!-- Challenges icon -->
    <div class="topbar-circle" onclick="toggleChallengeDropdown()" title="Challenges" style="position: relative;">
      <i class="fas fa-bolt"></i>
      <% if (challengeCount > 0) { %>
      <span class="notif-dot"><%= challengeCount %></span>
      <% } %>
    </div>


    <!-- Profile icon -->
    <div class="topbar-circle" title="Profile">
      <i class="fas fa-user-circle"></i>
    </div>
  </div>
</div>

<!-- Include dropdowns -->
<jsp:include page="/GetFriendRequestsServlet"/>
<jsp:include page="/GetReceivedChallenges" />


<!-- Toggle script -->
<script>
  function toggleFriendDropdown() {
    const challengeDropdown = document.getElementById("challenge-dropdown");
    const dropdown = document.getElementById("friend-dropdown");

    dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
    challengeDropdown.style.display = "none";
  }

  function toggleChallengeDropdown() {
    const friendDropDown = document.getElementById("friend-dropdown");
    const dropdown = document.getElementById("challenge-dropdown");
    dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
    friendDropDown.style.display  = "none";
  }

  window.addEventListener("click", function (e) {
    if (
            !e.target.closest('.topbar-circle') &&
            !e.target.closest('#friend-dropdown') &&
            !e.target.closest('#challenge-dropdown')
    ) {
      const fd = document.getElementById("friend-dropdown");
      const cd = document.getElementById("challenge-dropdown");
      if (fd) fd.style.display = "none";
      if (cd) cd.style.display = "none";
    }
  });
</script>