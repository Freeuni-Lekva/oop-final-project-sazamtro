package DAO;

import bean.FriendRequest;
import bean.FriendRequestStatus;
import bean.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestDAO {
    private final Connection connection;

    public FriendRequestDAO(Connection connection){
        this.connection = connection;
    }

    public boolean friendRequestExists(int sender_id, int receiver_id) throws SQLException{
        String query = "SELECT Count(*) From FriendRequests WHERE (from_user_id = ? AND to_user_id = ?) OR (from_user_id = ? AND to_user_id = ?)";
        PreparedStatement st = connection.prepareStatement(query);
        st.setInt(1, sender_id);
        st.setInt(2, receiver_id);
        st.setInt(3, receiver_id);
        st.setInt(4, sender_id);

        ResultSet rs = st.executeQuery();

        if(rs.next()){
            if(rs.getInt(1) > 0) return true;
        }
        return false;
    }

    public void sendFriendRequest(FriendRequest fr) throws SQLException{
        String statement = "INSERT INTO FriendRequests (from_user_id, to_user_id) VALUES (?, ?);";
        PreparedStatement st = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, fr.getSenderId());
        st.setInt(2, fr.getReceiverId());

        st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next()) {
            fr.setRequest_id(rs.getInt(1));
        }
    }

    private FriendRequest getRequestFromRow(ResultSet rs) throws SQLException{
        int request_id = rs.getInt("request_id");
        int sender_id = rs.getInt("from_user_id");
        int receiver_id = rs.getInt("to_user_id");
        FriendRequestStatus status = FriendRequestStatus.valueOf(rs.getString("status"));

        return new FriendRequest(request_id, sender_id, receiver_id, status);
    }

    public FriendRequest getRequestByID(int request_id) throws SQLException{
        String query = "SELECT * FROM FriendRequests where request_id = ?";

        PreparedStatement st = connection.prepareStatement(query);

        st.setInt(1, request_id);

        ResultSet rs = st.executeQuery();

        if(rs.next()){
            return getRequestFromRow(rs);
        }
        return null;
    }

    public List<FriendRequest> getPendingReceivedRequests(int user_id) throws SQLException{
        ArrayList<FriendRequest> requests = new ArrayList<>();

        String query = "SELECT * FROM FriendRequests where to_user_id = ? AND status like 'PENDING'";

        PreparedStatement st = connection.prepareStatement(query);

        st.setInt(1, user_id);

        ResultSet rs = st.executeQuery();

        while(rs.next()){
            requests.add(getRequestFromRow(rs));
        }
        return requests;
    }

    public void approveRequest(FriendRequest fr) throws SQLException{
        String statement = "UPDATE FriendRequests SET status = 'ACCEPTED' WHERE from_user_id = ? AND to_user_id = ?";
        PreparedStatement st = connection.prepareStatement(statement);
        st.setInt(1, fr.getSenderId());
        st.setInt(2, fr.getReceiverId());
        st.executeUpdate();
    }

    public void rejectRequest(FriendRequest fr) throws SQLException{
        String statement = "UPDATE FriendRequests SET status = 'REJECTED' WHERE from_user_id = ? AND to_user_id = ?";
        PreparedStatement st = connection.prepareStatement(statement);
            st.setInt(1, fr.getSenderId());
            st.setInt(2, fr.getReceiverId());
            st.executeUpdate();
    }

    public void removeFriendship(User friend1, User friend2) throws SQLException{
        String deletion = "DELETE FROM FriendRequests WHERE (from_user_id = ? AND to_user_id = ?) OR (from_user_id = ? AND to_user_id = ?)";
        PreparedStatement st = connection.prepareStatement(deletion);
        st.setInt(1, friend1.getUserId());
        st.setInt(2, friend2.getUserId());

        st.setInt(3, friend2.getUserId());
        st.setInt(4, friend1.getUserId());

        st.executeUpdate();
    }

    public List<User> getFriendsList(User user) throws SQLException{
        ArrayList<User> list = new ArrayList<>();

        String statement = "SELECT from_user_id, to_user_id FROM FriendRequests WHERE (from_user_id = ? OR to_user_id = ?) AND status = 'ACCEPTED'";
        UserDAO userDAO = new UserDAO(connection);
       PreparedStatement st = connection.prepareStatement(statement);
        st.setInt(1, user.getUserId());
        st.setInt(2, user.getUserId());

        ResultSet rs = st.executeQuery();

        while(rs.next()){
            User friend;
            if (rs.getInt("from_user_id") == user.getUserId()) {
                friend = userDAO.getUserById(rs.getInt("to_user_id"));
            } else {
                friend = userDAO.getUserById(rs.getInt("from_user_id"));
            }
            list.add(friend);
        }
        return list;
    }

    public boolean areFriends(User friend1, User friend2) throws SQLException{
        String query = "SELECT Count(*) FROM FriendRequests WHERE ((from_user_id = ? AND to_user_id = ?) OR (from_user_id = ? AND to_user_id = ?)) AND status like 'ACCEPTED'";
        PreparedStatement st = connection.prepareStatement(query);
        st.setInt(1, friend1.getUserId());
        st.setInt(2, friend2.getUserId());

        st.setInt(3, friend2.getUserId());
        st.setInt(4, friend1.getUserId());

        ResultSet rs = st.executeQuery();
        if(rs.next()){
            return rs.getInt(1) > 0;
        }
        return false;
    }
}
