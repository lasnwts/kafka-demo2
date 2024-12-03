package com.specialist.kafka_demo.servive;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;


@Configuration
@EnableKafka
public class KafkaListenerConsonants {

    private final BaseProcess baseProcess;

    Logger logger = LoggerFactory.getLogger(KafkaListenerConsonants.class);

    public KafkaListenerConsonants(BaseProcess baseProcess) {
        this.baseProcess = baseProcess;
    }

    @KafkaListener(topics = "${consonants.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void orderListener(ConsumerRecord<Long, String> recordKafka, Acknowledgment ack) {
        ack.acknowledge(); //Сообщение забираем сразу
        if (recordKafka.value() != null) {
            logger.debug("[Consonants]:KafkaListener(record.partition) == {}", recordKafka.partition());
            logger.debug("[Consonants]:KafkaListener(record.key)       == {}", recordKafka.key());
            logger.debug("[Consonants]:KafkaListener(record.value)     == {}", recordKafka.value());
            logger.debug("[Consonants]:KafkaListener(topic)            == {}", recordKafka.topic());
            logger.debug("[Consonants]:KafkaListener(Offset)           == {}", recordKafka.offset());
            baseProcess.process(recordKafka.value(), recordKafka.topic());
        }

    }
}
