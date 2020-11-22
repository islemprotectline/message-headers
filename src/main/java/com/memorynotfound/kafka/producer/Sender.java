package com.memorynotfound.kafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Sender {

    private static final Logger LOG = LoggerFactory.getLogger(Sender.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.topic.foo}")
    private String topicFoo;

    @Value("${app.topic.bar}")
    private String topicBar;

    public void sendFoo(String data){

       Message<String> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, topicFoo)
                .setHeader(KafkaHeaders.MESSAGE_KEY, "999")
                .setHeader(KafkaHeaders.PARTITION_ID, 0)
                .setHeader("X-Custom-Header", "Sending Custom Header with Spring Kafka islem")
                .build();

        LOG.info("sending message='{}' to topic='{}'", data, topicFoo);
        kafkaTemplate.send(message);
    }

    public void sendBar(String data){

        List<Header> headers = new ArrayList<>();
        headers.add(new RecordHeader("X-Custom-Header", "Sending Custom Header with Spring Kafka".getBytes()));

        ProducerRecord<String, String> bar = new ProducerRecord<>(topicBar, 0, "111", data, headers);
        LOG.info("sending message='{}' to topic='{}'", data, topicBar);

        kafkaTemplate.send(bar);
    }
}
