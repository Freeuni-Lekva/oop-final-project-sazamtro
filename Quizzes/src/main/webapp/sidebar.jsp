
<div class="sidebar">
    <div class="logo">
        <a href = "HomePageServlet"><img src="https://cdn-icons-png.flaticon.com/512/4722/4722178.png" alt="Logo"></a>
    </div>

    <ul class="nav-links">

        <li><a href="friends.jsp"><i class="fas fa-user-friends"></i> <span>Friends</span></a></li>
        <li><a href="quizAttempts-userOptions.jsp"><i class="fas fa-clock"></i> <span>History</span></a></li>
        <li><a href="/myQuizzes"><i class="fas fa-list"></i> <span>My Quizzes</span></a></li>
        <li><a href="ShowUserAchievementsServlet"><i class="fas fa-trophy"></i> <span>Achievements</span></a></li>


    <div class="sidebar-actions">
        <a href="create-quiz.jsp" class="sidebar-button create">
            <i class="fas fa-plus-circle"></i> <span>Create Quiz</span>
        </a>
        <form action="/LogoutServlet" method="post" style="margin: 0;">
            <button type="submit" class="sidebar-button logout">
                <i class="fas fa-sign-out-alt"></i> <span>Log Out</span>
            </button>
        </form>
    </div>
</div>

