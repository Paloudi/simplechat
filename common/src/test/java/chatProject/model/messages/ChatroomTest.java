package chatProject.model.messages;

import chatProject.model.FakeInstances;
import org.junit.Assert;
import org.junit.Test;

public class ChatroomTest {

    @Test
    public void getOwner() {
        Chatroom<Object> chatroom = new Chatroom<>("test", FakeInstances.DUMMY_ACTIVE_USER, null);
        Assert.assertEquals(FakeInstances.DUMMY_ACTIVE_USER, chatroom.getOwner());
    }

    @Test
    public void getToString() {
        Chatroom<String> chatroom = FakeInstances.DUMMY_CHATROOM_1;
        String result = chatroom.getName() + " (" + chatroom.getOwner().getAccount() + ')';
        Assert.assertEquals(result, chatroom.toString());
    }

    @Test
    public void getToStringNull() {
        Chatroom<String> chatroom = new Chatroom<>("test", null, null);
        Assert.assertEquals(chatroom.getName(), chatroom.toString());
    }
}
