package Servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SubmitQuizServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int quiz_id = Integer.parseInt(request.getParameter("quiz_id"));
        int user_id = Integer.parseInt(request.getParameter("user_id"));

    }
}
