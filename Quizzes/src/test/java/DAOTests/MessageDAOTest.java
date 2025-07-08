package DAOTests;

import DAO.MessageDAO;
import bean.FriendRequest;
import bean.Message.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.*;

public class MessageDAOTest{
    private Connection connection;
    private MessageDAO messageDAO;


    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");

        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        messageDAO = new MessageDAO(connection);

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE Messages (" +
                            "message_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "from_user_id INT NOT NULL, " +
                            "to_user_id INT NOT NULL, " +
                            "type ENUM('NOTE', 'FRIEND_REQUEST', 'CHALLENGE') NOT NULL, " +
                            "content TEXT, " +
                            "quiz_id INT, " +
                            "sent_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                            "is_read BOOLEAN DEFAULT FALSE " +
                    ");");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Messages");
        }
        connection.close();
    }

    @Test
    void testSendMessage(){
        try {
            NoteMessage note = new NoteMessage(1, 2, "hi");
            messageDAO.sendNote(note);
            ChallengeMessage challenge = new ChallengeMessage(1, 2, "Beat This", 11);
            messageDAO.sendChallenge(challenge);
            RequestMessage request = new RequestMessage(1, 2, "irakli");
            messageDAO.sendFriendRequest(request);

            assertTrue(note.getMessageId() >= 0);
            assertTrue(challenge.getMessageId() >= 0);
            assertTrue(request.getMessageId() >= 0);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetMessageById(){
        try {
            NoteMessage note = new NoteMessage(1, 2, "hi");
            messageDAO.sendNote(note);
            ChallengeMessage challenge = new ChallengeMessage(1, 2, "Beat This", 11);
            messageDAO.sendChallenge(challenge);
            RequestMessage request = new RequestMessage(1, 2, "irakli");
            messageDAO.sendFriendRequest(request);

            Message m = (NoteMessage) messageDAO.getMessageById(note.getMessageId());
            assertEquals(note, m);

            m = (ChallengeMessage) messageDAO.getMessageById(challenge.getMessageId());
            assertEquals(challenge, m);

            m = (RequestMessage) messageDAO.getMessageById(request.getMessageId());
            assertEquals(request, m);
            m.setMessage_id(11);
            assertEquals(11, m.getMessageId());
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetReceivedTypeMessage(){
        try {
            NoteMessage note = new NoteMessage(1, 2, "hi");
            messageDAO.sendNote(note);
            NoteMessage note2 = new NoteMessage(1, 2, "how are you?");
            messageDAO.sendNote(note2);
            ChallengeMessage challenge = new ChallengeMessage(1, 2, "Beat This", 11);
            messageDAO.sendChallenge(challenge);
            RequestMessage request = new RequestMessage(1, 2, "irakli");
            messageDAO.sendFriendRequest(request);

            List<Message> list = messageDAO.getReceivedTypeMessages(2, MessageType.NOTE);
            assertEquals(2, list.size());
            assertEquals(note, list.get(0));
            assertEquals(note2, list.get(1));

            list = messageDAO.getReceivedTypeMessages(2, MessageType.CHALLENGE);
            assertEquals(1, list.size());
            assertEquals(challenge, list.get(0));

            list = messageDAO.getReceivedTypeMessages(2, MessageType.FRIEND_REQUEST);
            assertEquals(1, list.size());
            assertEquals(request, list.get(0));
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetSentTypeMessage(){
        try {
            NoteMessage note = new NoteMessage(1, 2, "hi");
            messageDAO.sendNote(note);
            NoteMessage note2 = new NoteMessage(1, 2, "how are you?");
            messageDAO.sendNote(note2);
            ChallengeMessage challenge = new ChallengeMessage(1, 2, "Beat This", 11);
            messageDAO.sendChallenge(challenge);
            RequestMessage request = new RequestMessage(1, 2, "irakli");
            messageDAO.sendFriendRequest(request);

            List<Message> list = messageDAO.getSentTypeMessages(1, MessageType.NOTE);
            assertEquals(2, list.size());
            assertEquals(note, list.get(0));
            assertEquals(note2, list.get(1));

            list = messageDAO.getSentTypeMessages(1, MessageType.CHALLENGE);
            assertEquals(1, list.size());
            assertEquals(challenge, list.get(0));

            list = messageDAO.getSentTypeMessages(1, MessageType.FRIEND_REQUEST);
            assertEquals(1, list.size());
            assertEquals(request, list.get(0));
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetConversation(){
        try {
            NoteMessage note1 = new NoteMessage(1, 2, "hi");
            messageDAO.sendNote(note1);
            NoteMessage note2 = new NoteMessage(2, 1, "how are you?");
            messageDAO.sendNote(note2);
            NoteMessage note3 = new NoteMessage(1, 2, "Fine, you?");
            messageDAO.sendNote(note3);
            NoteMessage note4 = new NoteMessage(2, 1, "Me too?");
            messageDAO.sendNote(note4);

            List<Message> list = messageDAO.getConversation(1, 2);
            assertEquals(4, list.size());
            assertEquals(note1, list.get(0));
            assertEquals(note2, list.get(1));
            assertEquals(note3, list.get(2));
            assertEquals(note4, list.get(3));
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void testMarkAsRead(){
        try {
            NoteMessage note1 = new NoteMessage(1, 2, "hi");
            messageDAO.sendNote(note1);
            NoteMessage note2 = new NoteMessage(2, 1, "how are you?");
            messageDAO.sendNote(note2);
            NoteMessage note3 = new NoteMessage(1, 2, "Fine, you?");
            messageDAO.sendNote(note3);
            NoteMessage note4 = new NoteMessage(2, 1, "Me too?");
            messageDAO.sendNote(note4);

            messageDAO.markAsRead(note1.getMessageId());
            assertTrue(messageDAO.getMessageById(note1.getMessageId()).isRead());
            messageDAO.markAsRead(note2.getMessageId());
            assertTrue(messageDAO.getMessageById(note2.getMessageId()).isRead());
            messageDAO.markAsRead(note3.getMessageId());
            assertTrue(messageDAO.getMessageById(note3.getMessageId()).isRead());
            messageDAO.markAsRead(note4.getMessageId());
            assertTrue(messageDAO.getMessageById(note4.getMessageId()).isRead());
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
