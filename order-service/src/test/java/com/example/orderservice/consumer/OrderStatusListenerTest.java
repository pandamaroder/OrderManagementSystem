package com.example.orderservice.consumer;

import com.example.common.OrderStatusEvent;
import com.example.orderservice.TestBase;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderStatusListenerTest extends TestBase {

    @Autowired
    private KafkaTemplate<Object, OrderStatusEvent> kafkaTemplate;

    @Autowired
    private Environment environment;

    @Test
    public void testKafkaListenerReadOrderStatus(CapturedOutput output) throws InterruptedException, ExecutionException {

        final OrderStatusEvent orderStatusEvent = new OrderStatusEvent("Order Shipped", LocalDateTime.now());

        final String orderStatusTopic = environment.getProperty("spring.kafka.topics.order-status");
        //отправляем в  топик через тестовый продьюсер
        kafkaTemplate.send(orderStatusTopic, orderStatusEvent).get();
        //ждем запись в лог
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    // Проверяем, что лог содержит нужные сообщения
                    assertThat(output.getOut())
                            //.contains("Received message: OrderStatusEvent[status=Order Shipped") // сделать более читаемую конструкцию
                            .contains("Key: null; Partition: 0; Topic: " + orderStatusTopic);
                });
    }
}

