package com.license.outside_issues.service;

import com.license.outside_issues.model.WebSocketMessage;
import com.license.outside_issues.web.api.WebSocketController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "rabbitmq.queue", id = "listener")
public class RabbitMQReceiver {
    private final WebSocketController webSocketController;
    private static final Logger logger = LogManager.getLogger(RabbitMQReceiver.class.toString());

    public RabbitMQReceiver(WebSocketController webSocketController) {
        this.webSocketController = webSocketController;
    }

    @RabbitHandler
    public void receiver(String message) {
        logger.info("Message = " + message);
        webSocketController.receivePrivateMessage(new WebSocketMessage());
    }
}
