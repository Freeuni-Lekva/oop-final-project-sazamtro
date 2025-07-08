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

    public boolean friendRequestExists(int sender_id, int receiver_id){
        String query = "SELECT Count(*) From FriendRequests WHERE (from_user_id = ? AND to_user_id = ?) OR (from_user_id = ? AND to_user_id = ?)";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, sender_id);
            st.setInt(2, receiver_id);
            st.setInt(3, receiver_id);
            st.setInt(4, sender_id);

            ResultSet rs = st.executeQuery();

            if(rs.next()){
                if(rs.getInt(1) > 0) return true;
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to find friendRequest", e);
        }
        return false;
    }

    public void sendFriendRequest(FriendRequest fr){
        String statement = "INSERT INTO FriendRequests (from_user_id, to_user_id) VALUES (?, ?);";
        try(PreparedStatement st = connection.prepareStatement(statement)){
            st.setInt(1, fr.getSenderId());
            st.setInt(2, fr.getReceiverId());

            st.executeUpdate();

            try(ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    fr.setRequest_id(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed To Send Friend Request", e);
        }
    }

    private FriendRequest getRequestFromRow(ResultSet rs){
        try {
            int request_id = rs.getInt("request_id");
            int sender_id = rs.getInt("from_user_id");
            int receiver_id = rs.getInt("to_user_id");
            FriendRequestStatus status = FriendRequestStatus.valueOf(rs.getString("status"));

            return new FriendRequest(request_id, sender_id, receiver_id, status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FriendRequest> getPendingReceivedRequests(int user_id){
        ArrayList<FriendRequest> requests = new ArrayList<>();

        String query = "SELECT from_user_id FROM FriendRequests where to_user_id = ? AND status like 'PENDING'";

        try(PreparedStatement st = connection.prepareStatement(query)){

            st.setInt(1, user_id);

            ResultSet rs = st.executeQuery();

            while(rs.next()){
                requests.add(getRequestFromRow(rs));
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed To Get Received Requests", e);
        }

        return requests;
    }

    public void approveRequest(FriendRequest fr){
        String statement = "UPDATE FriendRequests SET status = 'APPROVED' WHERE from_user_id = ? AND to_user_id = ?";
        try(PreparedStatement st = connection.prepareStatement(statement)){
            st.setInt(1, fr.getSenderId());
            st.setInt(2, fr.getReceiverId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed To Approve Request", e);
        }
    }

    public void rejectRequest(FriendRequest fr){
        String statement = "UPDATE FriendRequests SET status = 'REJECTED' WHERE from_user_id = ? AND to_user_id = ?";
        try(PreparedStatement st = connection.prepareStatement(statement)){
            st.setInt(1, fr.getSenderId());
            st.setInt(2, fr.getReceiverId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed To Approve Request", e);
        }
    }

    public void removeFriendship(User friend1, User friend2){
        String deletion = "DELETE FROM FriendRequests WHERE (from_user_id = ? AND to_user_id = ?) OR (from_user_id = ? AND to_user_id = ?)";
        try(PreparedStatement st = connection.prepareStatement(deletion)){
            st.setInt(1, friend1.getUserId());
            st.setInt(2, friend2.getUserId());

            st.setInt(3, friend2.getUserId());
            st.setInt(4, friend1.getUserId());

            st.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Failed To Remove Friendship", e);
        }
    }

    public List<User> getFriendsList(User user){
        ArrayList<User> list = new ArrayList<>();

        String statement = "SELECT from_user_id, to_user_id FROM FriendRequests WHERE (from_user_id = ? OR to_user_id = ?) AND status = 'ACCEPTED'";
        UserDAO userDAO = new UserDAO(connection);
        try(PreparedStatement st = connection.prepareStatement(statement)){
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
        }catch (SQLException e){
            throw new RuntimeException("Failed To Get FriendsList", e);
        }
        return list;
    }

    public boolean areFriends(User friend1, User friend2){
        String query = "SELECT Count(*) FROM FriendRequests WHERE ((from_user_id = ? AND to_user_id = ?) OR (from_user_id = ? AND to_user_id = ?)) AND status like 'ACCEPTED'";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, friend1.getUserId());
            st.setInt(2, friend2.getUserId());

            st.setInt(3, friend2.getUserId());
            st.setInt(4, friend1.getUserId());

            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getInt(1) > 0;
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed To Remove Friendship", e);
        }
        return false;
    }
}
