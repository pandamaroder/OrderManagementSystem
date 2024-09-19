package com.example.orderservice;

import com.example.common.OrderStatusEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderStatusListener {

    /*Этот компонент будет слушать топик order-status-topic и выводить информацию о сообщении:*/
    /*@KafkaListener в Spring используется для создания слушателей (consumers) Kafka.
    Этот слушатель автоматически получает сообщения
    из указанных топиков Kafka.*/

    @KafkaListener(topics = "${spring.kafka.topics.order-status}")
    public void listen(ConsumerRecord<String, OrderStatusEvent> consumerRecord) {
        // Обработка сообщения
        log.info("Received message: {}", consumerRecord.value());
        log.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}",
                consumerRecord.key(), consumerRecord.partition(),
                consumerRecord.topic(), consumerRecord.timestamp());
    }
}
