package Servlets;

import DAO.AnnouncementDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/DeleteAnnouncementServlet")
public class DeleteAnnouncementServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String announcementId = req.getParameter("announcementId");
        if(announcementId == null || announcementId.trim().isEmpty()){
            resp.sendRedirect("/error.jsp");
        }

        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");
        AnnouncementDAO announcementDAO = new AnnouncementDAO(connection);
        try {
            announcementDAO.deleteAnnouncement(Integer.parseInt(announcementId));
            resp.sendRedirect("/AnnouncementServlet?mode=admin");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
