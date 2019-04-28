package by.bsu.server.loader;

import by.bsu.clientAgentChat.entity.Command;
import by.bsu.clientAgentChat.entity.Entity;
import by.bsu.clientAgentChat.entity.Message;
import by.bsu.clientAgentChat.entity.Role;
import by.bsu.server.util.EntityHandler;
import by.bsu.server.util.WebApiHandler;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServerLoader implements Closeable {

    private static final Logger logger = Logger.getLogger(ServerLoader.class);
    private static final Gson gson = new Gson();
    private static final int THREAD_POOL_SIZE = 50;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private static Queue<Entity> clientsQueue = new ConcurrentLinkedQueue<>();
    private static Queue<Entity> freeAgents = new ConcurrentLinkedQueue<>();
    private static List<Entity> entities = Collections.synchronizedList(new ArrayList<>());

    private ServerSocket serverSocket;

    public ServerLoader(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        logger.info("A new server on port " + this.getPort() + " was started!");
    }

    public void listen() throws IOException{
        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String type = bufferedReader.readLine();
            handleType(type, socket, bufferedReader);
        }
    }

    public static List<Entity> filterEntity(Predicate<Entity> predicate) {
        return entities.stream()
                            .filter(predicate)
                            .collect(Collectors.toList());
    }

    public static void delelteEntity(String  id) {
        entities.removeIf(entity -> entity.getId().equals(id));
    }

    public static void handle(Entity entity) {
        if (entity.getRole() == Role.AGENT) {
          if (clientsQueue.size() > 0) {
              Entity client = clientsQueue.poll();
              client.setCompanion(entity);
              entity.setCompanion(client);
              executorService.submit(new EntityHandler(client));
              executorService.submit(new EntityHandler(entity));
          }
          else {
              freeAgents.add(entity);
              Message message = new Message("You has been added to the agent queue. You position: " + freeAgents.size(),
                                                                                                            Command.INFO);
              entity.sendMessage(message.toString());
              logger.info(entity + " has been added to the agent queue");
          }
        }
        else {
          if (freeAgents.size() > 0) {
              Entity agent = freeAgents.poll();
              entity.setCompanion(agent);
              agent.setCompanion(entity);
              executorService.submit(new EntityHandler(entity));
              executorService.submit(new EntityHandler(agent));
          }
          else {
              clientsQueue.add(entity);
              Message message = new Message("You has been added to the client queue. You position: " + clientsQueue.size(),
                                                                                                                Command.INFO);
              entity.sendMessage(message.toString());
              logger.info(entity + " has been added to the client queue");
          }
        }
    }

    private static void handleType(String type, Socket socket, BufferedReader bufferedReader) throws IOException {
        switch (type) {
            case "CONSOLE":
                Entity entity = gson.fromJson(bufferedReader.readLine(), Entity.class);
                entity.setSocket(socket);
                logger.info("New entity: " + entity);
                entities.add(entity);
                handle(entity);
                break;
            case "WEBAPI":
                executorService.submit(new WebApiHandler(socket));
                break;

        }
    }

    public int getPort() {
        return this.serverSocket.getLocalPort();
    }

    public static List<Entity> getEntities() {
        return entities.subList(0, entities.size());
    }

    @Override
    public void close() throws IOException {
        if (this.serverSocket != null) {
            this.serverSocket.close();
            logger.info("A server on port " + this.getPort() + " has been closed!");
        }
    }

}
