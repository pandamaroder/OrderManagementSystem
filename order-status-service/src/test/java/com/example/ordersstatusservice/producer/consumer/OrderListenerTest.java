package com.example.ordersstatusservice.producer.consumer;

import com.example.common.OrderEvent;
import com.example.orderservice.TestBase;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderListenerTest extends TestBase {

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private Environment environment;

    @Test
    void testKafkaListenerReadOrder(CapturedOutput output) throws InterruptedException {
        // Отправляем сообщение в order-topic
        final OrderEvent orderEvent = new OrderEvent("Tproduct", 1);
        final String orderTopic = environment.getProperty("spring.kafka.topics.order");
        kafkaTemplate.send(orderTopic, orderEvent);
        // Почему в логе нет сообщений от продьюсера??? Ведь запущен полный инстанс
        Awaitility.await()
            .atMost(10, TimeUnit.SECONDS)
            .pollInterval(500, TimeUnit.MILLISECONDS)
            .untilAsserted(() -> {
                // Проверяем, что лог содержит нужные сообщения
                assertThat(output.getOut())
                    .contains("OrderEvent[product=Tproduct, quantity=1]");
            });

    }
}
