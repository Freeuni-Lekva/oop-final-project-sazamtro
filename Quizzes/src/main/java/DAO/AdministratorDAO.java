package DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministratorDAO {
    private final Connection connection;

    public AdministratorDAO(Connection connection) {
        this.connection = connection;
    }


    // remove user by its id
    // using 'ON DELETE CASCADE' in the SQL to automatically remove its records from all tables."
    public void removeUser(int userId) throws SQLException {
        String query = "DELETE FROM Users WHERE user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }


    // remove quiz by its id
    // using 'ON DELETE CASCADE' in the SQL to automatically remove its records from all tables."
    public void removeQuiz(int quizId) throws SQLException {
        String query = "DELETE FROM Quizzes WHERE quiz_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quizId);
            ps.executeUpdate();
        }
    }


    // clear quiz history
    // using 'ON DELETE CASCADE' in the SQL to automatically remove its records from all tables."
    public void clearQuizHistory(int quizId) throws SQLException {
        String query = "DELETE FROM QuizAttempts WHERE quiz_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quizId);
            ps.executeUpdate();
        }
    }


    // promote user to admin - updates Users.is_admin
    public void promoteUserToAdmin(int userId) throws SQLException {
        String query = "UPDATE Users SET is_admin = TRUE WHERE user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }


    public int getNumUsers() throws SQLException {
        String query = "SELECT COUNT(*) FROM Users";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }


    public int getNumAttempts() throws SQLException {
        String query = "SELECT COUNT(*) FROM QuizAttempts";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
