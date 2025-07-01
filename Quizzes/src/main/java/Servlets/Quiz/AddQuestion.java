package Servlets.Quiz;

import DAO.QuestionsDAO;
import DAO.QuizDAO;
import bean.Questions.*;
import bean.Quiz;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AddQuestion extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String typeStr = req.getParameter("type");
        if (typeStr == null) {
            resp.sendRedirect("/error.jsp?reason=Invalid type");
            return;
        }
        Question question;
        try {
            question = getDefaultQuestionByType(QuestionType.valueOf(typeStr));
        } catch (Exception e) {
            resp.sendRedirect("/error.jsp");
            return;
        }
        if (question == null) {
            resp.sendRedirect("/error.jsp");
            return;
        }

        // Validate required parameters first
        String quizIdStr = req.getParameter("quiz_id");
        int quizId = getIntParameter(quizIdStr, resp);
        if(quizId < 0) return;
        question.setQuiz_id(quizId);


        String prompt = req.getParameter("prompt");
        if (prompt == null) {
            resp.sendRedirect("/error.jsp?reason=prompt_not_specified");
            return;
        }
        question.setQuestionText(prompt);


        if (question instanceof PictureResponse) {
            String image_url = req.getParameter("image_url");
            if (image_url == null) {
                resp.sendRedirect("/error.jsp?reason=image_url_not_specified");
                return;
            }
            ((PictureResponse) question).setImage_url(image_url);
        }

        // Get connection from context
        Connection con = (Connection) getServletContext().getAttribute("DBConnection");
        if (con == null) {
            throw new ServletException("Database connection not initialized.");
        }
        QuizDAO quizDAO = new QuizDAO(con);
        QuestionsDAO questionsDAO = new QuestionsDAO(con);


        try {
            Quiz quiz = quizDAO.getOneQuiz(question.getQuizId());
            if (quiz == null) {
                resp.sendRedirect("/error.jsp?reason=quiz_not_found");
                return;
            }
            boolean isRandom = quiz.checkIfRandom();

            if (!isRandom) {
                String posStr = req.getParameter("position");
                int position = getIntParameter(posStr, resp);
                if(position < 0) return;
                question.setPosition(position);
            }

            int question_id = questionsDAO.insertQuestion(question);
            question.setId(question_id);


            // Handle answers
            if (question instanceof ChoiceQuestions) {
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

        resp.sendRedirect("/add-question?quiz_id=" + question.getQuizId() +
                "&position=" + (question.getPosition() + 1));
    }

    private int getIntParameter(String parameter, HttpServletResponse resp) throws IOException {
        if (parameter == null || parameter.trim().isEmpty()) {
            resp.sendRedirect("/error.jsp?reason=invalid_integer_input");
            return -1;
        }
        int result;
        try {
            result = Integer.parseInt(parameter);
            if (result < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            resp.sendRedirect("/error.jsp?reason=invalid_integer_input");
            return -1;
        }
        return result;
    }

    private Question getDefaultQuestionByType(QuestionType type){
        switch (type){
            case MULTIPLE_CHOICE: return new MultipleChoice();
            case QUESTION_RESPONSE: return new QuestionResponse();
            case MULTI_SELECT: return new MultiSelect();
            case FILL_IN_THE_BLANK: return new FillInQuestion();
            case PICTURE_RESPONSE: return new PictureResponse();
            default: return null;
        }
    }
    private void addOptions(QuestionsDAO questionsDAO,  int question_id, HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String str = req.getParameter("numOptions");
        int numOptions = getIntParameter(str, resp);
        if(numOptions < 0){
            throw new NumberFormatException();
        }

        String[] correctOptions = req.getParameterValues("correct_options");
        Set<Integer> correctOptionSet = getCorrectOptionsSet(correctOptions);

        for (int i = 0; i < numOptions; i++) {
            String optionText = req.getParameter("option" + i);
            if (optionText == null || optionText.trim().isEmpty()) {
                resp.sendRedirect("/error.jsp?reason=empty_option_text");
                return;
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
