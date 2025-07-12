package Servlets;

import DAO.UserDAO;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/UpdateUserProfilePictureServlet")
public class UpdateUserProfilePictureServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection =(Connection) req.getServletContext().getAttribute("DBConnection");
        User user = (User) req.getSession().getAttribute("user");
        String pictureUrl = req.getParameter("profilePicUrl");

        if(user == null || pictureUrl == null){
            resp.sendRedirect("/error.jsp");
            return;
        }
        UserDAO userDAO = new UserDAO(connection);
        try {
            userDAO.updateProfilePicture(user.getUserId(), pictureUrl);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        user.setProfilePictureUrl(pictureUrl);
        resp.sendRedirect("/myProfile.jsp");
    }
}
