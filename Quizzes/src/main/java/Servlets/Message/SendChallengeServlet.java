package Servlets.Message;

import DAO.MessageDAO;
import DAO.QuizDAO;
import DAO.UserDAO;
import bean.Message.ChallengeMessage;
import bean.Message.Message;
import bean.Quiz;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/SendChallengeServlet")
public class SendChallengeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        int message_id = Message.DEFAULT_MESSAGE_ID;
        User sender = (User) session.getAttribute(MessageAtributeNames.USER);
        User receiver;
        String receiver_id;
        String quiz_id;

        Connection connection = (Connection) req.getServletContext().getAttribute(MessageAtributeNames.CONNECTION);
        Quiz quiz;
        try {
            receiver_id = req.getParameter("friendId");
            if(receiver_id == null || receiver_id.trim().isEmpty()){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Receiver ID missing");
                return;
            }
            UserDAO uDAO = new UserDAO(connection);
            receiver = uDAO.getUserById(Integer.parseInt(receiver_id));

            if(receiver == null){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Receiver Not Found");
                return;
            }
            quiz_id = req.getParameter("quizId");
            if(quiz_id == null || quiz_id.trim().isEmpty()){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quiz ID missing");
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
        String content = quiz.getQuizTitle();
        ChallengeMessage challengeMessage = new ChallengeMessage(message_id, sender.getUserId(), receiver.getUserId(), content, quiz.getQuiz_id(), null,false);
        MessageDAO mDAO = new MessageDAO(connection);
        try {
            mDAO.sendChallenge(challengeMessage);
        } catch (SQLException e) {
            throw new RuntimeException("Failed To Send Challenge Message" ,e);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
