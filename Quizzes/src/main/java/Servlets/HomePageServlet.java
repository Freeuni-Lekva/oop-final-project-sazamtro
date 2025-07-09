package Servlets;

import DAO.AnnouncementDAO;
import DAO.FriendRequestDAO;
import DAO.MessageDAO;
import DAO.UserDAO;
import bean.FriendRequest;
import bean.Message.Message;
import bean.Message.MessageType;
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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ✅ Get DB connection and user from session
        Connection conn = (Connection) getServletContext().getAttribute("DBConnection");
        HttpSession session = req.getSession(false);

        if (conn == null || session == null || session.getAttribute("user") == null) {
            // Redirect safely if no connection or no logged-in user
            resp.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        try {
            // ✅ Initialize DAOs
            AnnouncementDAO annDAO = new AnnouncementDAO(conn);
            FriendRequestDAO frDAO = new FriendRequestDAO(conn);
            MessageDAO mDAO = new MessageDAO(conn);
            UserDAO userDAO = new UserDAO(conn);

            // ✅ Fetch data
            List<FriendRequest> friendRequests = frDAO.getPendingReceivedRequests(user.getUserId());
//            List<Message> challengeMessages = mDAO.getReceivedTypeMessages(user.getUserId(), MessageType.CHALLENGE);

            // ✅ Fetch sender users
            List<User> requestSenders = new ArrayList<>();
            for (FriendRequest fr : friendRequests) {
                requestSenders.add(userDAO.getUserById(fr.getSenderId()));
            }

            // ✅ Set attributes
            req.setAttribute("announcements", annDAO.getAllAnnouncements());
            req.setAttribute("friendRequests", friendRequests);
            req.setAttribute("requestSenders", requestSenders);

            // ✅ Forward to JSP safely (no output written yet)
            req.getRequestDispatcher("/homepage.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally redirect to an error page
            resp.sendRedirect("error.jsp");
        }
    }
}
