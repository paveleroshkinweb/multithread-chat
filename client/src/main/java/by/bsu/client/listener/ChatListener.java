package by.bsu.client.listener;

import by.bsu.clientAgentChat.entity.Command;
import by.bsu.clientAgentChat.entity.Entity;
import by.bsu.clientAgentChat.entity.Message;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

public class ChatListener extends Thread{

    private final Entity entity;

    public ChatListener(final Entity entity) {
        this.entity = entity;
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        try (InputStreamReader inputStreamReader = new InputStreamReader(entity.getSocket().getInputStream());
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                while (!entity.getSocket().isClosed()) {
                    Message message = gson.fromJson(bufferedReader.readLine(), Message.class);
                    if (message.getCommand() == Command.MESSAGE) {
                        System.out.println(message.getFromWhom().getName() + " > " + message.getContent());
                    }
                    else {
                        System.out.println(message.getContent());
                    }
                }
        } catch (IOException | NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}
