package DAO;

import bean.Achievement;

import java.sql.*;
import java.util.*;

public class AchievementsDAO {

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

    public List<Achievement> getUserAchievements(int user_id) throws SQLException {
        List<Achievement> result = new ArrayList<>();
        String sqlCommand = "SELECT * FROM UserAchievements WHERE user_id = ?";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
            st.setInt(1, user_id);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Achievement curr = new Achievement(rs.getInt("achievement_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("icon_url"));
                result.add(curr);
            }
            return result;
        }
    }
}
