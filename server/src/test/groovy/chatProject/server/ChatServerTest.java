package chatProject.server;

import chatProject.model.messages.Chatroom;
import chatProject.model.messages.Message;
import chatProject.model.user.Status;
import chatProject.model.user.UserAccount;
import chatProject.model.user.UserInfo;
import org.junit.Assert;
import org.junit.Test;


public class ChatServerTest {

    /**
     * Test asserting if a new chatroom is created
     * with the good values
     */
    @Test
    public void testNotifyNewChatroom() {
        //Assert
        ChatServer<Object> server = ChatServer.initEmptyChat(25000,null);
        Chatroom<Object> newChatroom = new Chatroom<>("test",null,null);

        //Act
        final Chatroom<Object> result = server.notifyNewChatroom(newChatroom);

        //Assign
        Assert.assertEquals("The chatroom name is the one set in the constructor",
                newChatroom.getName(), result.getName());
    }

    /**
     * Test asserting if a new message is created with the good values
     * remark: add check client notifier method is called in other test
     */
    @Test
    public void testNotifyNewMessage() {

        //Assert
        ChatServer<Object> server = ChatServer.initEmptyChat(25001, null);
        Message<Object> msg = new Message<>(1, null,"test");

        //Act
        final Message<Object> result = server.notifyNewMessage(1,msg);

        //Assign
        Assert.assertEquals("The message content is the one set in the constructor",
                msg.getMessage(), result.getMessage());
    }

    /**
     * Test asserting if the client method is called
     */
    @Test
    public void testNotifyUserChange_noDuplicatedUser() {
        //Assert
        ChatServer<Object> server = ChatServer.initEmptyChat(25002, null);
        UserAccount userAccount = new UserAccount(1, "toto");
        UserInfo user = new UserInfo(userAccount, Status.ACTIVE);

        //Act
        final UserInfo result = server.notifyUserChange(user);

        //Assign
        Assert.assertEquals("The user name is the one set in the constructor",
                userAccount.getUsername(), result.getAccount().getUsername());
    }
}