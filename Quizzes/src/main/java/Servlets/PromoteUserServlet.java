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

@WebServlet("/PromoteUserServlet")
public class PromoteUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");
        String userId = req.getParameter("userId");
        if(userId == null || userId.trim().isEmpty()){
            resp.sendRedirect("/error.jsp");
            return;
        }
        UserDAO userDAO = new UserDAO(connection);
        try {
            User promotee = userDAO.getUserById(Integer.parseInt(userId));
            if(promotee == null){
                resp.sendRedirect("/error.jsp");
                return;
            }
            userDAO.promoteUser(promotee.getUserId());
            if(req.getParameter("redirect") != null){
                resp.sendRedirect("/UserAdminServlet?mode=admin&username="+promotee.getUsername());
            }
            resp.sendRedirect("/UserAdminServlet");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
