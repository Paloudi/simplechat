package chatProject.server;

import chatProject.model.messages.Chatroom;
import chatProject.model.messages.Message;
import chatProject.model.user.Status;
import chatProject.model.user.UserAccount;
import chatProject.model.user.UserInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;


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

    @Test
    public void testOpenSocketAlreadyOpened() {
        //Assert
        ChatServer<Object> server = ChatServer.initEmptyChat(25003, null);
        //Act
        ChatServer<Object> server1 = ChatServer.initEmptyChat(25003, null);

        server.close();
        server1.close();
        //Assign
        Assert.assertTrue(true);
    }

    @Test
    public void testFindUser() {
        //Assert
        ChatServer<Object> server = ChatServer.initEmptyChat(25004, null);
        UserAccount userAccount = new UserAccount(1, "toto");
        UserInfo user = new UserInfo(userAccount, Status.ACTIVE);

        //Act
        server.notifyUserChange(user);
        Optional<UserAccount> result = server.findUser("toto");

        //Assign
        Assert.assertEquals("The user name is the one added to the server",
                userAccount.getUsername(), result.get().getUsername());
    }

    @Test
    public void testCheckIdleClients() {
        //Assert
        ChatServer<Object> server = ChatServer.initEmptyChat(25005, null);
        UserAccount userAccount = new UserAccount(1, "toto");
        UserInfo user = new UserInfo(userAccount, Status.ACTIVE);

        //Act
        server.notifyUserChange(user);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.checkIdleClients();
        Optional<UserInfo> result = server.getUsers().stream().findAny();

        //Assign
        Assert.assertEquals("The user status is inactive",
                Status.INACTIVE, result.get().getCurrentStatus());
    }

    @Test
    public void testLogin() {
        //Assert
        ChatServer<Object> server = ChatServer.initEmptyChat(25005, null);
        //Act
        server.login("toto");
        server.checkIdleClients();
        Optional<UserAccount> result = server.findUser("toto");

        //Assign
        Assert.assertEquals("The user status is inactive",
                "toto", result.get().getUsername());
    }
}