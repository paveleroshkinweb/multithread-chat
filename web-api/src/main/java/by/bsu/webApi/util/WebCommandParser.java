package by.bsu.webApi.util;

import by.bsu.clientAgentChat.entity.WebCommand;

import java.util.HashMap;
import java.util.Map;

public class WebCommandParser {

    private static Map<String, WebCommand> commands;

    static {
        commands = new HashMap<String, WebCommand>() {
            {
                put("null null", WebCommand.GET_ALL_ENTITIES);
                put("null true", WebCommand.GET_FREE_ENTITIES);
                put("null false", WebCommand.GET_UNFREE_ENTITIES);
                put("agent null", WebCommand.GET_ALL_AGENTS);
                put("agent true", WebCommand.GET_FREE_AGENTS);
                put("agent false", WebCommand.GET_UNFREE_AGENTS);
                put("client null", WebCommand.GET_ALL_CLIENTS);
                put("client true", WebCommand.GET_FREE_CLIENTS);
                put("client false", WebCommand.GET_UNFREE_CLIENTS);
            }
        };
    }

    public static WebCommand getWebCommandByParams(String type, String isFree) {
        return commands.get(type + " " + isFree);
    }
}
