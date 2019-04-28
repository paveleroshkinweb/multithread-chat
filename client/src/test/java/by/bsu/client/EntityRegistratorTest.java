package by.bsu.client;

import by.bsu.client.util.EntityRegistrator;
import by.bsu.clientAgentChat.entity.Entity;
import by.bsu.clientAgentChat.entity.Role;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EntityRegistratorTest {

    String line1 = "/register agent  valera";
    Entity entity1 = new Entity("valera", Role.AGENT);
    String line2 = "/register      client         petya";
    Entity entity2 = new Entity("petya", Role.CLIENT);

    EntityRegistrator entityRegistrator = new EntityRegistrator();

    @Test
    public void registerEntityTest() {
        assertEquals(entity1.getName(), entityRegistrator.registerEntity(line1).getName());
        assertEquals(entity1.getRole(), entityRegistrator.registerEntity(line1).getRole());
        assertEquals(entity2.getName(), entityRegistrator.registerEntity(line2).getName());
        assertEquals(entity2.getRole(), entityRegistrator.registerEntity(line2).getRole());
    }
}
