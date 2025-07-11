<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Quizzes</title>
  <link rel="stylesheet" type="text/css" href="style/index-page.css">

</head>

<body>


<div class="container">

  <header class="header">
    <h1>Welcome to <span class="brand">QUIZZES</span></h1>
    <p class="subheading">Master your mind. Challenge your friends. Rule the leaderboard.</p>
  </header>

  <div class="card-slider-wrapper">
    <div class="card-container">
      <a href="login.jsp" class="card">
        <div class="card-title">Be the Quiz Champion</div>
        <div class="card-desc">Top the leaderboard with your knowledge and speed!</div>
      </a>

      <a href="login.jsp" class="card">
        <div class="card-title">Challenge Your Friends</div>
        <div class="card-desc">Send quiz duels and see who rules the rankings!</div>
      </a>

      <a href="login.jsp" class="card">
        <div class="card-title">Create & Share Quizzes</div>
        <div class="card-desc">Design your own quizzes and publish them for others.</div>
      </a>

      <a href="login.jsp" class="card">
        <div class="card-title">Track Your Progress</div>
        <div class="card-desc">See your stats and earn achievements over time.</div>
      </a>

      <a href="login.jsp" class="card">
        <div class="card-title">Receive Quiz Challenges</div>
        <div class="card-desc">Get notified when friends challenge you or comment!</div>
      </a>
    </div>
  </div>

  <footer class="auth">
    <div class="auth-box">
      <a href="login.jsp" class="auth-card">
        <p class="auth-title">Already a member?</p>
        <span class="btn-auth login">Log In</span>
      </a>

      <a href="signup.jsp" class="auth-card">
        <p class="auth-title">New here?</p>
        <span class="btn-auth signup">Sign Up</span>
      </a>
    </div>
  </footer>


</div>

</body>
</html>
