package chatProject.server;

import com.google.gson.Gson;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main class for the server instance.
 */
public class Main {

    public static void main(String... args) {
        Logger logger = Logger.getAnonymousLogger();
        if (args.length != 2) {
            logger.log(Level.SEVERE, "Required arguments : <socket port (listener)> <web server port>");
            System.exit(1);
        }

        // the port of the socket opened in the ChatServer instance to send notifications to clients
        int socketPort = Integer.parseInt(args[0]);
        // the port to expose the web services of the ChatServerService
        int webServerPort = Integer.parseInt(args[1]);

        // init the Chat algo
        final Gson json = new Gson();
        // start the server implementation
        final ChatServer<String> server = ChatServer.initEmptyChat(socketPort, json);

        // start the web services
        new ChatServerService<>(server, json).serve(webServerPort);

    }
}
