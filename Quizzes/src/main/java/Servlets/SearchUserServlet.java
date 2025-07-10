package Servlets;

import DAO.UserDAO;
import Servlets.FriendRequests.RequestAtributeNames;
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
import DAO.FriendRequestDAO;
@WebServlet("/SearchUserServlet")
public class SearchUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute(RequestAtributeNames.CONNECTION);
        UserDAO userDAO = new UserDAO(connection);
        FriendRequestDAO requestDAO = new FriendRequestDAO(connection);

        User currentUser = (User) req.getSession().getAttribute(RequestAtributeNames.USER);
        String searchedUsername = req.getParameter("username");

        if (searchedUsername == null || searchedUsername.trim().isEmpty()) {
            resp.sendRedirect("/error.jsp");
            return;
        }

        try {
            User foundUser = userDAO.getUserByUsername(searchedUsername);
            if (foundUser == null || foundUser.getUserId() == currentUser.getUserId()) {
                req.setAttribute("userNotFound", true);
            } else {
                req.setAttribute("foundUser", foundUser);

                boolean areFriends = requestDAO.areFriends(currentUser, foundUser);
                boolean requestSent = requestDAO.friendRequestExists(currentUser.getUserId(), foundUser.getUserId());
                boolean requestReceived = requestDAO.friendRequestExists(foundUser.getUserId(), currentUser.getUserId());

                req.setAttribute("areFriends", areFriends);
                req.setAttribute("requestSent", requestSent);
                req.setAttribute("requestReceived", requestReceived);
            }

            req.getRequestDispatcher("/GetFriendListServlet").forward(req, resp);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to search user", e);
        }
    }
}
