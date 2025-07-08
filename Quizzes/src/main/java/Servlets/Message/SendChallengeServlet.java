package Servlets.Message;

import DAO.MessageDAO;
import DAO.QuizDAO;
import DAO.UserDAO;
import bean.Message.ChallengeMessage;
import bean.Message.Message;
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

public class SendChallengeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        int message_id = Message.DEFAULT_MESSAGE_ID;
        User sender = (User) session.getAttribute(MessageAtributeNames.USER);
        User receiver;
        String receiver_username;
        String quiz_id;

        Connection connection = (Connection) req.getServletContext().getAttribute(MessageAtributeNames.CONNECTION);
        Quiz quiz;
        try {
            receiver_username = req.getParameter(MessageAtributeNames.RECEIVER_USERNAME);
            if(receiver_username == null){
                resp.sendRedirect("/no-receiver-found.jsp");
                return;
            }
            UserDAO uDAO = new UserDAO(connection);
            receiver = uDAO.getUserByUsername(receiver_username);

            if(receiver == null){
                resp.sendRedirect("/no-receiver-found.jsp");
                return;
            }
            quiz_id = req.getParameter(MessageAtributeNames.QUIZ_ID);
            if(quiz_id == null || quiz_id.trim().isEmpty()){
                resp.sendRedirect("/no-quiz-id.jsp");
                return;
            }
            QuizDAO qDAO = new QuizDAO(connection);
            try {
                quiz = qDAO.getOneQuiz(Integer.parseInt(quiz_id));
                if(quiz == null){
                    resp.sendRedirect("/no-quiz.jsp");
                    return;
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("QUIZ_ID FORMAT IS ILLEGAL" ,e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String content =  req.getParameter(MessageAtributeNames.QUIZ_ID);

        ChallengeMessage challengeMessage = new ChallengeMessage(message_id, sender.getUserId(), receiver.getUserId(), content, quiz.getQuiz_id(), null,false);
        MessageDAO mDAO = new MessageDAO(connection);
        mDAO.sendChallenge(challengeMessage);
        resp.sendRedirect("/note-sent.jsp");
    }
}
