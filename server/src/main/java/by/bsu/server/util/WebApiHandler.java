package by.bsu.server.util;

import by.bsu.clientAgentChat.entity.Entity;
import by.bsu.clientAgentChat.entity.Role;
import by.bsu.clientAgentChat.entity.WebCommand;
import by.bsu.server.loader.ServerLoader;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class WebApiHandler implements Runnable {

    private final Socket socket;

    private static Gson gson = new Gson();
    private static Map<WebCommand, String> datas;

    static {
        datas = new HashMap<WebCommand, String>() {
            {
                put(WebCommand.GET_ALL_AGENTS, getEntitiesByPredicate(entity -> entity.getRole() == Role.AGENT));

                put(WebCommand.GET_ALL_CLIENTS, getEntitiesByPredicate(entity -> entity.getRole() == Role.CLIENT));

                put(WebCommand.GET_ALL_ENTITIES, getAllEntities());

                put(WebCommand.GET_FREE_AGENTS, getEntitiesByPredicate(entity -> entity.getRole() == Role.AGENT
                                                                                    && entity.getCompanion() == null));

                put(WebCommand.GET_FREE_CLIENTS, getEntitiesByPredicate(entity -> entity.getRole() == Role.CLIENT
                                                                                    && entity.getCompanion() == null));

                put(WebCommand.GET_FREE_ENTITIES, getEntitiesByPredicate(entity -> entity.getCompanion() == null));

                put(WebCommand.GET_UNFREE_AGENTS, getEntitiesByPredicate(entity -> entity.getRole() == Role.AGENT
                                                                                    && entity.getCompanion() != null));

                put(WebCommand.GET_UNFREE_CLIENTS, getEntitiesByPredicate(entity -> entity.getRole() == Role.CLIENT
                                                                                    && entity.getCompanion() != null));

                put(WebCommand.GET_UNFREE_ENTITIES, getEntitiesByPredicate(entity -> entity.getCompanion() != null));
            }
        };
    }

    public WebApiHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            while (!socket.isClosed()) {
                WebCommand webCommand = gson.fromJson(bufferedReader.readLine(), WebCommand.class);
                String res;
                if (webCommand == WebCommand.GET_ENTITY_BY_ID) {
                    res = getEntityById(bufferedReader);
                } else {
                    res = datas.get(webCommand);
                }
                bufferedWriter.write(res + "\n");
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getEntitiesByPredicate(Predicate<Entity> predicate) {
        return gson.toJson(ServerLoader.filterEntity(predicate));
    }

    private static String getAllEntities() {
        return gson.toJson(ServerLoader.getEntities());
    }

    private static String getEntityById(BufferedReader bufferedReader) throws IOException {
        String id = bufferedReader.readLine();
        List<Entity> res = ServerLoader.filterEntity(entity -> entity.getId().equals(id));
        if (res.size() == 0) {
            return "";
        }
        return gson.toJson(res.get(0));
    }

}
