package by.bsu.webApi.service;

import by.bsu.clientAgentChat.entity.Type;
import by.bsu.clientAgentChat.entity.WebCommand;
import com.google.gson.Gson;

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
            bufferedWriter.write(Type.WEBAPI + "\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    public Response getEntities(@QueryParam("type") String type,
                                @QueryParam("free") String isFree) throws IOException {
        if (type == null) {
            if (isFree == null) {
                bufferedWriter.write(WebCommand.GET_ALL_ENTITIES + "\n");
            }
            else if (isFree.equals("true")) {
                bufferedWriter.write(WebCommand.GET_FREE_ENTITIES + "\n");
            }
            else if (isFree.equals("false")) {
                bufferedWriter.write(WebCommand.GET_UNFREE_ENTITIES + "\n");
            }
        }
        if (type != null) {
            if (type.equals("agent")) {
                if (isFree == null) {
                    bufferedWriter.write(WebCommand.GET_ALL_AGENTS + "\n");
                }
                else if (isFree.equals("true")) {
                    bufferedWriter.write(WebCommand.GET_FREE_AGENTS + "\n");
                }
                else if (isFree.equals("false")) {
                    bufferedWriter.write(WebCommand.GET_UNFREE_AGENTS + "\n");
                }
            }
            else {
                if (isFree == null) {
                    bufferedWriter.write(WebCommand.GET_ALL_CLIENTS + "\n");
                }
                else if (isFree.equals("true")){
                    bufferedWriter.write(WebCommand.GET_FREE_CLIENTS + "\n");
                }
                else if (isFree.equals("false")) {
                    bufferedWriter.write(WebCommand.GET_UNFREE_CLIENTS + "\n");
                }
            }
        }
        bufferedWriter.flush();
        return formOkResponse(bufferedReader.readLine());
    }

    @Path("{id}")
    @GET
    public Response getEntityById(@PathParam("id") String id) throws IOException {
        bufferedWriter.write(WebCommand.GET_ENTITY_BY_ID + "\n");
        bufferedWriter.flush();
        bufferedWriter.write(id + "\n");
        bufferedWriter.flush();
        String line = bufferedReader.readLine();
        System.out.println(line);
        return formOkResponse(line);
    }

    private Response formOkResponse(Object entity) {
        return Response.ok()
                .entity(entity)
                .build();
    }


}