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

@WebServlet("/SendMessageServlet")
public class SendMessageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        int message_id = Message.DEFAULT_MESSAGE_ID;
        User sender = (User) session.getAttribute(MessageAtributeNames.USER);

        User receiver;
        String receiver_username;
        String content;

        Connection connection = (Connection) req.getServletContext().getAttribute(MessageAtributeNames.CONNECTION);
        try {
            receiver_username = req.getParameter(MessageAtributeNames.RECEIVER_USERNAME);
            if(receiver_username == null){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Receiver User Name Is Null");
                return;
            }
            UserDAO uDAO = new UserDAO(connection);
            receiver = uDAO.getUserByUsername(receiver_username);

            if(receiver == null){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Receiver Does Not Exist");;
                return;
            }
            content =  req.getParameter(MessageAtributeNames.CONTENT);
            if(content == null || content.trim().isEmpty()){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Message is Empty");
                return;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        NoteMessage noteMessage = new NoteMessage(message_id, sender.getUserId(), receiver.getUserId(), content, null, false);
        MessageDAO mDAO = new MessageDAO(connection);
        try {
            mDAO.sendNote(noteMessage);
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Failed To Send Note Message", e);
        }
        resp.setStatus(HttpServletResponse.SC_OK);

    }
}