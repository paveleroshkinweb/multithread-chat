package by.bsu.client.util;

import by.bsu.client.validation.CommandValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientAssistant {

    public void showCommands() {
        System.out.println("Commands :");
        System.out.println("1)To register, enter /register agent or client 'your name'");
        System.out.println("2)To end a chat, enter /leave (available only for the client)");
        System.out.println("3)To exit the system, enter /exit ");
        System.out.println("The name of user must be greater than 3!");
        System.out.println("For start chat the client should write something and if there is a free agent then they connect.");
    }

    public String getRegistrationLine() {
        CommandValidator commandValidator = new CommandValidator();
        String line = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (!commandValidator.isValidRegisterString(line = bufferedReader.readLine())) {
                System.out.println("Please, enter valid data!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

}
