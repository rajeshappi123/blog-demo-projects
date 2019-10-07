package com.github.mogikanen9.chat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;

import lombok.extern.slf4j.Slf4j;

/**
 * Chat Server
 *
 */
@Slf4j
public class App {
    public static void main(String[] args) throws DeploymentException, IOException, InterruptedException {
        Server server = new Server("localhost", 8082, "", null, MyChatEndpoint.class);
        server.start();
        log.info("---- Server Started -----");
        new CountDownLatch(1).await();
    }
}
