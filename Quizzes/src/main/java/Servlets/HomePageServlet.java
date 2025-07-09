package Servlets;

import DAO.AnnouncementDAO;
import DAO.FriendRequestDAO;
import DAO.MessageDAO;
import DAO.UserDAO;
import bean.FriendRequest;
import bean.Message.Message;
import bean.Message.MessageType;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/HomePageServlet")
public class HomePageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
            req.getRequestDispatcher("/homepage.jsp").forward(req, resp);
    }
}
