package Servlets.FriendRequests;

import DAO.FriendRequestDAO;
import DAO.MessageDAO;
import DAO.UserDAO;
import Servlets.Message.MessageAtributeNames;
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
import java.util.List;
import java.util.Set;

@WebServlet("/GetFriendListServlet")
public class GetFriendListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute(RequestAtributeNames.CONNECTION);
        FriendRequestDAO requestDAO = new FriendRequestDAO(connection);

        User user = (User) req.getSession().getAttribute(RequestAtributeNames.USER);
        try {
            List<User> friends = requestDAO.getFriendsList(user);
            req.setAttribute(RequestAtributeNames.FRIEND_LIST, friends);

            if(req.getParameter("mode") != null && "sidebar".equals(req.getParameter("mode"))) {
                MessageDAO mDAO = new MessageDAO(connection);
                Set<Integer> senderIds = mDAO.getUnreadSenderIds(user.getUserId());
                req.setAttribute(MessageAtributeNames.UNREAD_SENDER_IDS, senderIds);
                req.getRequestDispatcher("/friend-sidebar.jsp").include(req, resp);
            }else{
                req.getRequestDispatcher("/friends.jsp").forward(req, resp);
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed To Get Friend List", e);
        }
    }
}
