package Servlets.FriendRequests;

import DAO.FriendRequestDAO;
import DAO.UserDAO;
import bean.FriendRequest;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class GetFriendRequestsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");
        FriendRequestDAO requestDAO = new FriendRequestDAO(connection);

        User user = (User) req.getSession().getAttribute("user");

        List<FriendRequest> requests = requestDAO.getPendingReceivedRequests(user.getUserId());
        req.setAttribute("FriendRequests", requests);
        req.getRequestDispatcher("/friend-requests.jsp").forward(req, resp);
    }
}
