package Servlets.FriendRequests;

import DAO.FriendRequestDAO;
import DAO.UserDAO;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
@WebServlet("/DeleteFriendServlet")
public class DeleteFriendServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute(RequestAtributeNames.CONNECTION);
        FriendRequestDAO requestDAO = new FriendRequestDAO(connection);
        UserDAO userDAO = new UserDAO(connection);

        User user = (User) req.getSession().getAttribute(RequestAtributeNames.USER);

        int friendId;
        try {
            friendId = Integer.parseInt(req.getParameter("friend_id"));
        } catch (NumberFormatException e) {
            resp.sendRedirect("/error.jsp");
            return;
        }

        User friend;
        try {
            friend = userDAO.getUserById(friendId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed To Get Friend", e);
        }

        if (friend == null) {
            resp.sendRedirect("/error.jsp");
            return;
        }

        try {
            if (!requestDAO.areFriends(user, friend)) {
                resp.sendRedirect("/error.jsp");
                return;
            }

            requestDAO.removeFriendship(user, friend);
            resp.sendRedirect("/GetFriendListServlet");
        } catch (SQLException e) {
            throw new RuntimeException("Failed To Delete Friend", e);
        }
    }
}
