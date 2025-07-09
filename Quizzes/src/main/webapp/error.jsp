<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #fef3f3;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .error-box {
            width: 200px;
            background-color: #fff;
            border: 2px solid #ff4d4f;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            text-align: center;
        }

        .error-box h1 {
            color: #ff4d4f;
            margin-bottom: 10px;
        }

        .reason {
            font-size: 18px;
            color: #333;
        }
    </style>
</head>
<body>
<div class="error-box">
    <h1>Error</h1>
    <div class="reason">
        <%
            String reason = request.getParameter("reason");
        %>
        <p>Error: <%= reason %></p>
    </div>
</div>
</body>
</html>
