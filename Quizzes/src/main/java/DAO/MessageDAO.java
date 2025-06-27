package DAO;


import bean.Message.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageDAO {
    private final Connection connection;

    public MessageDAO(Connection connection){
        this.connection = connection;
    }

    /**
     * Adds message to database, generates message_id and changes
     * message_id of message.
     * @param message
     */
    private void sendSimpleMessage(Message message){
        String insert = "INSERT INTO Messages (from_user_id, to_user_id, type, content) values (?, ?, ?, ?);";
        try (PreparedStatement st = connection.prepareStatement(insert)){
            st.setInt(1, message.getSender_id());
            st.setInt(2, message.getReceiver_id());
            st.setString(3, message.getType().name());
            st.setString(4, message.getContent());

            st.executeUpdate();

            try(ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    message.setMessage_id(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to Send a NoteMessage", e);
        }
    }

    /**
     * Sends noteMessage using sendSimpleMessage
     * @param noteMessage
     */
    public void sendNote(NoteMessage noteMessage){
        sendSimpleMessage(noteMessage);
    }

    /**
     * Sends requestMessage using sendRequestMessage
     * @param requestMessage
     */
    public void sendFriendRequest(RequestMessage requestMessage){
        sendSimpleMessage(requestMessage);
    }

    /**
     * Sends challengeMessage, method is similar to sendSimpleMessage
     * except sql statement and the number of variables are different.
     * @param challengeMessage
     */
    public void sendChallenge(ChallengeMessage challengeMessage){
        String insert = "INSERT INTO Messages (from_user_id, to_user_id, type, content, quiz_id) values (?, ?, ?, ?, ?);";
        try (PreparedStatement st = connection.prepareStatement(insert)){
            st.setInt(1, challengeMessage.getSender_id());
            st.setInt(2, challengeMessage.getReceiver_id());
            st.setString(3, challengeMessage.getType().name());
            st.setString(4, challengeMessage.getContent());
            st.setInt(5, challengeMessage.getQuiz_id());

            st.executeUpdate();

            try(ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    challengeMessage.setMessage_id(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to Send a Challenge",e);
        }
    }

    /**
     *
     * @param rs ---> Gets us variables from a single row of a table;
     * @return new Message object using a single row of a sql table;
     */
    private Message getMessageByRow(ResultSet rs){
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
                    throw new RuntimeException("Unknown Message Type");
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to Get a Message by Row", e);
        }
    }

    /**
     * Searches for row where message_id is same as given parameter
     * and returns Message using getMessageByRow.
     * @param message_id
     * @return
     */
    public Message getMessageById(int message_id){
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

    /**
     * Given Prepared Statement, returns list of AbstractMessages
     * @param st
     * @return
     */
    private List<Message> messageQuery(PreparedStatement st){
        List<Message> result = new ArrayList<>();
        try(ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                result.add(getMessageByRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to Execute Query" ,e);
        }
        return result;
    }

    /**
     * Returns a list of messages which are received by
     * user_id and have MessageType, type.
     * @param user_id
     * @param type
     * @return
     */
    public List<Message> getReceivedTypeMessages(int user_id, MessageType type){
        String query = "SELECT * FROM Messages WHERE to_user_id = ? AND type = ?;";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, user_id);
            st.setString(2, type.name());

            return messageQuery(st);
        }catch (SQLException e){
            throw new RuntimeException("Failed to get Received Messages, Type: " + type.name(), e);
        }
    }

    /**
     * Similar to getReceivedTypeMessages method, only difference is that
     * this returns messages which are sent by user_id
     * @param user_id
     * @param type
     * @return
     */
    public List<Message> getSentTypeMessages(int user_id, MessageType type){
        String query = "SELECT * FROM Messages WHERE from_user_id = ? AND type = ?;";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, user_id);
            st.setString(2, type.name());

            return messageQuery(st);
        }catch (SQLException e){
            throw new RuntimeException("Failed to get Sent Messages, Type: " + type.name(), e);
        }
    }

    /**
     * Returns a list of every type of messages received by user_id.
     * @param user_id
     * @return
     */
    public List<Message> getInbox(int user_id){
        String query = "SELECT * FROM Messages WHERE to_user_id = ?";
        try(PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, user_id);

            return messageQuery(st);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to Get Inbox", e);
        }
    }

    /**
     * Marks the message with message_id as read.
     * @param message_id
     */
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