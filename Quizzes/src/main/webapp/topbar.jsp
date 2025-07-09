<%@ page import="java.util.List" %>
<%@ page import="bean.FriendRequest" %>
<%
  List<FriendRequest> requests = (List<FriendRequest>) request.getAttribute("friendRequests");
  int requestCount = (requests != null) ? requests.size() : 0;
%>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<link rel="stylesheet" href="style/topbar.css">

<div class="topbar">
  <!-- Middle icons -->
  <div class="topbar-center">
    <a href="/AnnouncementServlet" class="topbar-circle" title="Announcements">
      <i class="fas fa-bullhorn"></i>
    </a>
    <a href="quizzes.jsp" class="topbar-circle" title="Quizzes">
      <i class="fas fa-clipboard-question"></i>
    </a>
    <a href="achievements.jsp" class="topbar-circle" title="Achievements">
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

    <!-- Challenges -->
    <div class="topbar-circle" title="Challenges">
      <i class="fas fa-bullseye"></i>
    </div>

    <!-- Messages -->
    <div class="topbar-circle" title="Messages">
      <i class="fas fa-comments"></i>
    </div>

    <!-- Profile -->
    <div class="topbar-circle" title="Profile">
      <i class="fas fa-user-circle"></i>
    </div>
  </div>
</div>

<!-- Include dropdown markup from separate file -->
<jsp:include page="friend-requests-dropdown.jsp"/>

<!-- Toggle script -->
<script>
  function toggleFriendDropdown() {
    const dropdown = document.getElementById("friend-dropdown");
    dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
  }

  window.addEventListener("click", function (e) {
    if (!e.target.closest('.topbar-circle') && !e.target.closest('#friend-dropdown')) {
      const dropdown = document.getElementById("friend-dropdown");
      if (dropdown) dropdown.style.display = "none";
    }
  });
</script>
