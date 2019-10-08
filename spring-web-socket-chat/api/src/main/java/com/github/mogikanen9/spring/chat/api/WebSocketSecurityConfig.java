package com.github.mogikanen9.spring.chat.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig
      extends AbstractSecurityWebSocketMessageBrokerConfigurer { 

    @Override    
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {

        messages
        .nullDestMatcher().authenticated()         
        .simpDestMatchers("/app**").authenticated()
        .simpSubscribeDestMatchers("/topic/**").authenticated()
        //.simpDestMatchers("/app**").hasRole("USER") 
        //.simpSubscribeDestMatchers("/topic/**").hasRole("USER")         
        //.anyMessage().denyAll();        
        .anyMessage().authenticated();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}