<%@ page import="java.util.List" %>
<%@ page import="bean.Announcement" %>
<%@ page import="java.text.SimpleDateFormat" %>
<link rel="stylesheet" href="style/announcements.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<%
  List<Announcement> announcements = (List<Announcement>) request.getAttribute("announcements");
  SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

%>

<div class="announcement-section">
  <h2><i class="fas fa-bullhorn"></i> Announcements</h2>

  <%
    if (announcements != null && !announcements.isEmpty()) {
      for (Announcement a : announcements) {
  %>
  <div class="announcement-card">
    <div class="announcement-header">
<%--      <h3><%= a.getTitle() %></h3>--%>
      <span class="announcement-date"><%= formatter.format(a.getDoneAt()) %></span>
    </div>
    <p><%= a.getText() %></p>
  </div>
  <%
    }
  } else {
  %>
  <p>No announcements available.</p>
  <%
    }
  %>
</div>
