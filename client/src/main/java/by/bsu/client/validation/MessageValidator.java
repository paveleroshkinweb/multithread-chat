package by.bsu.client.validation;

import by.bsu.clientAgentChat.entity.Role;

public class MessageValidator {

    public boolean isValidMessage(String line, Role role) {
        return !(line.trim().equalsIgnoreCase("/leave") && role == Role.AGENT);
    }
}