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

public class GetFriendListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");
        FriendRequestDAO requestDAO = new FriendRequestDAO(connection);

        User user = (User) req.getSession().getAttribute("user");

        List<User> friends = requestDAO.getFriendsList(user);
        req.setAttribute("FriendList", friends);
        req.getRequestDispatcher("/friend-list.jsp").forward(req, resp);
    }
}
