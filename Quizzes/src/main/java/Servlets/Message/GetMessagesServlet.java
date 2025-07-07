package Servlets.Message;

import DAO.MessageDAO;
import bean.Message.*;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class GetMessagesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute(MessageAtributeNames.CONNECTION);
        MessageDAO mDAO = new MessageDAO(connection);
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute(MessageAtributeNames.USER);

        String type = req.getParameter(MessageAtributeNames.TYPE);

        String sent_received = req.getParameter(MessageAtributeNames.SENT_RECEIVED);

        if (type ==  null) {
            resp.sendRedirect("/message-fail.jsp");
            return;
        }

        type = type.toUpperCase();

        MessageType messageType;
        try {
            messageType = MessageType.valueOf(type);
        } catch (IllegalArgumentException e) {
            resp.sendRedirect("/message-fail.jsp");
            return;
        }
        List<Message> messageList;
        switch (sent_received){
            case "received":
                messageList = mDAO.getReceivedTypeMessages(user.getUserId(), messageType);
                break;
            case "sent":
                messageList = mDAO.getSentTypeMessages(user.getUserId(), messageType);
                break;
            default:
                resp.sendRedirect("/message-fail.jsp");
                return;
        }
        req.setAttribute("messageList", messageList);
        req.getRequestDispatcher("/messages.jsp").forward(req, resp);
    }
}