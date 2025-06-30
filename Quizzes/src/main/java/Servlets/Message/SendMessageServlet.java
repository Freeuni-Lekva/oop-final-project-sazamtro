package Servlets.Message;

import DAO.DatabaseConnection;
import DAO.MessageDAO;
import DAO.Quiz.QuizDAO;
import DAO.UserDAO;
import bean.Message.*;
import bean.Quiz;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

public class SendMessageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = DatabaseConnection.getConnection();
        MessageDAO mDAO = new MessageDAO(connection);
        UserDAO uDAO = new UserDAO(connection);
        HttpSession session = req.getSession();

        int message_id = Message.DEFAULT_MESSAGE_ID;
        User sender = (User) session.getAttribute("user");
        User receiver;
        String receiver_username = req.getParameter("receiver_username");
        String content =  req.getParameter("content");
        String type = req.getParameter("type");
        String quiz_id = req.getParameter("quiz_id");

        if (type ==  null) {
            resp.sendRedirect("/message-fail.jsp");
            return;
        }

        type = type.toUpperCase();

        try {
            if(receiver_username == null){
                resp.sendRedirect("/no-receiver-found.jsp");
                return;
            }
            receiver = uDAO.getUserByUsername(receiver_username);

            if(receiver == null){
                resp.sendRedirect("/no-receiver-found.jsp");
                return;
            }

            if(content == null || content.trim().isEmpty()){
                resp.sendRedirect("/no-content.jsp");
                return;
            }

            if(type.equals("CHALLENGE")){
                if(quiz_id == null){
                    resp.sendRedirect("/no-quiz-found.jsp");
                    return;
                }
                try {
                    Quiz quiz = QuizDAO.getOneQuiz(connection, Integer.parseInt(quiz_id));
                    if (quiz == null) {
                        resp.sendRedirect("/no-quiz-found.jsp");
                        return;
                    }
                }catch (NumberFormatException e){
                    resp.sendRedirect("/no-quiz-found.jsp");
                    return;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        switch (type){
            case "NOTE":
                NoteMessage noteMessage = new NoteMessage(message_id, sender.getUserId(), receiver.getUserId(), content, null, false);
                mDAO.sendNote(noteMessage);
                resp.sendRedirect("/note-sent.jsp");
                break;
            case "FRIEND_REQUEST":
                RequestMessage requestMessage = new RequestMessage(message_id, sender.getUserId(), receiver.getUserId(), content, null, false);
                mDAO.sendFriendRequest(requestMessage);
                resp.sendRedirect("/request-sent.jsp");
                break;
            case "CHALLENGE":
                ChallengeMessage challengeMessage = new ChallengeMessage(message_id, sender.getUserId(),
                        receiver.getUserId(), content, Integer.parseInt(quiz_id), null, false);
                mDAO.sendChallenge(challengeMessage);
                resp.sendRedirect("/challenge-sent.jsp");
                break;
            default:
                resp.sendRedirect("/message-fail.jsp");
        }
    }
}