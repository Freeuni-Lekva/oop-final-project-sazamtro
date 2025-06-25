package DAO;


import bean.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageDAO {
    private final Connection connection;

    public MessageDAO(Connection connection){
        this.connection = connection;
    }

    private void sendSimpleMessage(int sender_id, int receiver_id, MessageType type, String content){
        String insert = "INSERT INTO Messages (from_user_id, to_user_id, type, content) values (?, ?, ?, ?);";
        try (PreparedStatement st = connection.prepareStatement(insert)){
            st.setInt(1, sender_id);
            st.setInt(2, receiver_id);
            st.setString(3, type.toString());
            st.setString(4, content);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to Send a NoteMessage", e);
        }
    }

    public void sendNote(int sender_id, int receiver_id, MessageType type, String content){
        sendSimpleMessage(sender_id, receiver_id, type, content);
    }

    public void sendFriendRequest(int sender_id, int receiver_id, MessageType type, String content){
        sendSimpleMessage(sender_id, receiver_id, type, content);
    }

    public void sendChallenge(int sender_id, int receiver_id, MessageType type, String content, int quiz_id){
        String insert = "INSERT INTO Messages (from_user_id, to_user_id, type, content, quiz_id) values (?, ?, ?, ?, ?);";
        try (PreparedStatement st = connection.prepareStatement(insert)){
            st.setInt(1, sender_id);
            st.setInt(2, receiver_id);
            st.setString(3, type.toString());
            st.setString(4, content);
            st.setInt(5, quiz_id);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to Send a Challenge",e);
        }
    }

    private AbstractMessage getMessageByRow(ResultSet rs){
        try{
            int message_id = rs.getInt("message_id");
            int sender_id = rs.getInt("from_user_id");
            int receiver_id = rs.getInt("to_user_id");
            MessageType mt = MessageType.valueOf(rs.getString("type").toUpperCase());
            String content = rs.getString("content");
            Date timestamp = rs.getTimestamp("sent_at");
            boolean is_read = rs.getBoolean("is_read");


            switch (mt){
                case NOTE:
                    return new NoteMessage(message_id, sender_id, receiver_id, content, timestamp, is_read);
                case FRIEND_REQUEST:
                    return new RequestMessage(message_id, sender_id, receiver_id, content, timestamp, is_read);
                case CHALLENGE:
                    int quiz_id = rs.getInt("quiz_id");
                    return new ChallengeMessage(message_id, sender_id, receiver_id, content, quiz_id, timestamp, is_read);
                default:
                    return null;
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to Get a Message by Row", e);
        }
    }

    public AbstractMessage getMessageById(int message_id){
        String query = "SELECT * FROM Messages Where message_id = ?;";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, message_id);

            try(ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return getMessageByRow(rs);
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to Get Message By Id", e);
        }
        return null;
    }

    public List<AbstractMessage> getInbox(int user_id){
        List<AbstractMessage> result = new ArrayList<>();
        String query = "SELECT * FROM Messages WHERE from_user_id = ? OR to_user_id = ?";
        try(PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, user_id);
            st.setInt(2, user_id);

            try(ResultSet rs = st.executeQuery(query)) {
                while (rs.next()) {
                    result.add(getMessageByRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to Get Inbox", e);
        }
        return result;
    }

    public void markAsRead(int message_id){
        String sql = "UPDATE Messages " +
                     "SET is_read = TRUE " +
                     "WHERE message_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)){
            st.setInt(1, message_id);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to Mark a Message as Read",e);
        }
    }
}
