package com.example.ordersstatusservice.producer.producer;

import com.example.common.OrderEvent;
import com.example.orderservice.KafkaConsumerUtils;
import com.example.orderservice.TestBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderStatusProducerTest extends TestBase {

    private final BlockingQueue<ConsumerRecord<String, String>> orderEventsQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<ConsumerRecord<String, String>> orderStatusEventsQueue = new LinkedBlockingQueue<>();

    private MessageListenerContainer orderListenerContainer;
    private MessageListenerContainer orderStatusListenerContainer;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private Environment environment;

    @BeforeAll
    void setUpKafkaConsumers() {
        final String orderTopic = environment.getProperty("spring.kafka.topics.order");
        // Настраиваем консьюмеры для тестирования двух топиков - нужны чтобы проверить что в топике есьт сообщение
        orderListenerContainer = KafkaConsumerUtils.setUpKafkaConsumer(orderTopic, kafkaProperties, orderEventsQueue);

        orderStatusListenerContainer = KafkaConsumerUtils
            .setUpKafkaConsumer(kafkaProperties.getTemplate().getDefaultTopic(), kafkaProperties, orderStatusEventsQueue);
    }

    @AfterAll
    void tearDownKafkaConsumers() {
        stopKafkaContainer((KafkaMessageListenerContainer<?, ?>) orderListenerContainer);
        stopKafkaContainer((KafkaMessageListenerContainer<?, ?>) orderStatusListenerContainer);
    }

    private void stopKafkaContainer(KafkaMessageListenerContainer<?, ?> container) {
        if (container != null && container.isRunning()) {
            container.stop();
        }
    }

    @Test
    void testProducerSentOrder() throws InterruptedException {
        final String orderTopic = environment.getProperty("spring.kafka.topics.order");
        // Отправляем сообщение в order-topic
        OrderEvent orderEvent = new OrderEvent("product", 1);
        kafkaTemplate.send(orderTopic, orderEvent);
        final var received = orderEventsQueue.poll(10, TimeUnit.SECONDS);
        // проверили что в OrderEvent топике есть точно сообщение которое нужно вычитать == PRE CONDITION для  продьюсера
        assertThat(received).isNotNull();
        assertThat(received.value()).startsWith("{\"product\":");

        AtomicReference<ConsumerRecord<String, String>> receivedOrderStatusEvent = new AtomicReference<>();

        Awaitility.await()
            .atMost(10, TimeUnit.SECONDS)
            .pollInterval(500, TimeUnit.MILLISECONDS)
            .until(() -> {
                receivedOrderStatusEvent.set(orderStatusEventsQueue.poll());
                return receivedOrderStatusEvent.get() != null;
            });

        assertThat(receivedOrderStatusEvent).isNotNull();
        assertThat(receivedOrderStatusEvent.get().value()).startsWith("{\"status\":\"PROCESSED\",\"date\"");
        final Header[] headers = receivedOrderStatusEvent.get().headers().toArray();
        final var headerNames = Arrays.stream(headers)
            .map(Header::key)
            .toList();
        assertThat(headerNames)
            .hasSize(1)
            .containsExactlyInAnyOrder("__TypeId__");
    }
}
