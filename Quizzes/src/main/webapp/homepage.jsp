<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.Announcement" %>
<%@ page import="bean.Achievement" %>
<%@ page import="bean.Quiz" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Quiz Homepage</title>

    <!-- CSS -->
    <link rel="stylesheet" href="style/homepage.css" />
    <link rel="stylesheet" href="style/sidebar.css" />
    <link rel="stylesheet" href="style/topbar.css" />
    <link rel="stylesheet" href="style/cards.css" />

    <!-- FontAwesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
</head>
<body>

<div class="page-wrapper">

    <%-- Sidebar --%>
    <%@ include file="sidebar.jsp" %>

    <div class="main-section">

        <%-- Topbar --%>
        <jsp:include page="/TopBarServlet" />

        <div class="main-content">
            <div class="content-center">

                <%
                    Boolean showAnnouncements = (Boolean) request.getAttribute("showAnnouncements");
                    Boolean showFriendsAchievements = (Boolean) request.getAttribute("showFriendsAchievements");
                    Boolean showAllQuizzes = (Boolean) request.getAttribute("showAllQuizzes");
                %>

                <% if (showAnnouncements != null && showAnnouncements) { %>
                <jsp:include page="announcements.jsp" />
                <% } else if (showFriendsAchievements != null && showFriendsAchievements) { %>
                <jsp:include page="achievements-friends.jsp" />
                <% } else if (showAllQuizzes != null && showAllQuizzes) { %>
                <jsp:include page="quizzes.jsp" />
                <% } else { %>
                <!-- Blank central content -->
                <div style="height: 300px; display: flex; justify-content: center; align-items: center; color: #666;">
                    <p>Welcome! Use the top menu to navigate content.</p>
                </div>
                <% } %>

            </div>

            <%-- Friends sidebar on right --%>
            <div class="friends-sidebar">
                <jsp:include page="GetFriendListServlet">
                    <jsp:param name="mode" value="sidebar" />
                </jsp:include>
            </div>

        </div>

    </div>

</div>

</body>
</html>
