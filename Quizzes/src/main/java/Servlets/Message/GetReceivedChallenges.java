package Servlets.Message;

import DAO.MessageDAO;
import bean.Message.Message;
import bean.Message.MessageType;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class GetReceivedChallenges extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute(MessageAtributeNames.CONNECTION);
        MessageDAO mDAO = new MessageDAO(connection);
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute(MessageAtributeNames.USER);
        List<Message> messageList = mDAO.getReceivedTypeMessages(user.getUserId(), MessageType.CHALLENGE);
        req.setAttribute("messageList", messageList);
        req.getRequestDispatcher("/messages.jsp").forward(req, resp);
    }
}
