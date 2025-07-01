package Servlets.Quiz;

import DAO.DatabaseConnection;
import DAO.QuizDAO;
import bean.Quiz;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;


//Can't finish without Answers and Questions DAO classes
public class ShowOneQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        try(Connection connection = DatabaseConnection.getConnection();){
            Quiz curr = QuizDAO.getOneQuiz(connection, 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
