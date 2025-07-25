package Servlets.Quiz;

import DAO.QuestionsDAO;
import DAO.QuizDAO;
import bean.Questions.*;
import bean.Quiz;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.RowSetWarning;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/add-question")
public class AddQuestionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get connection from context
        Connection con = (Connection) getServletContext().getAttribute("DBConnection");
        if (con == null) {
            throw new ServletException("Database connection not initialized.");
        }
        QuizDAO quizDAO = new QuizDAO(con);
        QuestionsDAO questionsDAO = new QuestionsDAO(con);


        //get parameters for question

        QuestionType type = getType(req, resp);
        String prompt = getPrompt(req, resp);
        int quizId = getQuizId(req, resp);

        if (type == null || prompt == null || quizId < 0) {
            resp.sendRedirect("/error.jsp?reason=missing_parameters");
            return;
        }

        String imageUrl = getImageUrl(type, req, resp);
        int position;
        try {
            position = getPosition(quizId, quizDAO, req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //creating question object
        Question question;
        try {
            question = QuestionFactory.createQuestion(quizId, type, prompt, position, imageUrl);
            System.out.println("quiz: " + quizId);
            System.out.println("type: " + type.toString());
            System.out.println("prompt: " + prompt);
            System.out.println("position: " + position);
            if (question == null) {
                resp.sendRedirect("/error.jsp?reason=question_creation_failed");
                return;
            }

        } catch (Exception e) {
            resp.sendRedirect("/error.jsp");
            return;
        }

        try {
            int question_id = questionsDAO.insertQuestion(question); //adding to database
            question.setId(question_id);

            // Handle answers
            if (question.hasChoices()) {
                addOptions(questionsDAO, question_id, req, resp);
            } else {
                String correctAnswer = req.getParameter("correctAnswer");
                if (correctAnswer != null && !correctAnswer.trim().isEmpty()) {
                    questionsDAO.insertCorrectAnswerText(question_id, correctAnswer);
                }
            }

        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Database error", e);
        }
        req.setAttribute("quizId", quizId);
        int newPosition = position == -1 ? -1 : position + 1;
        req.setAttribute("position", newPosition);
        RequestDispatcher rd = req.getRequestDispatcher("/AddQuestion.jsp");
        rd.forward(req, resp);
    }

    private QuestionType getType(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String typeStr = req.getParameter("type");
        if (typeStr == null) {
            resp.sendRedirect("/error.jsp?reason=type_not_specified");
            throw new RuntimeException("Type not specified");
        }
        try {
            return QuestionType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            resp.sendRedirect("/error.jsp?reason=invalid_type");
            throw new RuntimeException("Invalid type");
        }
    }
    private String getPrompt(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String prompt = req.getParameter("prompt");
        if (prompt == null) {
            resp.sendRedirect("/error.jsp?reason=prompt_not_specified");
            throw new RuntimeException("Prompt not specified");
        }
        return prompt;
    }
    private int getQuizId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String quizIdStr = req.getParameter("quizId");
        System.out.println("QUIZ ID FROM PARAM: " + quizIdStr);  // <-- Add this line
        return getIntParameter(quizIdStr, resp);
    }
    private String getImageUrl(QuestionType type, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(type != QuestionType.PICTURE_RESPONSE){
            return null;
        }
        String imageUrl = req.getParameter("image_url");
        if (imageUrl == null) {
            resp.sendRedirect("/error.jsp?reason=image_url_not_specified");
            throw new RuntimeException("image_url_not_specified");
        }
        return imageUrl;
    }
    private int getPosition(int quizId, QuizDAO quizDAO, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        Quiz quiz = quizDAO.getOneQuiz(quizId);

        if(quiz == null){
            resp.sendRedirect("/error.jsp?reason=quiz_not_found");
            throw new RuntimeException("quiz_not_found");
        }
        boolean isRandom = quiz.checkIfRandom();

        int position = -1;
        if (!isRandom) {
            String posStr = req.getParameter("position");
            position = getIntParameter(posStr, resp);
            if(position < 0){
                resp.sendRedirect("/error.jsp?reason=invalid_position");
                throw new RuntimeException("invalid_position");
            }
        }
        return position;
    }

    private int getIntParameter(String parameter, HttpServletResponse resp) throws IOException {
        if (parameter == null || parameter.trim().isEmpty()) {
            resp.sendRedirect("/error.jsp?reason=invalid_integer_input");
            throw new InvalidParameterException("invalid_integer_input");
        }
        int result;
        try {
            result = Integer.parseInt(parameter);
            if (result < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            resp.sendRedirect("/error.jsp?reason=invalid_integer_input");
            throw new RuntimeException("couldn't parse integer");
        }
        return result;
    }

    private void addOptions(QuestionsDAO questionsDAO,  int question_id, HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String str = req.getParameter("numOptions");
        int numOptions = getIntParameter(str, resp);
        if(numOptions < 0){
            throw new NumberFormatException();
        }

        String[] correctOptions = req.getParameterValues("correct_options");
        Set<Integer> correctOptionSet = getCorrectOptionsSet(correctOptions);

        for (int i = 1; i <= numOptions; i++) {
            String optionText = req.getParameter("option" + i);
            if (optionText == null || optionText.trim().isEmpty()) {
                resp.sendRedirect("/error.jsp?reason=empty_option_text");
                throw new RuntimeException("empty option_text");
            }
            AnswerOption option = new AnswerOption(question_id, optionText, correctOptionSet.contains(i));
            questionsDAO.insertAnswerOptions(option);
        }
    }

    private Set<Integer> getCorrectOptionsSet(String[] correctOptions) {
        Set<Integer> res = new HashSet<>();
        if (correctOptions != null) {
            for (String correctOption : correctOptions) {
                try {
                    int opt = Integer.parseInt(correctOption);
                    res.add(opt);
                } catch (NumberFormatException e) {
                    // Ignore invalid correct option indices or log warning
                }
            }
        }
        return res;
    }
}
