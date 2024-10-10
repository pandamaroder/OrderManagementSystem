package com.example.ordersstatusservice;

import com.example.common.OrderEvent;
import com.example.common.OrderStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderListener {

    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;

    @KafkaListener(topics = "${spring.kafka.topics.order}")
    public void listen(ConsumerRecord<String, OrderEvent> consumerRecord) throws ExecutionException, InterruptedException {
        log.info("Received order event: {}", consumerRecord.value());
        final String stringFromValue = consumerRecord.value().toString();
        log.info("Order event as a String: {}", stringFromValue);
        LocalDateTime now = LocalDateTime.now();
        // формируем событие для топика order Status
        OrderStatusEvent orderStatusEvent = new OrderStatusEvent("PROCESSED", now, consumerRecord.value());

        // Отправляем событие в деф топик сервиса по OrderStatusEvent
        kafkaTemplate.sendDefault(orderStatusEvent).get();

        log.info("Sent order status event: {}", orderStatusEvent);
    }
}
