<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>

  <title>Invalid Input</title>
  <link rel="stylesheet" href="style/feedback.css">
</head>

<body>

<div class="login-signup-container">
  <h1>Invalid Input</h1>
  <p class="message">
    It looks like you didn’t fill in all the fields. Please check and try again!
  </p>

  <div class="divider"></div>

  <div class="actions">
    <form action="login.jsp" method="get" style="margin:0;">
      <button type="submit" class="login-btn">Back to Log In</button>
    </form>
    <form action="signup.jsp" method="get" style="margin:0;">
      <button type="submit" class="signup-btn">Back to Sign Up</button>
    </form>
  </div>
</div>
</body>

</html>
