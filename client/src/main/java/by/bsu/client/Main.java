package by.bsu.client;

import by.bsu.client.loader.ClientLoader;
import by.bsu.client.util.ClientAssistant;
import by.bsu.client.util.EntityRegistrator;
import by.bsu.clientAgentChat.entity.Entity;
import by.bsu.clientAgentChat.reader.ApplicationPropertyReader;

import java.io.IOException;

public class Main {

    private static final String HOST = ApplicationPropertyReader.getPropertyByName("host");
    private static final int PORT = Integer.valueOf(ApplicationPropertyReader.getPropertyByName("port"));


    public static void main(String[] args) {
        ClientAssistant clientAssistant = new ClientAssistant();
        EntityRegistrator entityRegistrator = new EntityRegistrator();
        clientAssistant.showCommands();
        String line = clientAssistant.getRegistrationLine();
        Entity entity = entityRegistrator.registerEntity(line);
        try (ClientLoader clientLoader = new ClientLoader(PORT, HOST, entity)) {
            clientLoader.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
