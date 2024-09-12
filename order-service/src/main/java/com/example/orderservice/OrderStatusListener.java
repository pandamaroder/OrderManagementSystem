package com.example.orderservice;

import com.example.common.OrderEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusListener {

    /*Этот компонент будет слушать топик order-status-topic и выводить информацию о сообщении:*/
    /*@KafkaListener в Spring используется для создания слушателей (consumers) Kafka.
    Этот слушатель автоматически получает сообщения
    из указанных топиков Kafka.*/

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStatusListener.class);

    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void listen(ConsumerRecord<String, OrderEvent> record, Acknowledgment acknowledgment) {
        // Обработка сообщения
        LOGGER.info("Received message: {}", record.value());
        LOGGER.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}",
                record.key(), record.partition(), record.topic(), record.timestamp());

        // Подтверждение смещения после успешной обработки
        acknowledgment.acknowledge();
    }





/*    public void listen(OrderEvent message, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp) {
        log.info("Received message: {}", message);
        log.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}", key, partition, topic, timestamp);
    }*/
}
