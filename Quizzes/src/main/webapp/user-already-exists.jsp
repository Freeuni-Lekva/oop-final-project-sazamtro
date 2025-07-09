<%--
  Created by IntelliJ IDEA.
  User: mac
  Date: 09.07.25
  Time: 17:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>User Already Exists</title>
    <link rel="stylesheet" href="style/user-already-exists.css">
</head>

<body>

<div class="login-signup-container">
    <h1>User Already Exists</h1>

    <div class="section">
        <p class="message">If this is your username, please log in</p>
        <form action="login.jsp" method="get">
            <button type="submit" class="btn-login">Log In</button>
        </form>
    </div>

    <hr>

    <div class="section">
        <p class="message">Otherwise, try signing up with a different username</p>
        <form action="signup.jsp" method="get">
            <button type="submit" class="btn-signup">Sign Up</button>
        </form>
    </div>
</div>
</body>

</html>
