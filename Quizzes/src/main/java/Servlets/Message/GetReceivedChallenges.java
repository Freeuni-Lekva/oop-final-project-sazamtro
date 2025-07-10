package Servlets.Message;

import DAO.MessageDAO;
import DAO.UserDAO;
import bean.FriendRequest;
import bean.Message.Message;
import bean.Message.MessageType;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/GetReceivedChallenges")
public class GetReceivedChallenges extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute(MessageAtributeNames.CONNECTION);
        MessageDAO mDAO = new MessageDAO(connection);
        HttpSession session = req.getSession();
        UserDAO userDAO = new UserDAO(connection);

        User user = (User) session.getAttribute(MessageAtributeNames.USER);
        try {
            List<Message> messageList = mDAO.getReceivedTypeMessages(user.getUserId(), MessageType.CHALLENGE);
            req.setAttribute("challengeMessages", messageList);
            List<User> challengeSenders = new ArrayList<>();
            for(Message m: messageList){
                challengeSenders.add(userDAO.getUserById(m.getSender_id()));
            }
            req.setAttribute("challengeSenders", challengeSenders);
            req.getRequestDispatcher("/challenge-dropdown.jsp").include(req, resp);
        }catch (SQLException e){
            throw new RuntimeException("Failed Get Received Challenges", e);
        }
    }
}
