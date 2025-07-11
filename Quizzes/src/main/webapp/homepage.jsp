<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.Announcement" %>
<%@ page import="bean.Achievement" %>

<%@ page import="Servlets.FriendRequests.RequestAtributeNames" %>

<!DOCTYPE html>
<html>
<head>
    <title>Quiz Homepage</title>
    <link rel="stylesheet" href="style/homepage.css">
    <link rel="stylesheet" href="style/sidebar.css">
    <link rel="stylesheet" href="style/topbar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<div class="page-wrapper">
    <%@ include file="sidebar.jsp" %>

    <div class="main-section">
        <jsp:include page="/TopBarServlet"/>

        <div class="main-content">
            <div class="content-center">
                <%
                    Boolean showFriendsAchievements = (Boolean) request.getAttribute("showFriendsAchievements");
                    Boolean showAllQuizzes = (Boolean) request.getAttribute("showAllQuizzes");
                    if (showFriendsAchievements != null && showFriendsAchievements) {
                        List<Achievement> achievements = (List<Achievement>) request.getAttribute("friendsAchievements");
                %>
                <h2>Friends' Achievements</h2>
                <div class="achievement-cards">
                    <% if (achievements != null && !achievements.isEmpty()) {
                        for (Achievement ach : achievements) { %>
                    <div class="achievement-card">
                        <h3><%= ach.getUsername() %></h3>
                        <h4><%= ach.getAchievement_name() %></h4>
                        <p><%= ach.getAchievement_descr() %></p>
                    </div>
                    <%     }
                    } else { %>
                    <p>No achievements found.</p>
                    <% } %>
                </div>
                <% }else if(showAllQuizzes != null && showAllQuizzes){
                %> <jsp:include page="/quizzes.jsp"/>
                <%
                } else {
                %>
                <jsp:include page="/announcements.jsp"/>
                <%
                    }
                %>
            </div>


            <%--<!-- Right side: Friends sidebar -->
            <jsp:include page="GetFriendListServlet">
                <jsp:param name="mode" value="sidebar" />
            </jsp:include>--%>
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