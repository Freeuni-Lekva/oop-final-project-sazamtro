package Servlets;

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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/UserProfileServlet")
public class UserProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");
        User user = (User) req.getSession().getAttribute("user");

        String other_username = req.getParameter("username");
        String mode = req.getParameter("mode");
        if(other_username == null || other_username.trim().isEmpty()){
            resp.sendRedirect("/error.jsp");
            return;
        }
        if(other_username.equals(user.getUsername())){
            resp.sendRedirect("/myProfile.jsp");
            return;
        }

        UserDAO userDAO = new UserDAO(connection);
        try {
            User other = userDAO.getUserByUsername(other_username);
            if(other == null){
                resp.sendRedirect("/error.jsp");
                return;
            }
            FriendRequestDAO friendRequestDAO = new FriendRequestDAO(connection);
            boolean areFriends = friendRequestDAO.areFriends(user, other);
            boolean requestExists = friendRequestDAO.friendRequestExists(other.getUserId(), user.getUserId());
            List<User> mutualFriends = getMutualFriends(user, other, friendRequestDAO);

            req.setAttribute("targetUser", other);
            req.setAttribute("mutualFriends", mutualFriends);
            req.setAttribute("areFriends", areFriends);
            req.setAttribute("friendRequestStatus", requestExists);
            req.setAttribute("mode", mode); // optional, can be null

            req.getRequestDispatcher("/userProfile.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private List<User> getMutualFriends(User user1, User user2, FriendRequestDAO dao){
        List<User> result = new ArrayList<>();
        try {
            List<User> friends1 = dao.getFriendsList(user1);
            List<User> friends2 = dao.getFriendsList(user2);
            for(int i = 0; i < friends1.size(); i++){
                for(int j = 0; j < friends2.size(); j++){
                    if(friends1.get(i).getUserId() == friends2.get(j).getUserId()){
                        result.add(friends1.get(i));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
