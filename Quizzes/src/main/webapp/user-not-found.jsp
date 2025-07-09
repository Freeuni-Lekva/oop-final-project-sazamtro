<%--
  Created by IntelliJ IDEA.
  User: mac
  Date: 09.07.25
  Time: 17:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>User Not Found</title>
    <link rel="stylesheet" href="style/user-not-found.css">
</head>
<body>
<div class="login-signup-container">
    <h1>User Not Found</h1>
    <p class="message">
        No account exists with the <span class="highlight">username</span> you entered
    </p>

    <div class="divider"></div>

    <div class="actions">
        <form action="login.jsp" method="get" style="margin:0;">
            <button type="submit" class="login-btn">Try Logging In Again</button>
        </form>
        <form action="signup.jsp" method="get" style="margin:0;">
            <button type="submit" class="signup-btn">Create An Account</button>
        </form>
    </div>
</div>
</body>
</html>

