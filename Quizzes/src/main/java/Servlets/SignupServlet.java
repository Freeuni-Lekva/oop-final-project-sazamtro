package Servlets;

import DAO.DatabaseConnection;
import DAO.UserDAO;
import bean.Hasher;
import bean.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("passwordConfirm");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty() || passwordConfirm == null || passwordConfirm.trim().isEmpty()) {
            response.sendRedirect("/invalid-input.jsp");
            return;
        }

        if (!password.equals(passwordConfirm)) {
            response.sendRedirect("password-confirmationError.jsp");
            return;
        }

        Connection connection = (Connection) request.getServletContext().getAttribute("DBConnection");

        try {
            UserDAO userDAO = new UserDAO(connection);
            User user = userDAO.getUserByUsername(username);

            if (user != null) {
                response.sendRedirect("/user-already-exists.jsp");
                return;
            }

            int defaultUserId = 0;
            String hashedPassword = Hasher.hashPassword(password);
            String defaultProfilePictureUrl = "";
            boolean defaultIsAdmin = false;

            User userToAdd = new User(defaultUserId, username, hashedPassword, defaultProfilePictureUrl, defaultIsAdmin);
            userDAO.addUser(userToAdd);

            HttpSession session = request.getSession();
            session.setAttribute("user", userToAdd);

            response.sendRedirect("HomePageServlet");
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            response.sendRedirect("/error.jsp");
        }
    }
}
