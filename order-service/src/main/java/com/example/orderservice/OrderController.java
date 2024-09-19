package com.example.orderservice;

import com.example.common.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private final KafkaProperties kafkaProperties;

    //Producer
    @PostMapping("/order")
    public void createAndPublishOrder(@RequestBody OrderEvent order) throws ExecutionException, InterruptedException {
        final String defaultTopic = kafkaProperties.getTemplate().getDefaultTopic();

        // Используем его при отправке сообщения
        kafkaTemplate.send(defaultTopic, order).get();

    }
}
