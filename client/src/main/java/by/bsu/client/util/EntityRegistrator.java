package by.bsu.client.util;

import by.bsu.clientAgentChat.entity.Entity;
import by.bsu.clientAgentChat.entity.Role;

public class EntityRegistrator {

    public Entity registerEntity(String str) {
        Role role = str.contains("agent") ? Role.AGENT : Role.CLIENT;
        String name = str.substring(str.indexOf(role.toString().toLowerCase()) + role.toString().length()).trim();
        return new Entity(name, role);
    }
}
