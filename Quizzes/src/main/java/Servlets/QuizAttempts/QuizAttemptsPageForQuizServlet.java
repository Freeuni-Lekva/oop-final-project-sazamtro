package Servlets.QuizAttempts;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/QuizAttemptsPageForQuizServlet")
public class QuizAttemptsPageForQuizServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String quizIdParam = request.getParameter("quizId");

        if (quizIdParam == null) {
            response.sendRedirect("error.jsp");
            return;
        }

        try {
            int quizId = Integer.parseInt(quizIdParam);
            request.setAttribute("quizId", quizId);
            request.getRequestDispatcher("/quizAttempts-quizOptions.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
