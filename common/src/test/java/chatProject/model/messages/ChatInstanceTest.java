package chatProject.model.messages;

import chatProject.model.FakeInstances;
import chatProject.model.user.Status;
import chatProject.model.user.UserInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatInstanceTest {

    @Test
    public void isChatroomUniqueWhenUnique() {
        ChatInstance<String> chatInstance = FakeInstances.DUMMY_CHAT_INSTANCE;
        Assert.assertTrue(chatInstance.isChatroomUnique(FakeInstances.DUMMY_CHATROOM_1.getName()));
    }

    @Test
    public void isChatroomUniqueWhenNotUnique() {
        List<Chatroom<String>> chatroomList = new ArrayList<>(2);
        chatroomList.add(FakeInstances.DUMMY_CHATROOM_2);
        chatroomList.add(FakeInstances.DUMMY_CHATROOM_2);
        chatroomList.add(FakeInstances.DUMMY_CHATROOM_2);
        ChatInstance<String> chatInstance = new ChatInstance<>(chatroomList, FakeInstances.DUMMY_USERS_MAP);
        Assert.assertFalse(chatInstance.isChatroomUnique(FakeInstances.DUMMY_CHATROOM_2.getName()));
    }

    @Test
    public void addUserWhenUserNull() {
        ChatInstance<String> chatInstance = FakeInstances.DUMMY_CHAT_INSTANCE;
        Assert.assertFalse(chatInstance.addUser(null));
    }

    @Test
    public void addUserWhenAlreadyAdded() {
        ChatInstance<String> chatInstance = FakeInstances.DUMMY_CHAT_INSTANCE;
        Assert.assertFalse(chatInstance.addUser(FakeInstances.DUMMY_ACTIVE_USER));
    }

    @Test
    public void addUserWhenEmpty() {
        ChatInstance<String> chatInstance = new ChatInstance<>(new ArrayList<>(0), new HashMap<>(1));
        Assert.assertTrue(chatInstance.addUser(FakeInstances.DUMMY_ACTIVE_USER));
    }

    @Test
    public void addSameUserWhenDifferentStatus() {
        ChatInstance<String> chatInstance = new ChatInstance<>(new ArrayList<>(0), new HashMap<>(1));
        UserInfo active_user = new UserInfo(FakeInstances.DUMMY_ACTIVE_USER.getAccount(), Status.INACTIVE);
        chatInstance.addUser(FakeInstances.DUMMY_ACTIVE_USER);
        Assert.assertTrue(chatInstance.addUser(active_user));
    }

    @Test
    public void addChatroomWhenNull() {
        ChatInstance<String> chatInstance = new ChatInstance<>(null, null);
        Assert.assertEquals(0, chatInstance.addChatroom(FakeInstances.DUMMY_CHATROOM_1));
    }
}

