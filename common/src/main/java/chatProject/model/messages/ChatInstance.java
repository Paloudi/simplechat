package chatProject.model.messages;

import chatProject.model.user.UserAccount;
import chatProject.model.user.UserInfo;

import java.time.LocalTime;
import java.util.*;

/**
 * The main class for the model of the chat.
 * Holds a list of {@link Chatroom} that each contain many {@link Message}.
 * Also holds the list of {@link UserInfo}.
 * @param <T> the type of messages in the chat
 */
public class ChatInstance<T> {

    //region Private Properties

    /**
     * The list of chatrooms in the chat.
     */
    private List<Chatroom<T>> chatrooms;

    /**
     * The list of users in the chat.
     * The key is the user information
     * The value is the last login time
     */
    private final Map<UserInfo, LocalTime> users;

    //endregion

    /**
     * Constructor
     * @param chatRooms chatRooms
     * @param users list of users
     */
    public ChatInstance(List<Chatroom<T>> chatRooms, Map<UserInfo, LocalTime> users) {
        chatrooms = chatRooms;
        this.users = users;
    }

    /**
     * Gets the list of chatrooms in the chat.
     * @return the list of {@link Chatroom} in the model
     */
    public List<Chatroom<T>> getCurrentChatrooms() {
        // return a safe read-only copy
        return Collections.unmodifiableList(chatrooms);
    }

    /**
     * Adds a new {@link Chatroom} in the chat.
     * @param newChatroom the chatroom to add
     * @return the ID of the new chatroom added
     */
    public Integer addChatroom(Chatroom<T> newChatroom) {
        if (null == chatrooms) {
            chatrooms = new LinkedList<>();
        }
        if(IsChatroomUnique(newChatroom.getName(), chatrooms)){
            chatrooms.add(newChatroom);
        }

        return chatrooms.indexOf(newChatroom);
    }

    /**
     * Adds a new {@link UserInfo} in the chat.
     * @param newUser the user to add
     */
    public boolean addUser(UserInfo newUser) {
        if (users.get(newUser) != null) {
            // already found in the model (same account and same status) - no update
            return false;
        }

        // may be already in the model but needs only to update the account
        final UserAccount newUserAccount = newUser.getAccount();
        final Optional<UserInfo> accountAlreadyPresent = users.keySet()
                .stream()
                .filter(user -> user.getAccount().equals(newUserAccount))
                .findAny();
        if (accountAlreadyPresent.isPresent()) {
            final UserInfo userInfo = accountAlreadyPresent.get();
            userInfo.setCurrentStatus(newUser.getCurrentStatus());
            users.replace(userInfo, LocalTime.now());
        } else {
            users.put(newUser, LocalTime.now());
        }
        return true;

    }

    /**
     * Gets the list of all registered users.
     * @return the collection of users in the chat.
     */
    public Map<UserInfo, LocalTime> getUsers() {
        return Collections.unmodifiableMap(this.users);
    }

    /**
     * Creates a new chat with no {@link Chatroom} no {@link Message} and no {@link UserInfo}.
     * @param <T> the type of messages in the chat
     * @return the new chat instance
     */
    public static <T> ChatInstance<T> initEmptyChat() {
        return new ChatInstance<T>(new ArrayList<Chatroom<T>>() { }, new HashMap<>(10));
    }

    /**
     * Determine if the chatroom name is unique
     * @param chatName new chat room name
     * @param chatrooms list of existing chat rooms
     * @return boolean
     */
    private boolean IsChatroomUnique(String chatName, List<Chatroom<T>> chatrooms){
        boolean isUnique = true;
        for (Chatroom<T> chatroom: chatrooms) {
            if(chatroom.getName().equalsIgnoreCase(chatName)){
                isUnique = false;
                break;
            }
        }
        return isUnique;
    }

}
