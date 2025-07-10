package Servlets.Message;

import DAO.MessageDAO;
import DAO.UserDAO;
import bean.Message.*;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@WebServlet("/GetMessagesServlet")
public class GetMessagesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute(MessageAtributeNames.CONNECTION);
        MessageDAO mDAO = new MessageDAO(connection);
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute(MessageAtributeNames.USER);

        String other_username = req.getParameter(MessageAtributeNames.RECEIVER_USERNAME);

        if(other_username == null || other_username.trim().isEmpty()){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Username not found");
            return;
        }

        UserDAO uDAO = new UserDAO(connection);
        User other;

        try {
            other = uDAO.getUserByUsername(other_username);
            if (other == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }
            List<Message> messageList = mDAO.getConversation(user.getUserId(), other.getUserId());
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(toJsonArray(messageList, user.getUserId()));
        } catch (SQLException e) {
            throw new RuntimeException("Failed To Get Conversation", e);
        }
    }

    private static String toJsonArray(List<Message> messages, int currentUserId) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            sb.append("{");
            sb.append("\"sender\": \"").append(msg.getSender_id() == currentUserId ? "YOU" : "THEM").append("\",");
            sb.append("\"content\": \"").append(msg.getContent().replace("\"", "\\\"")).append("\",");
            sb.append("\"timestamp\": \"").append(msg.getTimestamp()).append("\"");
            sb.append("}");
            if (i < messages.size() - 1) sb.append(",");
        }

        sb.append("]");
        return sb.toString();
    }
}