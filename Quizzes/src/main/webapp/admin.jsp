<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" type="text/css" href="style/admin-page.css">
</head>
<body>

<div class="container">

    <header class="header">
        <h1>Welcome to <span class="brand">ADMIN PANEL</span></h1>
        <p class="subheading">Manage your platform with ease and insight.</p>
    </header>

    <div class="card-slider-wrapper">
        <div class="card-container">
            <a href="/UserAdminServlet" class="card">
                <div class="card-title">Manage Users</div>
                <div class="card-desc">Remove users, promote admins, and monitor accounts.</div>
            </a>

            <a href="/showAllQuizzes?mode=admin" class="card">
                <div class="card-title">Manage Quizzes</div>
                <div class="card-desc">Remove quizzes and reset quiz history data.</div>
            </a>

            <a href="/AnnouncementServlet?mode=admin" class="card">
                <div class="card-title">Manage Announcements</div>
                <div class="card-desc">Create or delete announcements that users see on login.</div>
            </a>

            <a href="AdministratorServlet?action=viewStatistics" class="card">
                <div class="card-title">View Statistics</div>
                <div class="card-desc">Track user count and quiz activity across the platform.</div>
            </a>

        </div>
    </div>

    <footer class="auth">
        <div class="auth-box">
            <a href="HomePageServlet" class="auth-card">
                <p class="auth-title">Return to Homepage</p>
                <span class="btn-auth login">Go Home</span>
            </a>
        </div>
    </footer>

</div>

</body>
</html>
