<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <title>Sign Up - Quizzes</title>
  <link rel="stylesheet" href="style/login-signup.css">
</head>

<body>

<div class="login-signup-container">
  <h1>Create An Account</h1>
  <p>Join us and start quizzing!</p>

  <form action="SignupServlet" method="post" novalidate>
    <input type="text" name="username" placeholder="Username" required autofocus />
    <input type="password" name="password" placeholder="Password" required />
    <input type="password" name="passwordConfirm" placeholder="Confirm Password" required />
    <button type="submit" class="signup-btn" disabled>Sign Up</button>
  </form>

  <div class="switch-link">
    <p>Already have an account?</p>
    <p><a href="login.jsp">Log In Here</a></p>
  </div>
</div>

<script>
  const signupForm = document.querySelector('form');
  const signupInputs = signupForm.querySelectorAll('input[required]');
  const signupButton = signupForm.querySelector('button');

  function updateSignupButton() {
    const allFilled = Array.from(signupInputs).every(i => i.value.trim() !== '');
    signupButton.disabled = !allFilled;
    signupButton.style.opacity = allFilled ? '1' : '0.6';
    signupButton.style.cursor = allFilled ? 'pointer' : 'not-allowed';
    signupButton.style.background = allFilled
            ? 'linear-gradient(to right, #f88cbb, #f9a8d4)'
            : 'linear-gradient(to right, var(--pink-hover-bg), var(--pink-border))';
  }

  signupInputs.forEach(input => input.addEventListener('input', updateSignupButton));
  window.addEventListener('DOMContentLoaded', updateSignupButton);
</script>

</body>
</html>