package DAO;

import bean.Achievement;

import java.sql.*;
import java.util.*;

public class AchievementsDAO {

    // Constants for achievement IDs
    public static final int AMATEUR_AUTHOR_ID = 1;
    public static final int QUIZ_MACHINE_ID = 2;
    public static final int PRACTICE_MAKES_PERFECT_ID = 3;
    public static final int PERFECTIONIST_ID = 4;
    public static final int SOCIAL_BUTTERFLY_ID = 5;
    public static final int NIGHT_OWL_ID = 6;

    private Connection connection;

    public AchievementsDAO(Connection connection){
        this.connection = connection;
    }

    public List<Achievement> getAllAchievements() throws SQLException {
        List<Achievement> result = new ArrayList<>();
        String sqlCommand = "SELECT * FROM Achievements";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Achievement curr = new Achievement(rs.getInt("achievement_id"),
                                                   rs.getString("name"),
                                                   rs.getString("description"),
                                                   rs.getString("icon_url"));
                result.add(curr);
            }
        }
        return result;
    }

    public List<Achievement> getUserAchievements(int userId) throws SQLException {
        List<Achievement> result = new ArrayList<>();

        String sql = "SELECT a.* FROM UserAchievements ua " +
                "JOIN Achievements a ON a.achievement_id = ua.achievement_id " +
                "WHERE ua.user_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result.add(new Achievement(
                        rs.getInt("achievement_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("icon_url")
                ));
            }
        }
        return result;
    }

    public List<Achievement> getFriendsAchievements(int userId) throws SQLException {
        List<Achievement> result = new ArrayList<>();

        String sql = "SELECT u.username, a.achievement_id, a.name, a.description, a.icon_url " +
                "FROM UserAchievements ua " +
                "JOIN Achievements a ON a.achievement_id = ua.achievement_id " +
                "JOIN Users u ON u.user_id = ua.user_id " +
                "JOIN FriendRequests fr ON ((fr.from_user_id = ? AND fr.to_user_id = ua.user_id) " +
                "OR (fr.to_user_id = ? AND fr.from_user_id = ua.user_id)) " +
                "WHERE fr.status = 'ACCEPTED'";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result.add(new Achievement(
                        rs.getInt("achievement_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("icon_url"),
                        rs.getString("username")
                ));
            }
        }
        return result;
    }


    private boolean hasAchievement(int userId, int achievementId) throws SQLException {
        String sql = "SELECT 1 FROM UserAchievements WHERE user_id = ? AND achievement_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            st.setInt(2, achievementId);
            return st.executeQuery().next();
        }
    }

    private void awardAchievement(int userId, int achievementId) throws SQLException {
        if (!hasAchievement(userId, achievementId)) {
            String sql = "INSERT INTO UserAchievements (user_id, achievement_id) VALUES (?, ?)";
            try (PreparedStatement st = connection.prepareStatement(sql)) {
                st.setInt(1, userId);
                st.setInt(2, achievementId);
                st.executeUpdate();
            }
        }
    }

    public void checkAmateurAuthor(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Quizzes WHERE creator_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next() && rs.getInt(1) >= 1) {
                awardAchievement(userId, AMATEUR_AUTHOR_ID);
            }
        }
    }

    public void checkQuizMachine(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM QuizAttempts WHERE user_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next() && rs.getInt(1) >= 10) {
                awardAchievement(userId, QUIZ_MACHINE_ID);
            }
        }
    }

    public void checkPracticeMode(int userId) throws SQLException {
        String sql = "SELECT 1 FROM QuizAttempts WHERE user_id = ? AND is_practice = TRUE LIMIT 1";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            if (st.executeQuery().next()) {
                awardAchievement(userId, PRACTICE_MAKES_PERFECT_ID);
            }
        }
    }

    public void checkPerfectionist(int userId, int quizId) throws SQLException {
        String sql =
                "SELECT 1 " +
                        "FROM QuizAttempts qa " +
                        "JOIN ( " +
                        "    SELECT quiz_id, COUNT(*) AS total_questions " +
                        "    FROM Questions " +
                        "    WHERE quiz_id = ? " +
                        "    GROUP BY quiz_id " +
                        ") q ON qa.quiz_id = q.quiz_id " +
                        "WHERE qa.user_id = ? AND qa.score = q.total_questions " +
                        "LIMIT 1";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, quizId);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                awardAchievement(userId, PERFECTIONIST_ID);
            }
        }
    }

    public void checkSocialButterfly(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM FriendRequests " +
                "WHERE (from_user_id = ? OR to_user_id = ?) AND status = 'ACCEPTED'";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next() && rs.getInt(1) >= 5) {
                awardAchievement(userId, SOCIAL_BUTTERFLY_ID);
            }
        }
    }

    public void checkNightOwl(int userId, int attemptId) throws SQLException {
        String sql = "SELECT HOUR(taken_at) AS taken_hour FROM QuizAttempts WHERE attempt_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, attemptId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int hour = rs.getInt("taken_hour");
                if (hour >= 2 && hour <= 4) {
                    awardAchievement(userId, NIGHT_OWL_ID);
                }
            }
        }
    }
}



