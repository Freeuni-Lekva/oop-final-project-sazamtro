<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quiz Homepage</title>
    <link rel="stylesheet" href="style/homepage.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="logo">
            <img src="https://cdn-icons-png.flaticon.com/128/1791/1791301.png" alt="Logo" class="logo-img">
        </div>
        <ul>
            <li><a href="homepage.jsp"><i class="fas fa-house"></i> Home</a></li>
            <li><a href="friends.jsp"><i class="fas fa-user-friends"></i> Friends</a></li>
            <li><a href="history.jsp"><i class="fas fa-clock-rotate-left"></i> History</a></li>
            <li><a href="my-quizzes.jsp"><i class="fas fa-list-check"></i> My Quizzes</a></li>
            <li><a href="quizzes.jsp"><i class="fas fa-clipboard-question"></i> Quizzes</a></li>
            <li><a href="achievements.jsp"><i class="fas fa-trophy"></i> Achievements</a></li>
            <li><a href="AnnouncementServlet"><i class="fas fa-bullhorn"></i> Announcements</a></li>
        </ul>

        <div class="sidebar-actions">
            <a href="create-quiz.jsp" class="sidebar-button">create quiz</a>
            <a href="logout.jsp" class="sidebar-button logout">log out</a>
        </div>
    </div>

    <!-- Top Navigation -->
    <div class="topnav">
        <div class="nav-item">quizzes</div>
        <div class="nav-item">achievements</div>
        <div class="nav-item">announcements</div>
        <div class="nav-item">friends</div>
        <div class="nav-item">challenges</div>
        <div class="nav-item">messages</div>
        <div class="profile-pic"></div>
    </div>

    <!-- Main Content Area -->
    <div class="main">
        <%@ include file="announcements.jsp" %>
        <!-- Right Sidebar -->
<%--        <div class="right-sidebar">--%>
<%--            <%@ include file="friends-sidebar.jsp" %>--%>
<%--        </div>--%>
    </div>


<!-- JavaScript logic directly in JSP -->
<script>
    function handleAction(action) {
        switch(action) {
            case 'start':
                alert("Starting the quiz...");
                break;
            case 'practice':
                alert("Opening practice mode...");
                break;
            case 'share':
                alert("Sharing the quiz...");
                break;
            default:
                console.log("Unknown action: " + action);
        }
    }
</script>
</body>
</html>
