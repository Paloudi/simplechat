package chatProject.model.messages;

import chatProject.model.user.Status;
import chatProject.model.user.UserAccount;
import chatProject.model.user.UserInfo;

import java.util.logging.Logger;


/**
 * A model for a message sent in the chat.
 * A message has a content (of a generic type T), a unique ID and a sender.
 * @param <T> the type of messages in the chat
 */
public class Message<T> {

    //region Private Properties

    /**
     * The ID of the message (unique).
     */
    private final int id;
    /**
     * The sender of the message.
     */
    private final MessageOwnerConcrete sender;
    /**
     * The content of the message.
     */
    private final T content;

    private static Logger logger = Logger.getLogger(Message.class.getName());

    //endregion

    /**
     * COnstructor
     * @param id id
     * @param sender send info
     * @param content message content
     */
    public Message(final int id, final UserInfo sender, final T content) {
        super();
        this.id = id;
        if ((null == sender)) {
            Message.logger.severe("NULL SENDER DETECTED IN MESSAGE");
            final UserAccount account = new UserAccount(9999, "NULL");
            final UserInfo userInfo = new UserInfo(account, Status.ACTIVE);
            final Status currentStatus = userInfo.getCurrentStatus();
            this.sender = new MessageOwnerConcrete(account, currentStatus);
        } else {
            final UserAccount account = sender.getAccount();
            final Status currentStatus = sender.getCurrentStatus();
            this.sender = new MessageOwnerConcrete(account, currentStatus);
        }
        this.content = content;
    }

    //region Public Getters

    public static <T> Message<T> createMessage(final int id, final UserInfo sender, final T content) {
        Message.logger.info(String.format("New message [ID: %d, sender: %s, content: %s]", id, sender, content));
        return new Message<T>(id, sender, content);
    }

    /**
     * Gets the ID of the message.
     * @return the message unique ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the sender of the message.
     * @return the user who sent the message
     */
    public UserInfo getSender() {
        return sender;
    }

    /**
     * Gets the content of the message.
     * @return the message content
     */
    public T getMessage() {
        return content;
    }

    //endregion

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender=" + sender +
                ", content=" + content +
                '}';
    }
}
