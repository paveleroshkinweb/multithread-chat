package by.bsu.client.loader;

import by.bsu.client.listener.ChatListener;
import by.bsu.client.util.MessageParser;
import by.bsu.client.validation.MessageValidator;
import by.bsu.clientAgentChat.entity.Command;
import by.bsu.clientAgentChat.entity.Entity;
import by.bsu.clientAgentChat.entity.Message;
import by.bsu.clientAgentChat.entity.Type;

import java.io.*;
import java.net.Socket;


public class ClientLoader implements Closeable {

    private Socket socket;

    private Entity entity;

    public ClientLoader(int port, String host, Entity entity) throws IOException {
        this.socket = new Socket(host, port);
        this.entity = entity;
        this.entity.setSocket(this.socket);
    }

    @Override
    public void close() throws IOException {
        if (this.socket != null) {
            this.socket.close();
        }
    }

    public int getPort() {
        return this.socket.getPort();
    }

    public void start() throws IOException {
        new ChatListener(this.entity);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            MessageValidator messageValidator = new MessageValidator();
            this.entity.sendMessage(Type.CONSOLE.toString());
            this.entity.sendMessage(this.entity.toString());
            while (!socket.isClosed()) {
                String line = bufferedReader.readLine();
                if (line.trim().length() == 0) {
                    System.out.println("Message length must be greater than 0!");
                    continue;
                }
                if (messageValidator.isValidMessage(line, entity.getRole())) {
                    Message message = MessageParser.parseMessage(line, entity);
                    this.entity.sendMessage(message.toString());
                    if (message.getCommand() == Command.EXIT) {
                        System.exit(1);
                    }
                } else {
                    System.out.println("This command incorrect!");
                }
            }
        }
    }

}
