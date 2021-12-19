package chatProject.server;

import chatProject.model.messages.Chatroom;
import chatProject.model.messages.Message;
import chatProject.model.user.Status;
import chatProject.model.user.UserAccount;
import chatProject.model.user.UserInfo;
import junit.framework.TestCase;
import org.junit.Test;


public class ChatServerTest extends TestCase {

    /**
     * Test asserting if a new chatroom is created
     * with the good values
     */
    @Test
    public void testNotifyNewChatroom() {

        //Assert
        ChatServer server = ChatServer.initEmptyChat(444,null);
        Chatroom<Object> newChatroom = new Chatroom<>("test",null,null);

        //Act
        final Chatroom result = server.notifyNewChatroom(newChatroom);

        //Assign
        assertEquals("The chatroom name is the one set in the constructor",
                newChatroom.getName(), result.getName());
    }

    /**
     * Test asserting if a new message is created with the good values
     * remark: add check client notifier method is called in other test
     */
    @Test
    public void testNotifyNewMessage() {

        //Assert
        ChatServer server = ChatServer.initEmptyChat(444, null);
        Message msg = new Message(1, null,"test");

        //Act
        final Message result = server.notifyNewMessage(1,msg);

        //Assign
        assertEquals("The message content is the one set in the constructor",
                msg.getMessage(), result.getMessage());
    }

    /**
     * Test asserting if the client method is called
     */
    @Test
    public void testNotifyUserChange_noDuplicatedUser() {

        //Assert
        ChatServer server = ChatServer.initEmptyChat(444, null);
        UserAccount userAccount = new UserAccount(1, "toto");
        UserInfo user = new UserInfo(userAccount, Status.ACTIVE);

        //Act
        final UserInfo result = server.notifyUserChange(user);

        //Assign
        assertEquals("The user name is the one set in the constructor",
                userAccount.getUsername(), result.getAccount().getUsername());
    }
}