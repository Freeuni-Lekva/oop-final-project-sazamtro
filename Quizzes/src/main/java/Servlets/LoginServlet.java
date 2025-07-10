package Servlets;

import DAO.DatabaseConnection;
import DAO.UserDAO;
import bean.User;
import bean.Hasher;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.sendRedirect("/invalid-input.jsp");
            return;
        }

        Connection connection = (Connection) request.getServletContext().getAttribute("DBConnection");

        try {
            UserDAO userDAO = new UserDAO(connection);
            User user = userDAO.getUserByUsername(username);

            if (user == null) {
                // user not found
                response.sendRedirect("/user-not-found.jsp");
                return;
            }

            String hashedPassword = Hasher.hashPassword(password);
            if (hashedPassword.equals(user.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                response.sendRedirect("HomePageServlet");
            } else {
                response.sendRedirect("/incorrect-password.jsp");
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            response.sendRedirect("/error.jsp");
        }
    }
}