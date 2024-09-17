package com.example.orderservice;

import com.example.common.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    //Producer
    @PostMapping("/order")
    public void createAndPublishOrder(@RequestBody OrderEvent order) throws ExecutionException, InterruptedException {

        kafkaTemplate.send("order-topic", order).get();
    }
}
