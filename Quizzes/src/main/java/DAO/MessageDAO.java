package DAO;


import bean.Message.*;

import java.sql.*;
import java.util.*;
import java.util.Date;

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
    private void sendSimpleMessage(Message message) throws SQLException{
        String insert = "INSERT INTO Messages (from_user_id, to_user_id, type, content) values (?, ?, ?, ?);";
        PreparedStatement st = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, message.getSender_id());
        st.setInt(2, message.getReceiver_id());
        st.setString(3, message.getMessageType().name());
        st.setString(4, message.getContent());
        st.executeUpdate();


        ResultSet rs = st.getGeneratedKeys();
        if (rs.next()) {
            message.setMessage_id(rs.getInt(1));
        }
    }

    /**
     * Sends noteMessage using sendSimpleMessage
     * @param noteMessage
     */
    public void sendNote(NoteMessage noteMessage) throws SQLException{
        sendSimpleMessage(noteMessage);
    }

    /**
     * Sends requestMessage using sendRequestMessage
     * @param requestMessage
     */
    public void sendFriendRequest(RequestMessage requestMessage) throws SQLException{
        sendSimpleMessage(requestMessage);
    }

    /**
     * Sends challengeMessage, method is similar to sendSimpleMessage
     * except sql statement and the number of variables are different.
     * @param challengeMessage
     */
    public void sendChallenge(ChallengeMessage challengeMessage) throws SQLException{
        String insert = "INSERT INTO Messages (from_user_id, to_user_id, type, content, quiz_id) values (?, ?, ?, ?, ?);";
        PreparedStatement st = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, challengeMessage.getSender_id());
        st.setInt(2, challengeMessage.getReceiver_id());
        st.setString(3, MessageType.CHALLENGE.toString());
        st.setString(4, challengeMessage.getContent());
        st.setInt(5, challengeMessage.getQuiz_id());

        st.executeUpdate();

        ResultSet rs = st.getGeneratedKeys();
        if (rs.next()) {
            challengeMessage.setMessage_id(rs.getInt(1));
        }
    }

    /**
     *
     * @param rs ---> Gets us variables from a single row of a table;
     * @return new Message object using a single row of a sql table;
     */
    private Message getMessageByRow(ResultSet rs) throws SQLException{
        int message_id = rs.getInt("message_id");
        int sender_id = rs.getInt("from_user_id");
        int receiver_id = rs.getInt("to_user_id");
        MessageType mt = MessageType.valueOf(rs.getString("type").toUpperCase());
        String content = rs.getString("content");
        Timestamp timestamp = rs.getTimestamp("sent_at");
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
    }

    /**
     * Searches for row where message_id is same as given parameter
     * and returns Message using getMessageByRow.
     * @param message_id
     * @return
     */
    public Message getMessageById(int message_id) throws SQLException{
        String query = "SELECT * FROM Messages Where message_id = ?;";
        PreparedStatement st = connection.prepareStatement(query);
        st.setInt(1, message_id);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return getMessageByRow(rs);
        }
        return null;
    }

    /**
     * Given Prepared Statement, returns list of AbstractMessages
     * @param st
     * @return
     */
    private List<Message> messageQuery(PreparedStatement st) throws SQLException{
        List<Message> result = new ArrayList<>();
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            result.add(getMessageByRow(rs));
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
    public List<Message> getReceivedTypeMessages(int user_id, MessageType type) throws SQLException{
        String query = "SELECT * FROM Messages WHERE to_user_id = ? AND type = ? AND is_read = FALSE;";
        PreparedStatement st = connection.prepareStatement(query);
        st.setInt(1, user_id);
        st.setString(2, type.name());

        return messageQuery(st);
    }

    /**
     * Similar to getReceivedTypeMessages method, only difference is that
     * this returns messages which are sent by user_id
     * @param user_id
     * @param type
     * @return
     */
    public List<Message> getSentTypeMessages(int user_id, MessageType type) throws SQLException{
        String query = "SELECT * FROM Messages WHERE from_user_id = ? AND type = ?;";
        PreparedStatement st = connection.prepareStatement(query);
        st.setInt(1, user_id);
        st.setString(2, type.name());
        return messageQuery(st);
    }

    /**
     * Returns a list of every note message between the user and the other user.
     * @param user_id
     * @return
     */
    public List<Message> getConversation(int user_id, int other_id) throws SQLException{
        String query = "SELECT * FROM Messages " +
                "WHERE type = 'NOTE' AND " +
                "((from_user_id = ? AND to_user_id = ?) OR " +
                "(from_user_id = ? AND to_user_id = ?))" +
                " ORDER BY sent_at ASC";
                ;
        PreparedStatement st = connection.prepareStatement(query);
        st.setInt(1, user_id);
        st.setInt(2, other_id);
        st.setInt(3, other_id);
        st.setInt(4, user_id);

        return messageQuery(st);
    }

    /**
     * Returns set of user_id's who have sent new messages, that
     * are unread.
     * @param userId
     * @return
     */
    public Set<Integer> getUnreadSenderIds(int userId) {
        String query = "SELECT DISTINCT from_user_id FROM Messages WHERE to_user_id = ? AND type like 'NOTE' AND is_read = FALSE";
        Set<Integer> ids = new HashSet<>();
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("from_user_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting unread senders", e);
        }
        return ids;
    }


    /**
     * Marks the message with message_id as read.
     * @param message_id
     */
    public void markAsRead(int message_id) throws SQLException{
        String sql = "UPDATE Messages " +
                "SET is_read = TRUE " +
                "WHERE message_id = ?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, message_id);
        st.executeUpdate();
    }
}