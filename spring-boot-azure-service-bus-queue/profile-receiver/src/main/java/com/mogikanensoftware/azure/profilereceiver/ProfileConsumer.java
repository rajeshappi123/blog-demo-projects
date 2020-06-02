
package com.mogikanensoftware.azure.profilereceiver;

import java.util.concurrent.CompletableFuture;

import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.IQueueClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProfileConsumer implements Ordered {

    @Autowired
    private final IQueueClient iqClient;

    public ProfileConsumer(final IQueueClient isc) {
        this.iqClient = isc;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void consume() throws Exception {
        this.iqClient.registerMessageHandler(new IMessageHandler() {

            @Override
            public CompletableFuture<Void> onMessageAsync(IMessage message) {
                log.info("received message " + new String(message.getBody()) + " with body ID " + message.getMessageId());
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public void notifyException(Throwable exception, ExceptionPhase phase) {
                log.error("eeks!", exception);
            }
        });
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}