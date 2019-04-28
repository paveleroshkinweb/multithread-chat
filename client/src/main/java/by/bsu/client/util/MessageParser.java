package by.bsu.client.util;

import by.bsu.clientAgentChat.entity.Command;
import by.bsu.clientAgentChat.entity.Entity;
import by.bsu.clientAgentChat.entity.Message;

public class MessageParser {

    public static Message parseMessage(String line, Entity entity) {
        Message message = new Message();
        message.setFromWhom(entity);
        switch (line.trim()) {
            case "/exit":
                message.setCommand(Command.EXIT);
                message.setContent(entity.getName() + " exit the chat. You were added to the queue.");
                break;
            case "/leave":
                message.setCommand(Command.LEAVE);
                message.setContent(entity.getName() + " leave the chat. You were added to the queue.");
                break;
            default:
                message.setCommand(Command.MESSAGE);
                message.setContent(line.trim());
        }
        return message;
    }

}