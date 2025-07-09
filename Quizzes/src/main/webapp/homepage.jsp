<%
    if (request.getAttribute("announcements") == null) {
        response.sendRedirect("AnnouncementServlet");
        return;
    }
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quiz Homepage</title>
    <link rel="stylesheet" href="style/homepage.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

</head>
<body>
<div class="container">
    <%@ include file="sidebar.jsp" %>

    <div class="page-content">
        <%@ include file="topbar.jsp" %>

        <div class="main">
            <%@ include file="announcements.jsp" %>
        </div>
    </div>
</div>

<!-- JavaScript logic directly in JSP -->
<script>
    function handleAction(action) {
        switch(action) {
            case 'start':
                alert("Starting the quiz...");
                break;
            case 'practice':
                alert("Opening practice mode...");
                break;
            case 'share':
                alert("Sharing the quiz...");
                break;
            default:
                console.log("Unknown action: " + action);
        }
    }
</script>
</body>
</html>
