package com.dgmf.config;

import com.dgmf.messaging.Receiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.dgmf")
public class AppConfig {
    private static final String topicExchangeName = "spring-boot-exchange";
    private static final String queueName = "spring-boot";

    // Queue Declaration
    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    // Exchange Declaration
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    // Binding Between Exchange and Queue Declaration
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange)
                .with("foo.bar.#");
    }

    // Component to Send some Messages to the Listener
    // Because the Receiver class is a POJO, it needs to be
    // wrapped in the MessageListenerAdapter, where you specify
    // that it invokes receiveMessage.
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(
                receiver, "receiveMessage"
        );
    }

    // Configure a Message Listener Container
    @Bean
    SimpleMessageListenerContainer container(
            ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter
    ) {
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        // The Bean (receiver) defined in the listenerAdapter()
        // Method is Registered as a Message Listener in the
        // Container. It listens for messages on the "spring-boot" queue.
        container.setMessageListener(listenerAdapter);

        return container;
    }
}
