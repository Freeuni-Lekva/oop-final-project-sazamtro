package Servlets.FriendRequests;

import DAO.FriendRequestDAO;
import DAO.MessageDAO;
import DAO.UserDAO;
import Servlets.Message.MessageAtributeNames;
import bean.FriendRequest;
import bean.Message.Message;
import bean.Message.MessageType;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/GetFriendRequestsServlet")
public class GetFriendRequestsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute(RequestAtributeNames.CONNECTION);
        FriendRequestDAO requestDAO = new FriendRequestDAO(connection);
        MessageDAO mDAO = new MessageDAO(connection);
        UserDAO userDAO = new UserDAO(connection);
        User user = (User) req.getSession().getAttribute(RequestAtributeNames.USER);

        try {
            List<FriendRequest> requests = requestDAO.getPendingReceivedRequests(user.getUserId());
            req.setAttribute(RequestAtributeNames.FIREND_REQUESTS, requests);

            List<User> senders = new ArrayList<>();
            for(FriendRequest fr : requests){
                senders.add(userDAO.getUserById(fr.getSenderId()));
            }
            req.setAttribute("requestSenders", senders);
            req.getRequestDispatcher("/friend-requests-dropdown.jsp").include(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException("Failed To Get Friend Requests", e);
        }
    }
}

