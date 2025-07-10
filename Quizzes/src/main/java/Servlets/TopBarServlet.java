package Servlets;

import DAO.FriendRequestDAO;
import DAO.MessageDAO;
import DAO.UserDAO;
import Servlets.FriendRequests.RequestAtributeNames;
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

@WebServlet("/TopBarServlet")
public class TopBarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute(RequestAtributeNames.CONNECTION);
        FriendRequestDAO requestDAO = new FriendRequestDAO(connection);
        MessageDAO mDAO = new MessageDAO(connection);
        UserDAO userDAO = new UserDAO(connection);
        User user = (User) req.getSession().getAttribute(RequestAtributeNames.USER);

        try {
            List<FriendRequest> requests = requestDAO.getPendingReceivedRequests(user.getUserId());
            List<Message> challenges =  mDAO.getReceivedTypeMessages(user.getUserId(), MessageType.CHALLENGE);

            req.setAttribute("requests", requests);
            req.setAttribute("challenges", challenges);

            req.getRequestDispatcher("/topbar.jsp").include(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException("Failed To Get Friend Requests", e);
        }
    }
}
