package DAOTests;

import DAO.FriendRequestDAO;
import DAO.UserDAO;
import bean.FriendRequest;
import bean.FriendRequestStatus;
import bean.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.locks.Condition;

import static org.testng.AssertJUnit.*;

public class FriendRequestsDAOTest {
    private Connection connection;
    private FriendRequestDAO friendRequestDAO;
    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");

        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        friendRequestDAO = new FriendRequestDAO(connection);

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE FriendRequests (" +
                            "    request_id INT AUTO_INCREMENT PRIMARY KEY ," +
                            "    from_user_id INT NOT NULL,\n" +
                            "    to_user_id INT NOT NULL,\n" +
                            "    status ENUM('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL DEFAULT 'PENDING'" +
                            ");"
            );

            stmt.executeUpdate(
                    "CREATE TABLE Users (" +
                            "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "username VARCHAR(255) NOT NULL UNIQUE, " +
                            "password_hash VARCHAR(255) NOT NULL, " +
                            "profilePicture_url VARCHAR(255), " +
                            "is_admin BOOLEAN DEFAULT FALSE" +
                            ")"
            );
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS FriendRequests");
            stmt.executeUpdate("DROP TABLE IF EXISTS Users");
        }
        connection.close();
    }
    @Test
    void testSendFriendRequest(){
        try {
            FriendRequest fr1 = new FriendRequest(1, 2);
            FriendRequest fr2 = new FriendRequest(2, 3);

            friendRequestDAO.sendFriendRequest(fr1);
            friendRequestDAO.sendFriendRequest(fr2);

            assertTrue(fr1.getRequestId() >= 0);
            assertTrue(fr2.getRequestId() >= 0);

            assertEquals(fr1, friendRequestDAO.getRequestByID(fr1.getRequestId()));
            assertEquals(fr2, friendRequestDAO.getRequestByID(fr2.getRequestId()));

            assertEquals(FriendRequestStatus.PENDING, fr1.getStatus());
            assertEquals(FriendRequestStatus.PENDING, fr2.getStatus());
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFriendRequestExists(){
        try {
            FriendRequest fr1 = new FriendRequest(1, 2);
            FriendRequest fr2 = new FriendRequest(3, 4, FriendRequestStatus.PENDING);

            friendRequestDAO.sendFriendRequest(fr1);
            friendRequestDAO.sendFriendRequest(fr2);

            assertTrue(friendRequestDAO.friendRequestExists(1, 2));
            assertTrue(friendRequestDAO.friendRequestExists(2, 1));
            assertTrue(friendRequestDAO.friendRequestExists(3, 4));
            assertTrue(friendRequestDAO.friendRequestExists(4, 3));
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetPendingFriendRequests(){
        try {
            FriendRequest fr1 = new FriendRequest(1, 2);
            FriendRequest fr2 = new FriendRequest(3, 4, FriendRequestStatus.PENDING);
            FriendRequest fr3 = new FriendRequest(3, 2);

            friendRequestDAO.sendFriendRequest(fr1);
            friendRequestDAO.sendFriendRequest(fr2);
            friendRequestDAO.sendFriendRequest(fr3);

            List<FriendRequest> list = friendRequestDAO.getPendingReceivedRequests(2);
            assertEquals(2, list.size());
            assertEquals(fr1, list.get(0));
            assertEquals(fr3, list.get(1));

            list = friendRequestDAO.getPendingReceivedRequests(4);
            assertEquals(1, list.size());
            assertEquals(fr2, list.get(0));
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void testApproveRejectRequest(){
        try {
            UserDAO userDAO = new UserDAO(connection);

            User i = new User(-1, "irakli", "123", "", false);
            User t = new User(-1, "taso", "123", "", false);
            User g = new User(-1, "gvantsa", "123", "", false);
            User ts = new User(-1, "tsotne", "123", "", false);

            userDAO.addUser(i);
            userDAO.addUser(t);
            userDAO.addUser(g);
            userDAO.addUser(ts);

            FriendRequest fr1 = new FriendRequest(i.getUserId(), t.getUserId());
            FriendRequest fr2 = new FriendRequest(t.getUserId(), g.getUserId());
            FriendRequest fr3 = new FriendRequest(g.getUserId(), ts.getUserId());
            FriendRequest fr4 = new FriendRequest(ts.getUserId(), i.getUserId());
            FriendRequest fr5 = new FriendRequest(i.getUserId(), g.getUserId());

            friendRequestDAO.sendFriendRequest(fr1);
            friendRequestDAO.sendFriendRequest(fr2);
            friendRequestDAO.sendFriendRequest(fr3);
            friendRequestDAO.sendFriendRequest(fr4);
            friendRequestDAO.sendFriendRequest(fr5);

            friendRequestDAO.approveRequest(fr2);
            friendRequestDAO.approveRequest(fr3);
            friendRequestDAO.approveRequest(fr4);

            friendRequestDAO.rejectRequest(fr1);
            friendRequestDAO.rejectRequest(fr5);

            List<FriendRequest> request = friendRequestDAO.getPendingReceivedRequests(i.getUserId());
            assertTrue(request.isEmpty());
            request = friendRequestDAO.getPendingReceivedRequests(g.getUserId());
            assertTrue(request.isEmpty());
            request = friendRequestDAO.getPendingReceivedRequests(t.getUserId());
            assertTrue(request.isEmpty());
            request = friendRequestDAO.getPendingReceivedRequests(ts.getUserId());
            assertTrue(request.isEmpty());

            List<User> friendList = friendRequestDAO.getFriendsList(i);
            assertEquals(1, friendList.size());
            assertEquals(ts.getUserId(), friendList.get(0).getUserId());

            friendList = friendRequestDAO.getFriendsList(t);
            assertEquals(1, friendList.size());
            assertEquals(g.getUserId(), friendList.get(0).getUserId());

            friendList = friendRequestDAO.getFriendsList(g);
            assertEquals(2, friendList.size());
            assertEquals(t.getUserId(), friendList.get(0).getUserId());
            assertEquals(ts.getUserId(), friendList.get(1).getUserId());

            friendList = friendRequestDAO.getFriendsList(ts);
            assertEquals(2, friendList.size());
            assertEquals(g.getUserId(), friendList.get(0).getUserId());
            assertEquals(i.getUserId(), friendList.get(1).getUserId());

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRemoveFriend(){
        try {
            UserDAO userDAO = new UserDAO(connection);

            User i = new User(-1, "irakli", "123", "", false);
            User t = new User(-1, "taso", "123", "", false);
            User g = new User(-1, "gvantsa", "123", "", false);
            User ts = new User(-1, "tsotne", "123", "", false);

            userDAO.addUser(i);
            userDAO.addUser(t);
            userDAO.addUser(g);
            userDAO.addUser(ts);

            FriendRequest fr1 = new FriendRequest(i.getUserId(), t.getUserId());
            FriendRequest fr2 = new FriendRequest(t.getUserId(), g.getUserId());
            FriendRequest fr3 = new FriendRequest(g.getUserId(), ts.getUserId());
            FriendRequest fr4 = new FriendRequest(ts.getUserId(), i.getUserId());
            FriendRequest fr5 = new FriendRequest(i.getUserId(), g.getUserId());

            friendRequestDAO.sendFriendRequest(fr1);
            friendRequestDAO.sendFriendRequest(fr2);
            friendRequestDAO.sendFriendRequest(fr3);
            friendRequestDAO.sendFriendRequest(fr4);
            friendRequestDAO.sendFriendRequest(fr5);

            friendRequestDAO.approveRequest(fr2);
            friendRequestDAO.approveRequest(fr3);
            friendRequestDAO.approveRequest(fr4);

            friendRequestDAO.rejectRequest(fr1);
            friendRequestDAO.rejectRequest(fr5);

            assertTrue(friendRequestDAO.areFriends(g, ts));
            assertTrue(friendRequestDAO.areFriends(i, ts));

            friendRequestDAO.removeFriendship(g, ts);
            friendRequestDAO.removeFriendship(i, ts);


            assertFalse(friendRequestDAO.areFriends(g, ts));
            assertFalse(friendRequestDAO.areFriends(i, ts));

            List<User> friends = friendRequestDAO.getFriendsList(g);
            assertEquals(1, friends.size());
            assertEquals(t.getUserId(), friends.get(0).getUserId());

            friends = friendRequestDAO.getFriendsList(i);
            assertEquals(0, friends.size());

            friends = friendRequestDAO.getFriendsList(ts);
            assertEquals(0, friends.size());

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
