package by.bsu.client;

import by.bsu.client.util.MessageParser;
import by.bsu.clientAgentChat.entity.Entity;
import by.bsu.clientAgentChat.entity.Message;
import by.bsu.clientAgentChat.entity.Role;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageParserTest {

    String line1 = " /exit ";
    String line2 = " /leave ";
    String line3 = "hello sdfs fsdfl";
    Entity client = new Entity("valera", Role.CLIENT);
    Entity agent = new Entity("petya", Role.AGENT);

    @Test
    public void parseMessageTest1() {
        Message msg = MessageParser.parseMessage(line1, client);
        assertEquals("valera exit the chat. You were added to the queue.", msg.getContent());
    }

    @Test
    public void parseMessageTest2() {
        Message msg = MessageParser.parseMessage(line2, client);
        assertEquals("valera leave the chat. You were added to the queue.", msg.getContent());
    }

    @Test
    public void parseMessageTest3() {
        Message msg = MessageParser.parseMessage(line3, agent);
        assertEquals("hello sdfs fsdfl", msg.getContent());
    }

}
