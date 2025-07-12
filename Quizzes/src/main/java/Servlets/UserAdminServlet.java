package Servlets.Admin;

import DAO.UserDAO;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/UserAdminServlet")
public class UserAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");
        UserDAO userDAO = new UserDAO(connection);

        String searchQuery = req.getParameter("search");
        List<User> users;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            User user = null;
            try {
                user = userDAO.getUserByUsername(searchQuery.trim());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            users = new ArrayList<>();
            if (user != null) {
                users.add(user);
            }
        } else {
            try {
                users = userDAO.getAllUsers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        req.setAttribute("userList", users);
        req.getRequestDispatcher("/users-admin-page.jsp").forward(req, resp);
    }
}
