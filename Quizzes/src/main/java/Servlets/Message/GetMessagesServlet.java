package Servlets.Message;

import DAO.MessageDAO;
import DAO.UserDAO;
import bean.Message.*;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetMessagesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute(MessageAtributeNames.CONNECTION);
        MessageDAO mDAO = new MessageDAO(connection);
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute(MessageAtributeNames.USER);

        String other_username = (String) req.getAttribute(MessageAtributeNames.RECEIVER_USERNAME);

        if(other_username == null || other_username.trim().isEmpty()){
            resp.sendRedirect("/error.jsp");
            return;
        }

        UserDAO uDAO = new UserDAO(connection);
        User other;

        List<Message> messageList;
        try {
            other = uDAO.getUserByUsername(other_username);
            messageList = mDAO.getConversation(user.getUserId(), other.getUserId());
            req.setAttribute("messageList", messageList);
            req.getRequestDispatcher("/messages.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException("Failed To Get Conversation", e);
        }
    }
}