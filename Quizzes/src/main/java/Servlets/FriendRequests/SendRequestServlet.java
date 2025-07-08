package Servlets.FriendRequests;

import DAO.FriendRequestDAO;
import DAO.MessageDAO;
import DAO.UserDAO;
import Servlets.Message.SendMessageServlet;
import bean.FriendRequest;
import bean.Message.Message;
import bean.Message.RequestMessage;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SendRequestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute(RequestAtributeNames.CONNECTION);
        UserDAO userDAO = new UserDAO(connection);
        FriendRequestDAO requestDAO = new FriendRequestDAO(connection);
        MessageDAO messageDAO = new MessageDAO(connection);

        User sender = (User) req.getSession().getAttribute(RequestAtributeNames.USER);
        String receiverUsername = req.getParameter(RequestAtributeNames.RECEIVER_USERNAME);

        if(receiverUsername == null || receiverUsername.trim().isEmpty()){
            resp.sendRedirect("/error.jsp");
            return;
        }

        User receiver;
        try {
            receiver = userDAO.getUserByUsername(receiverUsername);
            if(receiver == null){
                resp.sendRedirect("/error.jsp");
                return;
            }
            if(receiver.getUserId() == sender.getUserId()){
                resp.sendRedirect("/error.jsp");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(requestDAO.friendRequestExists(sender.getUserId(), receiver.getUserId())){
            resp.sendRedirect("/request-exists.jsp");
            return;
        }

        FriendRequest friendRequest = new FriendRequest(sender.getUserId(), receiver.getUserId());
        RequestMessage requestMessage = new RequestMessage(Message.DEFAULT_MESSAGE_ID, sender.getUserId(), receiver.getUserId(), "NEED A DISCORD MEETING FOR THIS", null, false);
        try {
            messageDAO.sendFriendRequest(requestMessage);
        } catch (SQLException e) {
            throw new RuntimeException("Failed To Send Friend Request Message" ,e);
        }
        requestDAO.sendFriendRequest(friendRequest);
        resp.sendRedirect("/request-sent.jsp");
    }
}
