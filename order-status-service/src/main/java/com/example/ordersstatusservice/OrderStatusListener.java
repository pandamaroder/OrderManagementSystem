package com.example.ordersstatusservice;


import com.example.common.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderStatusListener {

    private static final Logger log = LoggerFactory.getLogger(OrderStatusListener.class);

    @Autowired
    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;


// механизм горизонт масштабирования - группа консьюмеров - разделение по вычитке - спомошь. партиций 10
   // добавить спел на топик ней м

    @KafkaListener(topics = "order-topic")
    public void listen(OrderEvent orderEvent) {
        log.info("Received order event: {}", orderEvent);
        LocalDateTime now = LocalDateTime.now();

        // Создаем новое событие статуса заказа
        OrderStatusEvent orderStatusEvent = new OrderStatusEvent("PROCESSED", now);

       // acknowledgment.acknowledge();
        // Отправляем событие в топик order-status-topic
        kafkaTemplate.send("order-status-topic", orderStatusEvent);

        log.info("Sent order status event: {}", orderStatusEvent);
    }
}
