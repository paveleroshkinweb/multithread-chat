package by.bsu.server;

import by.bsu.clientAgentChat.reader.ApplicationPropertyReader;
import by.bsu.server.loader.ServerLoader;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

public class Main {

    private static final Logger logger = Logger.getLogger(ServerLoader.class);
    private static final String log4jPropertiesPath = ApplicationPropertyReader.getPropertyByName("log4jPropertiesPath");
    private static final int PORT = Integer.valueOf(ApplicationPropertyReader.getPropertyByName("port"));

    static {
        PropertyConfigurator.configure(log4jPropertiesPath);
    }

    public static void main(String[] args) {
        try(ServerLoader server = new ServerLoader(PORT)) {
            server.listen();
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
