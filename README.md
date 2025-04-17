# Java Group Chat Application

A Java-based TCP group chat application demonstrating multi-client support, message broadcasting, and chat logging.

## Description

This repository implements a simple chat server and console client in Java:

- **Server**: Listens on a configurable port (default `5000`), accepts multiple client connections, broadcasts messages to all connected users, logs chat history to `chat_log.txt`, and handles graceful disconnections.
- **Client**: Connects to the server, prompts the user for a username, sends and receives messages in real-time, and allows users to disconnect or exit the application via commands.

## Key Features

- **Multi-Client Support**: Handles multiple clients simultaneously using a dedicated thread per connection.
- **Message Broadcasting**: Relays each user's messages to all other connected clients in real-time.
- **Chat Logging**: Appends all chat messages to `chat_log.txt` with timestamps for persistent record-keeping.
- **User Commands**: Supports `disconnect` to leave the group chat session and `exit` to terminate the client application.

## Prerequisites

- Java JDK 8 or higher
- (Optional) Maven or Gradle for project management

## Setup & Usage

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/java-group-chat.git
   cd java-group-chat
   ```

2. **Compile the source files**
   ```bash
   javac Server.java Client.java ClientHandler.java
   ```

3. **Start the server**
   ```bash
   java Server
   ```
   By default, the server listens on port `5000`. You can modify the port in the `main` method if needed.

4. **Launch one or more clients**
   ```bash
   java Client
   ```
   - Type `connect` (or `reconnect`) to join the chat.
   - Enter your desired username when prompted.
   - Chat live: type messages and press Enter to send.
   - Use `disconnect` to leave the chat or `exit` to quit the client.

## Project Structure

```
├── Server.java         # Main server implementation
├── ClientHandler.java  # Handles each client connection in a separate thread
├── Client.java         # Console-based chat client with user menu
├── chat_log.txt        # Appended chat history logs (auto-generated)
└── README.md           # Project documentation
```

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

