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
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/editQuiz")
public class EditQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String quizIdStr = req.getParameter("quizId");
        if (quizIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing quizId parameter");
            return;
        }
        int quiz_id = Integer.parseInt(quizIdStr);
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        try{
            QuizDAO qDAO = new QuizDAO(connection);
            List<Question> quizQuestions = qDAO.getQuizQuestions(quiz_id);
            Map<Question, List<AnswerOption>> questionAnswerOptionMap = new HashMap<>();
            Map<Question, String> questionTextAnswerMap = new HashMap<>();
            QuestionsDAO questionsDAO = new QuestionsDAO(connection);
            for(Question curr : quizQuestions){
                if (curr.hasChoices()){
                    List<AnswerOption> answers = new ArrayList<>();
                    answers = questionsDAO.getOptions(curr.getId());
                    questionAnswerOptionMap.putIfAbsent(curr, answers);
                }
                else{
                    String answer = questionsDAO.getCorrectAnswerText(curr.getId());
                    if(answer != null){
                        questionTextAnswerMap.put(curr, answer);
                    }
                    else{
                        questionTextAnswerMap.put(curr, "");
                    }
                }
            }
            Quiz quiz = qDAO.getOneQuiz(quiz_id);
            req.setAttribute("quiz_id", quiz_id);
            req.setAttribute("quizTitle", quiz.getQuizTitle());
            req.setAttribute("isRandom", quiz.checkIfRandom());
            req.setAttribute("immediateCorrection", quiz.checkIfImmediate_correction());
            req.setAttribute("multiPage", quiz.checkIfMultipage());
            req.setAttribute("quizDescription", quiz.getQuizDescription());
            req.setAttribute("question_options", questionAnswerOptionMap);
            req.setAttribute("question_textAnswer", questionTextAnswerMap);

            RequestDispatcher rd = req.getRequestDispatcher("editQuiz.jsp");
//            System.out.println("Quiz title in doGet: " + quiz.getQuizTitle());
            rd.forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("All params:");
//        for (String paramName : req.getParameterMap().keySet()) {
//            System.out.println(paramName + ": " + req.getParameter(paramName));
//        }


        int quizId = Integer.parseInt(req.getParameter("quiz_id"));
        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");
        QuestionsDAO questionsDAO = new QuestionsDAO(connection);
        QuizDAO quizDAO = new QuizDAO(connection);

        try {
            // Update quiz metadata
            String title = req.getParameter("quizTitle");
            System.out.println("quizTitle from request: " + req.getParameter("quizTitle"));
            String description = req.getParameter("quizDescription");
            boolean random = req.getParameter("randomOrder") != null;
            boolean multiPage = req.getParameter("multiPage") != null;
            boolean immediateCorrection = req.getParameter("immediateCorrection") != null;
            quizDAO.updateQuizMeta(quizId, title, description, random, multiPage, immediateCorrection);

            Set<Question> questions = new LinkedHashSet<>(quizDAO.getQuizQuestions(quizId));
            Set<Integer> existingQuestionIds = new LinkedHashSet<>(questions.stream().map(Question::getId).collect(Collectors.toSet()));
            Set<Integer> submittedQuestionIds = new HashSet<>();

            // --- Handle existing questions ---
            Enumeration<String> paramNames = req.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String param = paramNames.nextElement();
                if (param.startsWith("questions[") && param.contains("]")) {
                    int qidStart = param.indexOf("[") + 1;
                    int qidEnd = param.indexOf("]");
                    int questionId = Integer.parseInt(param.substring(qidStart, qidEnd));
                    submittedQuestionIds.add(questionId);
                }
            }

            // Delete removed questions
            for (int oldId : existingQuestionIds) {
                if (!submittedQuestionIds.contains(oldId)) {
                    questionsDAO.deactivateQuestion(oldId);
                }
            }

            // Update or insert each submitted question
            for (int questionId : submittedQuestionIds) {
                String text = req.getParameter("questions[" + questionId + "][text]");
//                String typeStr = req.getParameter("questions[" + questionId + "][type]");
//                QuestionType type = QuestionType.valueOf(typeStr);
                questionsDAO.updateQuestionText(questionId, text);

                Question question = questionsDAO.getQuestionById(questionId);

                if (question.hasChoices()) {
                    Map<Integer, String> submittedOptions = new HashMap<>();
                    List<AnswerOption> oldOptions = questionsDAO.getOptions(questionId);
                    Set<Integer> existingOptionIds = new HashSet<>();
                    for (AnswerOption ao : oldOptions) existingOptionIds.add(ao.getId());

                    // First pass: collect all option texts
                    Map<String, String> optionTexts = new HashMap<>();
                    Map<String, Boolean> optionCorrectness = new HashMap<>();

                    for (String param : req.getParameterMap().keySet()) {
                        String prefix = "questions[" + questionId + "][options][";
                        if (param.startsWith(prefix)) {
                            if (param.endsWith("][text]")) {
                                int start = prefix.length();
                                int end = param.indexOf("][text]");
                                String idStr = param.substring(start, end);
                                String opText = req.getParameter(param);
                                optionTexts.put(idStr, opText);
                            } else if (param.endsWith("][correct]")) {
                                int start = prefix.length();
                                int end = param.indexOf("][correct]");
                                String idStr = param.substring(start, end);
                                optionCorrectness.put(idStr, true);
                            }
                        }
                    }

                    // Handle single correct answer for MULTIPLE_CHOICE
                    if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
                        String correct = req.getParameter("questions[" + questionId + "][correctOption]");
                        if (correct != null) {
                            for (String idStr : optionTexts.keySet()) {
                                optionCorrectness.put(idStr, idStr.equals(correct));
                            }
                        }
                    }

                    // Now insert/update all options
                    for (String idStr : optionTexts.keySet()) {
                        String opText = optionTexts.get(idStr);
                        boolean correct = optionCorrectness.getOrDefault(idStr, false);

                        if (idStr.startsWith("new_")) {
                            AnswerOption ans = new AnswerOption(questionId, opText, correct);
                            questionsDAO.insertAnswerOptions(ans);
                        } else {
                            int optionId = Integer.parseInt(idStr);
                            submittedOptions.put(optionId, opText);
                            AnswerOption ans = new AnswerOption(optionId, questionId, opText, correct);
                            questionsDAO.updateAnswerOption(ans);
                        }
                    }

                    // Delete removed options
                    for (int oldOptionId : existingOptionIds) {
                        if (!submittedOptions.containsKey(oldOptionId)) {
                            questionsDAO.deleteOption(oldOptionId);
                        }
                    }
                }
                else {
                    String answer = req.getParameter("questions[" + questionId + "][answer]");
                    if (answer == null || answer.trim().isEmpty()) {
                        questionsDAO.deleteCorrectAnswerTextByQuestionId(questionId);
                    } else {
                        questionsDAO.updateCorrectAnswerText(questionId, answer);
                    }

                    if (question.getQuestionType() == QuestionType.PICTURE_RESPONSE) {
                        String imageUrl = req.getParameter("questions[" + questionId + "][image]");
                        questionsDAO.updateImageUrl(questionId, imageUrl);
                    }
                }
            }

            // --- Handle new questions ---
            for (String param : req.getParameterMap().keySet()) {
                if (param.startsWith("newQuestions[") && param.endsWith("][type]")) {
                    int qIndex = Integer.parseInt(param.substring(13, param.indexOf("][type]")));
                    String typeStr = req.getParameter(param);
                    String prompt = req.getParameter("newQuestions[" + qIndex + "][text]");
                    QuestionType type = QuestionType.valueOf(typeStr);
//                    Question questio = questionsDAO.getQuestionById(qIndex); //not sure
                    String imageUrl = null;
                    if (type == QuestionType.PICTURE_RESPONSE) {
                        imageUrl = req.getParameter("newQuestions[" + qIndex + "][image]");
                    }
                    Question newQuestion = QuestionFactory.createQuestion(quizId, type, prompt, -1, imageUrl);
                    int newQid = questionsDAO.insertQuestion(newQuestion);

                    if (newQuestion.hasChoices()) {
                        for (int i = 0; ; i++) {
                            String optParam = "newQuestions[" + qIndex + "][options][" + i + "][text]";
                            if (!req.getParameterMap().containsKey(optParam)) break;

                            String text = req.getParameter(optParam);
                            boolean correct = false;
                            if (type == QuestionType.MULTIPLE_CHOICE) {
                                String correctOpt = req.getParameter("newQuestions[" + qIndex + "][correctOption]");
                                correct = String.valueOf(i).equals(correctOpt);
                            } else {
                                correct = req.getParameter("newQuestions[" + qIndex + "][options][" + i + "][correct]") != null;
                            }
                            AnswerOption ans = new AnswerOption(newQid, text, correct);
                            questionsDAO.insertAnswerOptions(ans);
                        }
                    } else {
                        String answer = req.getParameter("newQuestions[" + qIndex + "][answer]");
                        if (answer != null && !answer.trim().isEmpty()) {
                            questionsDAO.insertCorrectAnswerText(newQid, answer);
                        }
                    }
                }
            }
// Update question positions based on 'random' setting
            List<Integer> orderedQuestionIds = questionsDAO.getOrderedQuestionIds(quizId); // you’ll need this DAO function

            if (random) {
                for (int qid : orderedQuestionIds) {
                    questionsDAO.updateQuestionPosition(qid, -1);
                }
            } else {
                int pos = 1;
                for (int qid : orderedQuestionIds) {
                    questionsDAO.updateQuestionPosition(qid, pos++);
                }
            }

            req.setAttribute("quiz", quizDAO.getOneQuiz(quizId));
            req.getRequestDispatcher("/single_quiz_page.jsp").forward(req, resp);


        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}

