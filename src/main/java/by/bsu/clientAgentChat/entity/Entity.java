package by.bsu.clientAgentChat.entity;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.UUID;

public class Entity {

    private static final transient Gson gson = new Gson();

    private String id;

    private String name;

    private Role role;

    private transient Entity companion;

    private transient Socket socket;

    private transient BufferedWriter bufferedWriter;

    public Entity(String name, Role role) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.role = role;
    }

    public Entity(String name, Role role, Socket socket)  {
        this(name, role);
        this.socket = socket;
        initBufferedWriter();
    }

    private void initBufferedWriter() {
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        if (bufferedWriter == null) {
            initBufferedWriter();
        }
    }

    public Entity getCompanion() {
        return companion;
    }

    public void setCompanion(Entity companion) {
        this.companion = companion;
    }

    public void sendMessage(String message) {
        try {
            bufferedWriter.write(message + "\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
