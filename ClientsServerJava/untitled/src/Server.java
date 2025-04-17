import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private PrintWriter logger; // For writing to the file

    public Server(int port) throws IOException {
        // Create the server socket
        serverSocket = new ServerSocket(port);

        // Create or append to "chat_log.txt" in the current folder.
        // Passing `true` to FileWriter means "append mode".
        FileWriter fileWriter = new FileWriter("chat_log.txt", true);

        // Auto-flush = true means PrintWriter will flush after each println().
        logger = new PrintWriter(fileWriter, true);
    }

    public void startServer() {
        System.out.println("Server started on port " + serverSocket.getLocalPort());

        while (true) {
            try {
                // Wait for a new client to connect
                Socket clientSocket = serverSocket.accept();
                System.out.println("A new client has connected: " + clientSocket.getInetAddress());

                // Spawn a new thread to handle each client
                ClientHandler handler = new ClientHandler(clientSocket, logger);
                Thread thread = new Thread(handler);
                thread.start();

            } catch (IOException e) {
                e.printStackTrace();
                break; // If you want the server to stop on exception, or remove this to keep going
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Start server on port 1234
            Server server = new Server(5000);
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
