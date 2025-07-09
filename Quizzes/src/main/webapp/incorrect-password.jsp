<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<html lang="en">
<head>
  <title>Incorrect Password</title>
  <link rel="stylesheet" href="style/incorrect-password.css">
</head>

<body>

<div class="login-signup-container">
  <h1>Incorrect Password</h1>
  <p class="message">
    The <span class="highlight">password</span> you entered is incorrect
  </p>

  <div class="divider"></div>

  <div class="actions">
    <form action="login.jsp" method="get" style="margin:0;">
      <button type="submit" class="login-btn">Try Logging In Again</button>
    </form>
  </div>
</div>
</body>

</html>
