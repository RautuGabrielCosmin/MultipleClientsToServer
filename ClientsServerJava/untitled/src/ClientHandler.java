import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private PrintWriter logger;  
    private String clientUsername;

    public ClientHandler(Socket socket, PrintWriter logger) {
        try {
            this.socket = socket;
            this.logger = logger;

            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);

            broadcastMessage("Server:" + clientUsername + " has entered the chat!");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        try {
            String msgFromClient;

            while (socket.isConnected() && (msgFromClient = bufferedReader.readLine()) != null) {

                if (msgFromClient.equalsIgnoreCase("disconnect")) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
                System.out.println("From " + clientUsername + " : " + msgFromClient);
                logger.println("From " + clientUsername + " : " + msgFromClient);

                bufferedWriter.write("Msg Received.");
                bufferedWriter.newLine();
                bufferedWriter.flush();

                if (msgFromClient.equalsIgnoreCase("exit")) {
                    System.out.println("Client " + socket.getInetAddress() + " has disconnected.");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Connection lost with " + socket.getInetAddress());
        } finally {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                // Avoid sending back to the same client who wrote it
                if (!clientHandler.clientUsername.equals(this.clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }


    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
    }


    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null)  bufferedReader.close();
            if (bufferedWriter != null)  bufferedWriter.close();
            if (socket != null)         socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
