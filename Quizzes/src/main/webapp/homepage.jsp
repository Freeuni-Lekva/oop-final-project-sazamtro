<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.Announcement" %>
<%@ page import="Servlets.FriendRequests.RequestAtributeNames" %>

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
        <jsp:include page="/TopBarServlet"/>

        <div class="main">
            <!-- Left side: Announcements -->
            <div class="details">
                <jsp:include page="/AnnouncementServlet"/>
            </div>

            <!-- Right side: Friends sidebar -->
            <jsp:include page="/GetFriendListServlet"/>
        </div>

    </div>
</div>



</body>
</html>
