package by.bsu.webApi.service;

import by.bsu.clientAgentChat.entity.Type;
import by.bsu.clientAgentChat.entity.WebCommand;
import by.bsu.webApi.constant.WebApiErrors;
import by.bsu.webApi.util.WebCommandParser;
import com.google.gson.Gson;
import com.mysql.cj.util.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.Socket;

@Path("/entities")
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON})
public class EntityService {

    private static final Gson gson = new Gson();
    private static Socket socket;
    private static BufferedWriter bufferedWriter;
    private static BufferedReader bufferedReader;

    static {
        try {
            socket = new Socket("localhost", 5555);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(gson.toJson(Type.WEBAPI) + "\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    public Response getEntities(@QueryParam("type") String type,
                                @QueryParam("free") String isFree) throws IOException {
        WebCommand command = WebCommandParser.getWebCommandByParams(type, isFree);
        if (command == null) {
            return formBadResponse(404, WebApiErrors.incorrectRequest);
        }
        bufferedWriter.write(gson.toJson(command)+ "\n");
        bufferedWriter.flush();
        return formOkResponse(bufferedReader.readLine());
    }

    @Path("{id}")
    @GET
    public Response getEntityById(@PathParam("id") String id) throws IOException {
        bufferedWriter.write(gson.toJson(WebCommand.GET_ENTITY_BY_ID) + "\n");
        bufferedWriter.flush();
        bufferedWriter.write(id + "\n");
        bufferedWriter.flush();
        String line = bufferedReader.readLine();
        if (StringUtils.isNullOrEmpty(line)) {
            return formBadResponse(404, WebApiErrors.incorrectID);
        }
        return formOkResponse(line);
    }

    private Response formOkResponse(Object entity) {
        return Response.ok()
                .entity(entity)
                .build();
    }

    private Response formBadResponse(int status, Object entity) {
        return Response.status(status)
                .entity(entity)
                .build();
    }


}