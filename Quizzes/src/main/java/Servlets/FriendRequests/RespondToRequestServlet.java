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
import java.sql.SQLException;

public class RespondToRequestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");
        FriendRequestDAO requestDAO = new FriendRequestDAO(connection);
        UserDAO userDAO = new UserDAO(connection);

        User user = (User) req.getSession().getAttribute("user");
        String sender_user_name = req.getParameter("sender");
        String response = req.getParameter("response");

        if(sender_user_name == null || sender_user_name.trim().isEmpty()){
            resp.sendRedirect("/error.jsp");
            return;
        }

        if(response == null || response.trim().isEmpty()){
            resp.sendRedirect("/error.jsp");
            return;
        }

        User sender;

        try {
            sender = userDAO.getUserByUsername(sender_user_name);
        } catch (SQLException e) {
            throw new RuntimeException("Failed To Get Sender",e);
        }

        if(sender == null){
            resp.sendRedirect("/error.jsp");
            return;
        }

        if(!requestDAO.friendRequestExists(user.getUserId(), sender.getUserId())){
            resp.sendRedirect("/error.jsp");
            return;
        }

        FriendRequest fr = new FriendRequest(sender.getUserId(), user.getUserId());
        switch (response.toLowerCase()) {
            case "accept":
                requestDAO.approveRequest(fr);
                resp.sendRedirect("/request-approved.jsp");
                break;
            case "reject":
                requestDAO.rejectRequest(fr);
                resp.sendRedirect("/request-rejected.jsp");
                break;
            default:
                resp.sendRedirect("/error.jsp");
        }
    }
}
