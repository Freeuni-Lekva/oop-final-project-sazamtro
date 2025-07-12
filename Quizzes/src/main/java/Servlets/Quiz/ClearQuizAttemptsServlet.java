package Servlets.Quiz;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/ClearQuizAttemptsServlet")
public class ClearQuizAttemptsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String quizIdParam = req.getParameter("id");
        if (quizIdParam == null) {
            resp.sendRedirect("/error.jsp");
            return;
        }

        int quizId;
        try {
            quizId = Integer.parseInt(quizIdParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect("/error.jsp");
            return;
        }

        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");
        try {
            String deleteAnswers = "DELETE FROM UserAnswers WHERE attempt_id IN (SELECT attempt_id FROM QuizAttempts WHERE quiz_id = ?)";
            try (PreparedStatement stmt = connection.prepareStatement(deleteAnswers)) {
                stmt.setInt(1, quizId);
                stmt.executeUpdate();
            }

            // Then delete from QuizAttempts
            String deleteAttempts = "DELETE FROM QuizAttempts WHERE quiz_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteAttempts)) {
                stmt.setInt(1, quizId);
                stmt.executeUpdate();
            }

            String deleteMessages = "DELETE FROM Messages WHERE quiz_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteMessages)) {
                stmt.setInt(1, quizId);
                stmt.executeUpdate();
            }
            resp.sendRedirect("/showAllQuizzes?mode=admin");

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("/error.jsp");
        }
    }
}
