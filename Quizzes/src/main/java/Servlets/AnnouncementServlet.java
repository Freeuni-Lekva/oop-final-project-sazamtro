package Servlets;

import DAO.AnnouncementDAO;
import DAO.UserDAO;
import bean.Announcement;

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

@WebServlet("/AnnouncementServlet")
public class AnnouncementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        AnnouncementDAO announcementDAO = new AnnouncementDAO(connection);

        try {
            List<Announcement> announcements = announcementDAO.getAllAnnouncements();
            request.setAttribute("announcements", announcements);
            request.setAttribute("showAnnouncements", true);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("announcements", new ArrayList<>());
            request.setAttribute("showAnnouncements", true);
        }
        String mode = request.getParameter("mode");

        if (mode != null && "admin".equals(mode)) {
            request.getRequestDispatcher("/announcement-admin.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/homepage.jsp").include(request, response);
        }

    }
}
