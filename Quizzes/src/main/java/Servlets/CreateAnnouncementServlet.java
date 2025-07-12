package Servlets;

import DAO.AnnouncementDAO;
import bean.Announcement;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/CreateAnnouncementServlet")
public class CreateAnnouncementServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");
        AnnouncementDAO announcementDAO = new AnnouncementDAO(connection);

        String text = req.getParameter("announcementText");
        if(text == null || text.trim().isEmpty()){
            resp.sendRedirect("/AnnouncementServlet?mode=admin");
            return;
        }

        try {
            announcementDAO.addAnnouncement(new Announcement(-1, user.getUserId(), user.getUsername(),text, null));
            resp.sendRedirect("/AnnouncementServlet?mode=admin");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
