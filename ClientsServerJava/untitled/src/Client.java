import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;

            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();

                
                if (messageToSend.equalsIgnoreCase("disconnect")) {
                    bufferedWriter.write("disconnect");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }

                
                if (messageToSend.equalsIgnoreCase("exit")) {
                    bufferedWriter.write("exit");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break; 
                }
                
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null)  bufferedReader.close();
            if (bufferedWriter != null)  bufferedWriter.close();
            if (socket != null)         socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Type 'connect' (or 'reconnect') to join chat, or 'exit' to quit entirely.");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("connect") || command.equalsIgnoreCase("reconnect")) {

                System.out.println("Please enter your username for the group chat: ");
                String username = scanner.nextLine();

                try {
                    Socket socket = new Socket("localhost", 5000);
                    System.out.println("Connected to the server.");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Thread listenThread = new Thread(() -> {
                        try {
                            String response;
                            while ((response = reader.readLine()) != null) {
                                System.out.println("Server: " + response);
                            }
                        } catch (IOException e) {
                            
                        }
                    });
                    listenThread.start();
                    Client client = new Client(socket, username);
                    client.sendMessage();

                } catch (IOException e) {
                    System.out.println("Could not connect to server: " + e.getMessage());
                }

            } else if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting entire program...");
                break;
            }
        }

        System.out.println("Goodbye.");
    }
}
