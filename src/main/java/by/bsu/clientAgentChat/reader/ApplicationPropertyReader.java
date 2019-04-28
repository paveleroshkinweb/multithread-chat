package by.bsu.clientAgentChat.reader;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApplicationPropertyReader {

    private static final Logger logger = Logger.getLogger(ApplicationPropertyReader.class);

    private static final String propertiesPath = "src/main/resources/application.properties";

    public static String getPropertyByName(String name) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(propertiesPath)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            logger.error(e);
        }
        return properties.getProperty(name);
    }
}
