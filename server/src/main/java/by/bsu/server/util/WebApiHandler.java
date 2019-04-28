package by.bsu.server.util;

import by.bsu.clientAgentChat.entity.Entity;
import by.bsu.clientAgentChat.entity.Role;
import by.bsu.server.loader.ServerLoader;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class WebApiHandler implements Runnable {

    private final Socket socket;

    public WebApiHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Gson gson = new Gson();
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            while (!socket.isClosed()) {
                String webCommand = bufferedReader.readLine();
                switch (webCommand) {
                    case "GET_ALL_AGENTS":
                        List<Entity> entities1 = ServerLoader.filterEntity(entity -> entity.getRole() == Role.AGENT);
                        bufferedWriter.write(gson.toJson(entities1) + "\n");
                        break;
                    case "GET_ALL_CLIENTS":
                        List<Entity> entities2 = ServerLoader.filterEntity(entity -> entity.getRole() == Role.CLIENT);
                        bufferedWriter.write(gson.toJson(entities2) + "\n");
                        break;
                    case "GET_ALL_ENTITIES":
                        List<Entity> entities3 = ServerLoader.getEntities();
                        bufferedWriter.write(gson.toJson(entities3) + "\n");
                        break;
                    case "GET_FREE_AGENTS":
                        List<Entity> entities4 = ServerLoader.filterEntity(entity -> entity.getRole() == Role.AGENT
                                                                            && entity.getCompanion() == null);
                        bufferedWriter.write(gson.toJson(entities4) + "\n");
                        break;
                    case "GET_FREE_CLIENTS":
                        List<Entity> entities5 = ServerLoader.filterEntity(entity -> entity.getRole() == Role.CLIENT
                                                                            && entity.getCompanion() == null);
                        bufferedWriter.write(gson.toJson(entities5) + "\n");
                        break;
                    case "GET_FREE_ENTITIES":
                        List<Entity> entities6 = ServerLoader.filterEntity(entity -> entity.getCompanion() == null);
                        bufferedWriter.write(gson.toJson(entities6) + "\n");
                        break;
                    case "GET_ENTITY_BY_ID":
                        String id = bufferedReader.readLine();
                        List<Entity> res = ServerLoader.filterEntity(entity -> entity.getId().equals(id));
                        if (res.size() == 0) {
                            bufferedWriter.write("\n");
                        }
                        else {
                            bufferedWriter.write(gson.toJson(res) + "\n");
                        }
                        break;
                    case "GET_UNFREE_AGENTS":
                        List<Entity> entities7 = ServerLoader.filterEntity(entity -> entity.getRole() == Role.AGENT
                                                                            && entity.getCompanion() != null);
                        bufferedWriter.write(gson.toJson(entities7) + "\n");
                        break;
                    case "GET_UNFREE_CLIENTS":
                        List<Entity> entities8 = ServerLoader.filterEntity(entity -> entity.getRole() == Role.CLIENT
                                                                            && entity.getCompanion() != null);
                        bufferedWriter.write(gson.toJson(entities8) + "\n");
                        break;
                    case "GET_UNFREE_ENTITIES":
                        List<Entity> entities9 = ServerLoader.filterEntity(entity -> entity.getCompanion() != null);
                        bufferedWriter.write(gson.toJson(entities9) + "\n");
                        break;
                }
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
