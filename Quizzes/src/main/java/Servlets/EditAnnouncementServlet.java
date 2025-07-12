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

@WebServlet("/EditAnnouncementServlet")
public class EditAnnouncementServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String announcementId = req.getParameter("announcementId");
        String text = req.getParameter("announcementText");
        if(announcementId == null || announcementId.trim().isEmpty() || text == null || text.trim().isEmpty()){
            resp.sendRedirect("/erros.jsp");
            return;
        }
        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");
        AnnouncementDAO announcementDAO = new AnnouncementDAO(connection);
        try {
            announcementDAO.setAnnouncementText(Integer.parseInt(announcementId), text);
            resp.sendRedirect("/AnnouncementServlet?mode=admin");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
