package Servlets.FriendRequests;

import DAO.FriendRequestDAO;
import DAO.UserDAO;
import Servlets.HomePageServlet;
import bean.FriendRequest;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/RespondToRequestServlet")
public class RespondToRequestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute(RequestAtributeNames.CONNECTION);
        FriendRequestDAO requestDAO = new FriendRequestDAO(connection);

//        User user = (User) req.getSession().getAttribute(RequestAtributeNames.USER);
        String request_id = req.getParameter(RequestAtributeNames.REQUEST_ID);

        if(request_id == null || request_id.trim().isEmpty()){
            resp.sendRedirect("/error.jsp");
            return;
        }

        UserDAO userDAO = new UserDAO(connection);
        FriendRequest fr;
        try {
            User user = userDAO.getUserById(1);
            fr = requestDAO.getRequestByID(Integer.parseInt(request_id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String response = req.getParameter(RequestAtributeNames.RESPONSE);
        if(fr == null){
            resp.sendRedirect("/error.jsp");
            return;
        }


        try {
            if (!requestDAO.friendRequestExists(fr.getSenderId(), fr.getReceiverId())) {
                resp.sendRedirect("/error.jsp");
                return;
            }
            switch (response.toLowerCase()) {
                case "accept":
                    requestDAO.approveRequest(fr);
                    break;
                case "reject":
                    requestDAO.rejectRequest(fr);
                    break;
                default:
                    resp.sendRedirect("/error.jsp");
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed To Respond TO Friend Request", e);
        }
        resp.sendRedirect("/HomePageServlet");
    }
}
