<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Passwords Do Not Match</title>
  <link rel="stylesheet" href="style/incorrect-password.css">
</head>
<body>

<div class="login-signup-container">
  <h1>Passwords Don’t Match</h1>

  <p class="message">
    The <span class="highlight">confirmation password</span> doesn’t match the one above.
    Please recheck and try again.
  </p>

  <div class="divider"></div>

  <div class="actions">
    <form action="signup.jsp" method="get" style="margin:0;">
      <button type="submit" class="login-btn">Go Back to Sign Up</button>
    </form>
  </div>
</div>

</body>
</html>
