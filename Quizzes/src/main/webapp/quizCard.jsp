<%@ page import="bean.Quiz" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.User" %> <!-- Assuming User bean for friends -->
<%
    Quiz quiz = (Quiz) request.getAttribute("quiz");
    List<User> friends = (List<User>) request.getAttribute("friends");
    // Pass this from your servlet/controller — friends list of logged-in user
%>

<div class="quiz-card">
    <h3 class="quiz-title"><%= quiz.getQuizTitle() %></h3>

    <form action="/quizzes/start" method="get" class="start-form">
        <input type="hidden" name="id" value="<%= quiz.getQuiz_id() %>" />
        <button type="submit" class="btn start-btn">Start</button>
    </form>

    <form action="/quizzes/show" method="get" class="start-form">
            <input type="hidden" name="quizId" value="<%= quiz.getQuiz_id() %>" />
            <button type="submit" class="btn start-btn">View</button>
        </form>

    <button class="btn share-btn" onclick="toggleFriendsList('<%= quiz.getQuiz_id() %>')">Share</button>

    <div class="friends-list" id="friends-list-<%= quiz.getQuiz_id() %>" style="display:none;">
        <% if (friends != null && !friends.isEmpty()) { %>
        <ul>
            <% for (User friend : friends) { %>
            <li>
                <span><%= friend.getUsername() %></span>
                <button class="btn send-challenge-btn" onclick="sendChallenge('<%= quiz.getQuiz_id() %>',
                        '<%= friend.getUserId() %>', this)">Send Challenge</button>
            </li>
            <% } %>
        </ul>
        <% } else { %>
        <p>No friends available.</p>
        <% } %>
    </div>
</div>

<script>
    function toggleFriendsList(quizId) {
        const panel = document.getElementById('friends-list-' + quizId);
        if (panel.style.display === 'none' || panel.style.display === '') {
            panel.style.display = 'block';
        } else {
            panel.style.display = 'none';
        }
    }

    function sendChallenge(quizId, friendId, btn) {
        btn.disabled = true; // Disable button to avoid double click
        btn.textContent = "Sending...";

        fetch('SendChallengeServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `quizId=${encodeURIComponent(quizId)}&friendId=${encodeURIComponent(friendId)}`
        })
            .then(response => {
                if (response.ok) {
                    btn.textContent = "Sent!";
                } else {
                    btn.textContent = "Failed, try again";
                    btn.disabled = false;
                }
            })
            .catch(() => {
                btn.textContent = "Failed, try again";
                btn.disabled = false;
            });
    }
</script>
