package com.github.mogikanen9.chat;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;

@ServerEndpoint(
    value = "/chat/{username}", 
    decoders = MessageDecoder.class, 
    encoders = MessageEncoder.class,
    configurator = ChatEndpointConfigurator.class)
@Slf4j    
public class MyChatEndpoint {

    private Session session;

    private static Map<String, MyChatEndpoint> endPoints = new ConcurrentHashMap<>();
    private static Map<String, String> users = new ConcurrentHashMap<>();

    protected Session getSession() {
        return this.session;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {       
        log.debug(String.format("onOpen#session.getId()->%s, username->%s",session.getId(), username));
        this.session = session;
        endPoints.put(session.getId(), this);
        users.put(session.getId(), username);
        this.broadcast(new Message(username,String.format("new user joined with id=%s", session.getId())));
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException {
        log.debug(String.format("onMessage->%s",message));
        this.broadcast(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        endPoints.remove(session.getId());
        String username = users.get(session.getId());
        users.remove(session.getId());
        this.broadcast(new Message(username, String.format("user with id=%s has just left",session.getId())));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
    }

    protected void broadcast(Message message) {
        endPoints.entrySet().forEach(entry -> {
            synchronized (entry) {
                try {
                    entry.getValue().getSession().getBasicRemote().sendObject(message);
                    log.debug(String.format("broadcasted to session->%s",entry.getValue().getSession().getId()));
                } catch (IOException | EncodeException e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }
}