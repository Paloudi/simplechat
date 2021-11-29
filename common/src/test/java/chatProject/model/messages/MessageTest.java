package chatProject.model.messages;

import org.junit.Assert;
import org.junit.Test;

public class MessageTest {

    @Test
    public void getId() {
        int id = 42;
        final Message<Object> message = Message.createMessage(id, null, null);

        Assert.assertEquals("The message ID is not the one set in the constructor",
                id, message.getId());
    }

    @Test
    public void getContent() {
        String content = "TEST";
        final Message<String> message = Message.createMessage(0, null, content);

        Assert.assertEquals("The message content is not the one set in the constructor",
                content, message.getMessage());
    }


}