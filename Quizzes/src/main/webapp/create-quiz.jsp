<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Quiz</title>
    <link rel="stylesheet" type="text/css" href="/style/sidebar.css" />
    <link rel="stylesheet" type="text/css" href="/style/create_quiz_style.css" />
</head>
<body>
<div class="container">
    <%-- Sidebar stays aligned left --%>
    <%@ include file="sidebar.jsp" %>

    <%-- Main form area --%>
    <div class="main-content">
        <h2>Create a New Quiz</h2>
        <form action="/quizzes/new" method="post">
            <label for="title">Quiz Title</label>
            <input type="text" name="title" id="title" required />

            <label for="description">Description</label>
            <textarea name="description" id="description" rows="4"></textarea>

            <div class="checkbox-group">
                <label><input type="checkbox" name="random" /> Randomize question order</label><br>
                <label><input type="checkbox" name="multipage" /> Show one question per page</label><br>
                <label><input type="checkbox" name="immediate_correction" /> Immediate correction after submitting</label>
            </div>

            <button type="submit">Create Quiz</button>
        </form>
    </div>
</div>
</body>
</html>
