<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <title>Login - Quizzes</title>
  <link rel="stylesheet" href="style/login-signup.css">
</head>

<body>

<div class="login-signup-container">
  <h1>Welcome Back</h1>
  <p>Please log in to your account</p>

  <form action="LoginServlet" method="post" novalidate>
    <input type="text" name="username" placeholder="Username" required autofocus />
    <input type="password" name="password" placeholder="Password" required />
    <button type="submit" class="login-btn" disabled>Log In</button>
  </form>

  <div class="switch-link">
    <p>New here?</p>
    <p><a href="signup.jsp">Create An Account</a></p>
  </div>
</div>

<script>
  const loginForm = document.querySelector('form');
  const loginInputs = loginForm.querySelectorAll('input[required]');
  const loginButton = loginForm.querySelector('button');

  function updateLoginButton() {
    const allFilled = Array.from(loginInputs).every(i => i.value.trim() !== '');
    loginButton.disabled = !allFilled;
    loginButton.style.opacity = allFilled ? '1' : '0.6';
    loginButton.style.cursor = allFilled ? 'pointer' : 'not-allowed';
    loginButton.style.background = allFilled
            ? 'linear-gradient(to right, #4f76f7, #7a55ef)'
            : 'linear-gradient(to right, var(--blue-light), var(--blue-lighter))';
  }

  loginInputs.forEach(input => input.addEventListener('input', updateLoginButton));
  window.addEventListener('DOMContentLoaded', updateLoginButton);
</script>

</body>
</html>