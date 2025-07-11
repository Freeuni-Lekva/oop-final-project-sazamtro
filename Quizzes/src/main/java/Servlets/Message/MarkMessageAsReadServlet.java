package Servlets.Message;

import DAO.MessageDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/MarkMessageAsReadServlet")
public class MarkMessageAsReadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String messageIdParam = req.getParameter("messageId");
        String redirectUrl = req.getParameter("redirect"); // where to go after marking as read

        if (messageIdParam == null || redirectUrl == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        int messageId = Integer.parseInt(messageIdParam);

        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");
        MessageDAO messageDAO = new MessageDAO(connection);
        try {
            messageDAO.markAsRead(messageId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        resp.sendRedirect(redirectUrl);
    }
}
