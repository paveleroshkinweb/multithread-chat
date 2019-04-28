package by.bsu.client.validation;

import by.bsu.clientAgentChat.entity.Role;

public class MessageValidator {

    public boolean isValidMessage(String line, Role role) {
        if (line.trim().equalsIgnoreCase("/leave") && role == Role.AGENT) {
            return false;
        }
        return true;
    }
}