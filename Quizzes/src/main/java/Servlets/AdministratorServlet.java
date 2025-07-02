package Servlets;

import DAO.AdministratorDAO;
import DAO.AnnouncementDAO;
import DAO.UserDAO;
import DAO.QuizDAO;
import bean.Announcement;
import bean.Quiz;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

@WebServlet("/AdministratorServlet")
public class AdministratorServlet extends HttpServlet {


    private User getAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("/login.jsp");
            return null;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/login.jsp");
            return null;
        }

        if (!user.checkIfAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "admins only.");
            return null;
        }

        return user;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User admin = getAdmin(request, response);
        if (admin == null) {
            return;
        }

        Connection connection = (Connection) request.getServletContext().getAttribute("DBConnection");
        AdministratorDAO administratorDAO = new AdministratorDAO(connection);

        String action = request.getParameter("action");

        if (action.equals("viewStatistics")) {
            try {
                int numUsers = administratorDAO.getNumUsers();
                int numAttempts = administratorDAO.getNumAttempts();

                request.setAttribute("numUsers", numUsers);
                request.setAttribute("numAttempts", numAttempts);

                request.getRequestDispatcher("/statistics-page.jsp").forward(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("message", "error: " + e.getMessage());
                request.getRequestDispatcher("/administrator-page.jsp").forward(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "unknown action.");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User admin = getAdmin(request, response);
        if (admin == null) {
            return;
        }

        Connection connection = (Connection) request.getServletContext().getAttribute("DBConnection");
        AdministratorDAO administratorDAO = new AdministratorDAO(connection);
        AnnouncementDAO announcementDAO = new AnnouncementDAO(connection);
        UserDAO userDAO = new UserDAO(connection);
        QuizDAO quizDAO = new QuizDAO(connection);

        HttpSession session = request.getSession();

        String action = request.getParameter("action");

        try {
            switch (action) {

                case "createAnnouncement":
                    doCreateAnnouncement(request, session, announcementDAO, admin);
                    break;

                case "removeUser":
                    doRemoveUser(request, session, administratorDAO, userDAO);
                    break;

                case "removeQuiz":
                    doRemoveQuiz(request, session, administratorDAO, quizDAO);
                    break;

                case "clearQuizHistory":
                    doClearQuizHistory(request, session, administratorDAO, quizDAO);
                    break;

                case "promote":
                    doPromote(request, session, administratorDAO, userDAO);
                    break;


                default:
                    session.setAttribute("message", "unknown action");
                    break;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("message", "error: " + e.getMessage());
        }

        response.sendRedirect("/administrator-page.jsp");
    }


    private void doCreateAnnouncement(HttpServletRequest request, HttpSession session, AnnouncementDAO announcementDAO, User admin) throws SQLException {
        String text = request.getParameter("text");

        if (text == null || text.trim().isEmpty()) {
            session.setAttribute("message", "no announcement text");
        } else {
            Announcement announcement = new Announcement(0, admin.getUserId(), admin.getUsername(), text, new Timestamp(System.currentTimeMillis()));
            announcementDAO.addAnnouncement(announcement);
            session.setAttribute("message", "successful announcement");
        }
    }

    private void doRemoveUser(HttpServletRequest request, HttpSession session, AdministratorDAO administratorDAO, UserDAO userDAO) throws SQLException {
        String userIdStr = request.getParameter("userId");

        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            session.setAttribute("message", "no user id provided");
        } else {
            int userId = Integer.parseInt(userIdStr);
            User user = userDAO.getUserById(userId);

            if (user != null) {
                administratorDAO.removeUser(userId);
                session.setAttribute("message",
                        "user " + user.getUsername() + " was removed");
            } else {
                session.setAttribute("message", "user not found");
            }
        }
    }

    private void doRemoveQuiz(HttpServletRequest request, HttpSession session, AdministratorDAO administratorDAO, QuizDAO quizDAO) throws SQLException {
        String quizIdStr = request.getParameter("quizId");

        if (quizIdStr == null || quizIdStr.trim().isEmpty()) {
            session.setAttribute("message", "no quiz ID provided");
        } else {
            int quizId = Integer.parseInt(quizIdStr);
            Quiz quiz = quizDAO.getOneQuiz(quizId);

            if (quiz != null) {
                administratorDAO.removeQuiz(quizId);
                session.setAttribute("message", "quiz " + quiz.getQuizTitle() + " was removed");
            } else {
                session.setAttribute("message", "quiz not found");
            }
        }
    }

    private void doClearQuizHistory(HttpServletRequest request, HttpSession session, AdministratorDAO administratorDAO, QuizDAO quizDAO) throws SQLException {
        String quizIdStr = request.getParameter("quizId");

        if (quizIdStr == null || quizIdStr.trim().isEmpty()) {
            session.setAttribute("message", "no quiz ID provided");
        } else {
            int quizId = Integer.parseInt(quizIdStr);
            Quiz quiz = quizDAO.getOneQuiz(quizId);

            if (quiz != null) {
                administratorDAO.clearQuizHistory(quizId);
                session.setAttribute("message", "quiz history cleared for: " + quiz.getQuizTitle());
            } else {
                session.setAttribute("message", "quiz not found");
            }
        }
    }

    private void doPromote(HttpServletRequest request, HttpSession session, AdministratorDAO administratorDAO, UserDAO userDAO) throws SQLException {
        String userIdStr = request.getParameter("userId");

        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            session.setAttribute("message", "no user ID provided.");
        } else {
            int userId = Integer.parseInt(userIdStr);
            User user = userDAO.getUserById(userId);

            if (user != null) {
                administratorDAO.promoteUserToAdmin(userId);
                session.setAttribute("message", "user " + user.getUsername() + " was promoted to admin");
            } else {
                session.setAttribute("message", "user not found");
            }
        }
    }
}
