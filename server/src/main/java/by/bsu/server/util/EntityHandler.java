package by.bsu.server.util;

import by.bsu.clientAgentChat.entity.Command;
import by.bsu.clientAgentChat.entity.Entity;
import by.bsu.clientAgentChat.entity.Message;
import by.bsu.server.loader.ServerLoader;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.*;

public class EntityHandler implements Runnable {

    private static final Logger logger = Logger.getLogger(EntityHandler.class);

    private Entity entity;

    public EntityHandler(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void run() {
        try {
            Gson gson = new Gson();
            InputStreamReader inputStreamReader = new InputStreamReader(entity.getSocket().getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Entity companion = entity.getCompanion();
            companion.sendMessage(helloMessage().toString());
            while (!entity.getSocket().isClosed()) {
                Message message = gson.fromJson(bufferedReader.readLine(), Message.class);
                companion.sendMessage(message.toString());
                switch (message.getCommand()) {
                    case LEAVE:
                        handleLeave();
                        return;
                    case EXIT:
                        handleExit();
                        return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Message helloMessage() {
        Message message = new Message(this.entity, "Server > Your companion " + entity.getName(), Command.INFO);
        return message;
    }

    private void handleExit() {
        Entity companion = this.entity.getCompanion();
        companion.setCompanion(null);
        ServerLoader.deleteEntity(entity.getId());
        ServerLoader.handle(companion);
    }

    private void handleLeave() {
        Entity companion = this.entity.getCompanion();
        companion.setCompanion(null);
        entity.setCompanion(null);
        ServerLoader.handle(companion);
        ServerLoader.handle(entity);
    }
}
