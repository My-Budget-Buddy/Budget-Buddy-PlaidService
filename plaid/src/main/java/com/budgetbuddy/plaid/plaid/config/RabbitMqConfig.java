package com.budgetbuddy.plaid.plaid.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${eureka.instance.hostname}")
    private String host;

    // Exchanges
    @Value("${exchanges.direct}")
    private String directExchange;

    // Set up credentials and connect to RabbitMQ:
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    // Configure the RabbitTemplate:
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setReplyTimeout(6000);
        return rabbitTemplate;
    }

    // Create the exchange:
    @Bean
    public Exchange directExchange() {
        return new DirectExchange(directExchange);
    }

    // Define the queue for handling Plaid link requests:
    @Bean
    public Queue plaidLinkQueue() {
        return new Queue("plaid-link-queue");
    }

    // Define the queue for handling Plaid account requests:
    @Bean
    public Queue plaidAccountQueue() {
        return new Queue("plaid-account-queue");
    }

    // Bind the Plaid link queues to the exchange:
    @Bean
    public Binding plaidLinkBinding(Queue plaidLinkQueue, Exchange directExchange) {
        return BindingBuilder.bind(plaidLinkQueue)
                .to(directExchange)
                .with("plaid-link-routing-key")
                .noargs();
    }

    // Bind the Plaid account queues to the exchange:
    @Bean
    public Binding plaidAccountBinding(Queue plaidAccountQueue, Exchange directExchange) {
        return BindingBuilder.bind(plaidAccountQueue)
                .to(directExchange)
                .with("plaid-account-routing-key")
                .noargs();
    }

    // Configure the message converter:
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}