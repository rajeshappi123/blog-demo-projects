package com.github.mogikanen9.chat;

import javax.websocket.server.ServerEndpointConfig;

public class ChatEndpointConfigurator extends ServerEndpointConfig.Configurator {

    private static final String ORIGIN = "http://localhost:8080";

    @Override
       public boolean checkOrigin(String originHeaderValue) {
           return ORIGIN.equals(originHeaderValue);
       }
}
