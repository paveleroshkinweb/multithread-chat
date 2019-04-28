package by.bsu.clientAgentChat.entity;

import com.google.gson.Gson;

public class Message {

    private static final transient Gson gson = new Gson();

    private Entity fromWhom;

    private String content;

    private Command command;

    public Message() {

    }

    public Message(Entity fromWhom, String content, Command command) {
        this.fromWhom = fromWhom;
        this.content = content;
        this.command = command;
    }

    public Message(String content, Command command) {
        this.content = content;
        this.command = command;
    }

    public Entity getFromWhom() {
        return fromWhom;
    }

    public void setFromWhom(Entity fromWhom) {
        this.fromWhom = fromWhom;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
