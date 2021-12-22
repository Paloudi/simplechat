package chatProject.server;

import chatProject.algo.ChatroomAlgo;
import chatProject.algo.MessageAlgo;
import chatProject.algo.UserAlgo;
import chatProject.model.messages.ChatInstance;
import chatProject.model.messages.Chatroom;
import chatProject.model.messages.Message;
import chatProject.model.user.UserInfo;
import chatProject.model.user.Status;
import chatProject.model.user.UserAccount;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class implements the server side of the Chat.
 * To be generated in the {@link Main} instance using {@link #initEmptyChat(int, Gson)}.
 * @param <T> the type of messages to use (probably String)
 */
public class ChatServer<T> implements UserAlgo, ChatroomAlgo<T>, MessageAlgo<T>, AutoCloseable {

    //region Private properties

    /**
     * The model for the chat.
     */
    private final ChatInstance<T> chatInstance;

    /**
     * The current list of connected clients.
     */
    private final Collection<ClientNotifierInterface<T>> clientNotifiers;

    /**
     * A shared Json (de)serializer to improve performance.
     */
    private final Gson json;

    /**
     * The thread that holds a socket to send notifications of new messages and new chatrooms to clients.
     * Must be interrupted on close.
     */
    private Thread socketThread = null;

    /**
     * The thread that checks for idle clients.
     * Must be interrupted on close.
     */
    private Thread checkIdleClients = null;

    /**
     * Tracks if the server is running or not
     */
    private boolean running;

    //endregion

    //region Constructor

    /**
     * Chat Server Constructor
     * @param chatInstance chat instance
     * @param clientNotifiers client notifiers
     * @param json json
     */
    public ChatServer(ChatInstance<T> chatInstance,
                      Collection<ClientNotifierInterface<T>> clientNotifiers,
                      Gson json) {
        this.chatInstance = chatInstance;
        this.clientNotifiers = clientNotifiers;
        this.json = json;
        this.running = false;
    }

    //endregion

    //region Chat initialization

    /**
     * The entry point to generate an instance of this class using an empty {@link ChatInstance} model.
     * @param socketPort the port of the socket to open on this server.
     * @param json the Json (de)serializer to use
     * @param <T> the type of messages to use
     * @return a new instance of this class to use as a server
     * @throws IOException not sure when ?
     */
    public static <T> ChatServer<T> initEmptyChat(int socketPort, Gson json) {

        // instantiate a new instance of this class with an empty model.
        final ChatServer<T> server = new ChatServer<>(
                ChatInstance.initEmptyChat(),
                new HashSet<>(),
                json);

        // open a dedicated thread to manage the socket for notifications.
        server.socketThread = new Thread(() -> {
            try {
                server.openSocket(socketPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        server.socketThread.start();

        server.checkIdleClients();

        return server;
    }

    /**
     * Opens a socket on the given port to notify clients of new chatrooms and messages.
     * @param port the port to use
     * @throws IOException if the socket cannot be opened
     */
    public void openSocket(int port) throws IOException {

        // open the socket in a try-with-resources (auto close the socket on exit)
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            this.running = true;
            // loop forever to accept all new clients
            while(this.running) {

                // Socket.accept() is blocking - wait for a new client
                final Socket client = serverSocket.accept();
                // a new client has been found
                clientNotifiers.add(
                        // init the notifier to send notifs for this new client
                        ClientNotifier.init(client, json)
                );
            }
        }
    }

    /**
     * Checks for idle clients (no ping for a long time).
     * Updates the status of these users accordingly.
     */
    public void checkIdleClients() {
        this.checkIdleClients = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(100); // check every 100ms
                    // avoid removing instances during the iteration - store members to update
                    final Collection<UserInfo> usersToUpdate = new HashSet<>();
                    chatInstance.getUsers().forEach( (user, time) -> {
                                if (user.getCurrentStatus() == Status.ACTIVE
                                        && ChronoUnit.SECONDS.between(time, LocalDateTime.now()) > 2) {
                                    user.setCurrentStatus(Status.INACTIVE);
                                    usersToUpdate.add(user);
                                }
                            }
                    );
                    usersToUpdate.forEach(this::notifyUserChange);
                } catch (InterruptedException e) {
                    // interrupted
                    break;
                }
            }
        }
        );

        this.checkIdleClients.start();

    }

    @Override
    public void close() {

        /* 1. we should end infinite loops before closing... */
        this.running = false;
        // 2. terminate all threads :

        // cleanly close the check for idle clients
        checkIdleClients.interrupt();
        // cleanly close the socket on exit
        socketThread.interrupt();
    }

    //endregion

    //region User part

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<UserInfo> getUsers() {
        return chatInstance.getUsers().keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserInfo login(String userName) {
        int usersCount = getUsers().size();
        final UserInfo user = new UserInfo(
                findUser(userName).orElse(new UserAccount(usersCount, userName)),
                Status.ACTIVE // user just logged in - status is active
        );
        notifyUserChange(user);

        return user;
    }

    /**
     * Finds a user in the model given its username (if the user is already registered).
     * @param userName the username to find
     * @return an optional {@link UserAccount} with the user model only if already in the model
     */
    public Optional<UserAccount> findUser(String userName) {
        return chatInstance.getUsers().keySet().stream()
                .map(UserInfo::getAccount)
                .filter(account -> account.getUsername().equals(userName))
                .findAny();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserInfo notifyUserChange(UserInfo user) {
        // notify a user change only if the user did change
        if (chatInstance.addUser(user)) {
            // notify all clients
            clientNotifiers.forEach(
                    client -> client.notifyUserChange(user)
            );
        }
        return user;
    }

    //endregion

    //region Chatroom part

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getCurrentChatroomNames() {
        return chatInstance
                // retrieve all chatrooms
                .getCurrentChatrooms()
                .stream()
                // get the chatroom name(s) from the model(s) instance(s)
                .map(Chatroom::getName)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Chatroom<T> getChatroom(int chatroomId) {
        return chatInstance.getCurrentChatrooms().get(chatroomId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long addChatroom(String chatroomName, UserInfo owner) {

        // instantiate the chatroom
        final Chatroom<T> newChatroom = new Chatroom<>(chatroomName, owner, new ArrayList<>());

        // add it in the model
        final long newChatroomId = chatInstance.addChatroom(newChatroom);

        if(newChatroomId != -1){
            /* maybe I should notify clients about the new chatroom ?? */
            notifyNewChatroom(newChatroom);
        }

        return newChatroomId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Chatroom<T> notifyNewChatroom(Chatroom<T> newChatroom) {
        if (clientNotifiers != null) {
            clientNotifiers.forEach(
                    client -> client.notifyNewChatroom(newChatroom)
            );
        }
        return newChatroom;
    }

    //endregion

    //region Messages Part

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message<T>> getChatroomMessages(int chatroomId) {
        Optional<Chatroom<T>> optional = Optional.ofNullable(getChatroom(chatroomId));
        return optional.map(Chatroom::getCurrentMessages).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message<T> addMessage(int chatroomId, UserInfo user, T content) {
        Message<T> msg = this.getChatroom(chatroomId).addMessage(user, content);

        //Notify users
        this.notifyNewMessage(chatroomId, msg);

        // return new created message
        return  msg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message<T> notifyNewMessage(int chatroomId, Message<T> newMessage) {
        if (null != this.clientNotifiers) {
            this.clientNotifiers.forEach(
                    client -> client.notifyNewMessage(chatroomId, newMessage)
            );
        }
        return newMessage;
    }

    //endregion
}
