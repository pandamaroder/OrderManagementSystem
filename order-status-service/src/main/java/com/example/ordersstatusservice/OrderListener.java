package com.example.ordersstatusservice;

import com.example.common.OrderEvent;
import com.example.common.OrderStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderListener {

    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;

    @KafkaListener(topics = "${spring.kafka.topics.order}")
    public void listen(OrderEvent orderEvent) {
        log.info("Received order event: {}", orderEvent);
        LocalDateTime now = LocalDateTime.now();

        // Создаем новое событие статуса заказа
        OrderStatusEvent orderStatusEvent = new OrderStatusEvent("PROCESSED", now);

        // Отправляем событие в топик order-status-topic
        kafkaTemplate.send("order-status-topic", orderStatusEvent);

        log.info("Sent order status event: {}", orderStatusEvent);
    }
}
