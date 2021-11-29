package chatProject.model;

import chatProject.AddMessageForm;
import chatProject.model.user.Status;
import chatProject.model.user.UserAccount;
import chatProject.model.user.UserInfo;
import org.junit.Assert;
import org.junit.Test;

public class AddMessageFormTest {

    @Test
    public void getId() {
        final int id = 42;
        final AddMessageForm<Object> messageForm = new AddMessageForm<>(id, null, null);
        final int chatroomId = messageForm.getChatroomId();
        Assert.assertEquals("The AddMessageForm id is not the one set in the constructor", id, chatroomId);

    }

    @Test
    public void getSender() {
        final UserAccount account = new UserAccount(0, "test");
        final UserInfo user = new UserInfo(account, Status.ACTIVE);
        final AddMessageForm<Object> messageForm = new AddMessageForm<>(0, user, null);
        final UserInfo sender = messageForm.getUser();
        Assert.assertEquals("The AddMessageForm sender is not the one set in the constructor", user, sender);
    }

    @Test
    public void getContent() {
        final String content = "test";
        final AddMessageForm<String> messageForm = new AddMessageForm(0, null, content);
        final String messageContent = messageForm.getContent();
        Assert.assertEquals("The AddMessageForm content is not the one set in the constructor", content, messageContent);
    }
}
