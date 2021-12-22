package chatProject.model.messages;

import chatProject.model.FakeInstances;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    @Test
    public void getSender() {
        final Message<Object> message = Message.createMessage(0, FakeInstances.DUMMY_ACTIVE_USER, null);
        assertEquals("The message sender is not the one set in the constructor",
                FakeInstances.DUMMY_ACTIVE_USER, message.getSender());
    }

    @Test
    public void getId() {
        int id = 42;
        final Message<Object> message = Message.createMessage(id, null, null);

        assertEquals("The message ID is not the one set in the constructor",
                id, message.getId());
    }

    @Test
    public void getContent() {
        String content = "TEST";
        final Message<String> message = Message.createMessage(0, null, content);

        assertEquals("The message content is not the one set in the constructor",
                content, message.getMessage());
    }

    @Test
    public void getToString() {
        Message<String> message = FakeInstances.DUMMY_MESSAGE_1;
        String result = "Message{" +
                "id=" + message.getId() +
                ", sender=" + message.getSender() +
                ", content=" + message.getMessage() +
                '}';
        assertEquals(result, message.toString());
    }


}