
package com.mogikanensoftware.azure.accountreceiver.config;

import javax.jms.ConnectionFactory;
import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ErrorHandler;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@EnableTransactionManagement
public class JmsConfiguration {

    @Bean
    public JmsListenerContainerFactory jmsListenerContainerFactory(final ConnectionFactory connectionFactory) {
        final DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
        jmsListenerContainerFactory.setSessionAcknowledgeMode(javax.jms.Session.CLIENT_ACKNOWLEDGE);

        // anonymous class
        jmsListenerContainerFactory.setErrorHandler(
                new ErrorHandler() {
                    @Override
                    public void handleError(Throwable t) {
                        log.error("An error has occurred in the transaction: {}", t.getMessage());
                    }
                });

        jmsListenerContainerFactory.setTransactionManager(this.jmsTransactionManager(connectionFactory));
        return jmsListenerContainerFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory jmsConnectionFactory) {
        final JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(jmsConnectionFactory);
        jmsTemplate.setSessionTransacted(true); 
        //jmsTemplate.setDeliveryDelay(500);        
        return jmsTemplate;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager jmsTransactionManager(final ConnectionFactory connectionFactory) {
        return new JmsTransactionManager(connectionFactory);
    }

    @Bean
    public PlatformTransactionManager jpaTransactionManager(final EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}