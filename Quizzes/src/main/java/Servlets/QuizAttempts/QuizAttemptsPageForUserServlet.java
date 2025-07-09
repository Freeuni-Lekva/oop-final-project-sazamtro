package Servlets.QuizAttempts;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/QuizAttemptsPageForUserServlet")
public class QuizAttemptsPageForUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Just forward to JSP that shows the filtering options
        try {
            request.getRequestDispatcher("/quizAttempts-userOptions.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
