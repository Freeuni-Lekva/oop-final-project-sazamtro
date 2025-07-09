package Servlets;

import DAO.AnnouncementDAO;
import DAO.FriendRequestDAO;
import DAO.UserDAO;
import bean.FriendRequest;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/HomePageServlet")
public class HomePageServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Connection conn = (Connection) getServletContext().getAttribute("DBConnection");
//        User user = (User) req.getSession().getAttribute("user");
        UserDAO userDAO = new UserDAO(conn);
        User user = null;
        try {
            user = userDAO.getUserById(2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        AnnouncementDAO annDAO = new AnnouncementDAO(conn);
        FriendRequestDAO frDAO = new FriendRequestDAO(conn);

        try {
            req.setAttribute("announcements", annDAO.getAllAnnouncements());
            req.setAttribute("friendRequests", frDAO.getPendingReceivedRequests(user.getUserId()));

            List<FriendRequest> friendRequestList = (List) req.getAttribute("friendRequests");
            List<User> senders = new ArrayList<>();
            for(FriendRequest fr : friendRequestList){
                senders.add(userDAO.getUserById(fr.getSenderId()));
            }
            req.setAttribute("requestSenders", senders);
        } catch (SQLException e) {
            System.out.println("Failed To Load HomePage");
        }

        try {
            req.getRequestDispatcher("/homepage.jsp").forward(req, resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
